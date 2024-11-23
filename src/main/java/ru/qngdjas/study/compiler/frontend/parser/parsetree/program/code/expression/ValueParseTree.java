package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.Token;

public record ValueParseTree(Token value) implements ExpressionComponent {

    @Override
    public String toString() {
        return String.format("\"%s\"", value.getValue());
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitValueParseTree(this);
    }
}
