package ru.qngdjas.study.compiler;

import ru.qngdjas.study.compiler.backend.generator.PythonPseudocodeExportParserTreeVisitor;
import ru.qngdjas.study.compiler.frontend.lexer.Lexer;
import ru.qngdjas.study.compiler.frontend.parser.exception.SyntaxParseException;
import ru.qngdjas.study.compiler.frontend.parser.Parser;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.ProgramParseTree;
import ru.qngdjas.study.compiler.frontend.Token;

import java.util.ArrayDeque;

public class SimpleCompiler {

    private final Lexer lexer;
    private final Parser parser;
    private final PythonPseudocodeExportParserTreeVisitor generator;

    private ProgramParseTree programPT;

    public SimpleCompiler(String programText) {
        lexer = new Lexer(programText);
        parser = new Parser();
        generator = new PythonPseudocodeExportParserTreeVisitor();
    }

    public boolean compile() {
        ArrayDeque<Token> tokens = lexing();
        parser.setTokens(tokens);
        try {
            programPT = parsing();
        } catch (SyntaxParseException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

    public String toPython() {
        if (programPT == null) {
            compile();
        }
        return generator.visitProgramParseTree(programPT);
    }

    private ArrayDeque<Token> lexing() {
        return lexer.parse();
    }

    private ProgramParseTree parsing() throws SyntaxParseException {
        return parser.parse();
    }
}
