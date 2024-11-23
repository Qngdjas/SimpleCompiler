package ru.qngdjas.study.compiler.frontend.parser.parsetree;

import ru.qngdjas.study.compiler.backend.generator.ParserTreeVisitor;

public interface ParseTree {

    String accept(ParserTreeVisitor visitor);
}
