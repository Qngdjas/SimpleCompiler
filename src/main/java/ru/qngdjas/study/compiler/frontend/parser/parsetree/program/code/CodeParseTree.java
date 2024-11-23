package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public final class CodeParseTree implements ParseTree {

    private final List<StatementParseTree> statements = new ArrayList<>();

    public void addStatements(List<StatementParseTree> statements) {
        this.statements.addAll(statements);
    }

    public List<StatementParseTree> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        return statements.toString();
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitCodeParseTree(this);
    }
}
