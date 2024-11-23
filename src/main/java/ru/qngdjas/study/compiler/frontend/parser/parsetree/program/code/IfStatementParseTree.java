package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;

public class IfStatementParseTree extends StatementParseTree {

    private CodeParseTree thenPT;
    private CodeParseTree elsePT;

    public void setThenPT(CodeParseTree thenPT) {
        this.thenPT = thenPT;
    }

    public void setElsePT(CodeParseTree elsePT) {
        this.elsePT = elsePT;
    }

    public CodeParseTree getThenPT() {
        return thenPT;
    }

    public CodeParseTree getElsePT() {
        return elsePT;
    }

    @Override
    public String toString() {
        return String.format("{\"if\": {%s, \"then\": %s, \"else\": %s}}", super.toString(), thenPT, elsePT);
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitIfStatementParseTree(this);
    }
}
