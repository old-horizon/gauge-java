package test.files.formatted

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.Table

class StepImpl {
    @Step("Say <hello> to <world>")
    fun helloWorld(greeting: String, name: Int): String {
    }

    @Step("step <a> and a table <table>")
    fun stepWithTable(a: Float, table: Table) {
    }

    @Step("A step with no params")
    fun someStepStep() {
    }

    @Step("Tell <greeting> to <name>")
    fun helloWorld(greeting: String, name: String) {
        println("$greeting, $name")
    }

    @Step("Tell <greeting> <name>")
    fun helloWorld(greeting: String, argName: String) {
    }

    @Step("† ‡ µ ¢ step with <Û> and <į>")
    fun stepWith(a: String, b: String) {
    }

    @Step("A step with comments")
    fun someStepWithComments() {
        //comment1
        //comment2
        /*
                    comment3
                    comment4
         */
        /*
                comment6
                    comment7
                        comment8
         */
        println("")
    }

    @Step("A step with newLine")
    fun someStepStep() {
        println("\n")
    }

    @Step("A step with \\")
    fun stepWithDualBackSlashes() {
    }

    @Step("A step with /")
    fun stepWithCommonSlash() {
    }

    @Step("A step with //")
    fun stepWithCommonDualSlashes() {
    }

    @Step("A step with |")
    fun stepWithPipeline() {
    }

    @Step("A step 123")
    fun stepWithTab() {
    }

    @Step("A step with ` ~ = + ? # $ % ^ ! & * ( ) : ; , . - _ [ ]")
    fun stepWithSpecialChars() {
    }

    @Step("A step defined with alias syntax")
    fun stepDefinedWithAliasSyntax() {
    }
}