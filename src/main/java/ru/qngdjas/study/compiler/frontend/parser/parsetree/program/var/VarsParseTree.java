package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public final class VarsParseTree implements ParseTree {

    private final List<VarParseTree> vars = new ArrayList<>();

    public void addVars(List<VarParseTree> vars) {
        this.vars.addAll(vars);
    }

    public List<VarParseTree> getVars() {
        return vars;
    }

    @Override
    public String toString() {
        return vars.toString();
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitVarsParseTree(this);
    }
}
