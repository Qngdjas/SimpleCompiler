package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.frontend.parser.parsetree.ParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ExpressionComponent;

abstract public class StatementParseTree implements ParseTree {

    private ExpressionComponent expressionPT;

    public void setExpressionPT(ExpressionComponent expressionPT) {
        this.expressionPT = expressionPT;
    }

    public ExpressionComponent getExpressionPT() {
        return expressionPT;
    }

    @Override
    public String toString() {
        return String.format("\"expression\": %s", expressionPT.toString());
    }
}
