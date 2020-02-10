package com.thoughtworks.gauge.scan

import com.github.javaparser.Range
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.body.VariableDeclaratorId
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.type.ReferenceType
import com.thoughtworks.gauge.StepRegistryEntry
import com.thoughtworks.gauge.StepValue
import com.thoughtworks.gauge.Util
import com.thoughtworks.gauge.registry.StepRegistry
import kastree.ast.Node
import kastree.ast.Node.Decl.Func
import kastree.ast.Node.Decl.Func.Param
import kastree.ast.Node.Expr.BinaryOp
import kastree.ast.Node.Expr.StringTmpl
import kastree.ast.Node.Expr.StringTmpl.Elem.Regular
import kastree.ast.Node.ValueArg
import kastree.ast.Visitor
import kastree.ast.Writer

class KotlinRegistryMethodVisitor(private val stepRegistry: StepRegistry, private val file: String) : Visitor() {

    override fun visit(v: Node?, parent: Node) {
        if (v is Func) {
            for (annotation in getStepAnnotations(v)) {
                if (annotation.args.isEmpty()) {
                    continue
                }
                addStepToRegistry(v, annotation.args)
            }
        } else {
            super.visit(v, parent)
        }
    }

    private fun addStepToRegistry(func: Func, args: List<ValueArg>) {
        val parameterizedSteps = getParameterizedSteps(args)

        for (parameterizedStep in parameterizedSteps) {
            val aliases = parameterizedSteps.toMutableList().also { it.remove(parameterizedStep) }
            val stepText = StepsUtil().getStepText(parameterizedStep)
            val stepValue = StepValue(stepText, parameterizedStep)

            val entry = StepRegistryEntry().also {
                it.name = Writer.write(func)
                it.stepText = parameterizedStep
                it.stepValue = stepValue
                it.parameters = convertFunctionParams(func.params)
                it.span = func.tag as Range
                it.hasAlias = aliases.isNotEmpty()
                it.aliases = aliases
                it.fileName = file
            }

            stepRegistry.addStep(stepValue, entry)
        }
    }

    private fun getParameterizedSteps(args: List<ValueArg>): List<String> {
        return args.map { it.expr }.fold(mutableListOf(), { acc, expr ->
            when {
                expr is BinaryOp && expr.lhs is StringTmpl && expr.rhs is StringTmpl -> {
                    acc += Util.trimQuotes(
                            concatStringParams(expr.lhs as StringTmpl) +
                                    concatStringParams(expr.rhs as StringTmpl))
                    acc
                }
                expr is StringTmpl -> {
                    acc += Util.trimQuotes(concatStringParams(expr))
                    acc
                }
                else -> acc
            }
        })
    }
}

private fun concatStringParams(stringTmpl: StringTmpl): String =
        stringTmpl.elems.filterIsInstance<Regular>().joinToString("") { it.str }

private fun convertFunctionParams(params: List<Param>) =
        params.map {
            Parameter(ReferenceType(ClassOrInterfaceType(it.type.toString())),
                    VariableDeclaratorId(it.name))
        }

private fun getStepAnnotations(node: Func) = node.anns.flatMap { it.anns }.filter { it.names.contains("Step") }
