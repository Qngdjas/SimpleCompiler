package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.Token;

public class AssignmentStatementParseTree extends StatementParseTree {

    private Token name;

    public void setName(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("{\":=\" : {\"%s\": \"%s\", %s}}", name.getType(), name.getValue(), super.toString());
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitAssignmentStatementParseTree(this);
    }
}
