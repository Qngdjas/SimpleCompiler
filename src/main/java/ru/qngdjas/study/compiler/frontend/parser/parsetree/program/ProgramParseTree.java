package ru.qngdjas.study.compiler.frontend.parser.parsetree.program;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.ParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.CodeParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarsParseTree;
import ru.qngdjas.study.compiler.frontend.Token;

public final class ProgramParseTree implements ParseTree {

    private Token name;
    private VarsParseTree varPT;
    private CodeParseTree codePT;

    public void setName(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }

    public void setVarPT(VarsParseTree varPT) {
        this.varPT = varPT;
    }

    public VarsParseTree getVarPT() {
        return varPT;
    }

    public void setCodePT(CodeParseTree codePT) {
        this.codePT = codePT;
    }

    public CodeParseTree getCodePT() {
        return codePT;
    }

    @Override
    public String toString() {
        return String.format("{\"%s\": \"%s\", \"vars\": %s, \"code\": %s}", name.getType(), name.getValue(), varPT, codePT);
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitProgramParseTree(this);
    }
}
