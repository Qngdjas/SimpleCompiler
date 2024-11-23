package ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.ParseTree;
import ru.qngdjas.study.compiler.frontend.Token;

public final class VarParseTree implements ParseTree {

    private final Token id;
    private Token type;

    public VarParseTree(Token id) {
        this.id = id;
    }

    public Token getId() {
        return id;
    }

    public void setType(Token type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("{\"%s\": \"%s\", \"type\": \"%s\"}", id.getType(), id.getValue(), type.getValue());
    }

    @Override
    public String accept(ParserTreeVisitor visitor) {
        return visitor.visitVarParseTree(this);
    }
}
