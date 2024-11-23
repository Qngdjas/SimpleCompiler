package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.Token;

public record ExpressionParseTree(Token operator, ExpressionComponent left,
                                  ExpressionComponent right) implements ExpressionComponent {

    @Override
    public String toString() {
        return String.format("{\"%s\": {\"value1\": %s, \"value2\": %s}}", operator.getValue(), left.toString(), right.toString());
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitExpressionParseTree(this);
    }
}
