package com.thoughtworks.gauge.scan

import com.github.javaparser.Position
import com.github.javaparser.Range
import kastree.ast.Node
import kastree.ast.psi.Converter
import kastree.ast.psi.Parser
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.psiUtil.endOffset
import org.jetbrains.kotlin.psi.psiUtil.startOffset

const val LINE_SEPARATOR = '\n'

class KotlinParser(code: String) : Parser(object : Converter() {
    override fun onNode(node: Node, elem: PsiElement) {
        if (node is Node.Decl.Func || node is Node.Decl.Func.Param) {
            node.tag = Range(getPosition(elem.startOffset), getPosition(elem.endOffset))
        }
        super.onNode(node, elem)
    }

    private fun getPosition(offset: Int): Position {
        val line = code.take(offset).count { it == LINE_SEPARATOR } + 1
        val column = offset - code.lastIndexOf(LINE_SEPARATOR, offset)
        return Position(line, column)
    }
})
