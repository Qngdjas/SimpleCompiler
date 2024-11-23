package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;

public class WhileStatementParseTree extends StatementParseTree {

    private CodeParseTree doPT;

    public void setDoPT(CodeParseTree doPT) {
        this.doPT = doPT;
    }

    public CodeParseTree getDoPT() {
        return doPT;
    }

    @Override
    public String toString() {
        return String.format("{\"while\" : {%s, \"do\": %s}}", super.toString(), doPT);
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitWhileStatementParseTree(this);
    }
}
