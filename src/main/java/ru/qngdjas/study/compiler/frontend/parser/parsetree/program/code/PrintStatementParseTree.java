package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;

public class PrintStatementParseTree extends StatementParseTree {

    @Override
    public String toString() {
        return String.format("{\"writeln\" : {%s}}", super.toString());
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitPrintStatementParseTree(this);
    }
}
