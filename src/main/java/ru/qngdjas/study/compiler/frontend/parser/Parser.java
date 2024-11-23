package ru.qngdjas.study.compiler.frontend.parser;

import ru.qngdjas.study.compiler.frontend.parser.exception.EmptyValueException;
import ru.qngdjas.study.compiler.frontend.parser.exception.ExpectedTokenException;
import ru.qngdjas.study.compiler.frontend.parser.exception.SyntaxParseException;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.*;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ExpressionComponent;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ExpressionParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ValueParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.ProgramParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarsParseTree;
import ru.qngdjas.study.compiler.frontend.Token;

import java.util.*;

/**
 * Парсер Pascal, реализующий следующую грамматику:<p>
 * <u>Программа:</u>
 * <pre>
 *    <программа> ::= PROGRAM ID SEMICOLON <описание> <блок> DOT
 * </pre>
 * <u>Объявление переменных:</u>
 * <pre>
 *     <описание> ::= VAR <объявление> |
 *                    EMPTY
 *
 *   <объявление> ::= <объявление> <переменные> COLON TYPE SEMICOLON |
 *                    <переменные> COLON TYPE SEMICOLON
 *
 *   <переменные> ::= <переменные> COMMA ID |
 *                    ID
 * </pre><p>
 * <u>Алгоритм:</u>
 * <pre>
 *         <блок> ::= BEGIN <операторы> END
 *
 *    <операторы> ::= <операторы> <оператор> SEMICOLON |
 *                    EMPTY
 *
 *     <оператор> ::= <условие> |
 *                    <цикл> |
 *                    <присваивание> |
 *                    <печать>
 *
 *      <условие> ::= IF <выражение> THEN <блок> ELSE <блок> |
 *                    IF <выражение> THEN <блок>
 *
 *         <цикл> ::= WHILE <выражение> DO <блок>
 *
 * <присваивание> ::= ID ASSIGN <выражение>
 *
 *       <печать> ::= WRITELN LEFT_PARENTHESIS <выражение> RIGHT_PARENTHESIS
 *
 *    <выражение> ::= <слагаемое> <действие> <слагаемое> |
 *                    <слагаемое>
 *
 *    <слагаемое> ::= <значение> <произведение> <значение> |
 *                    <значение>
 *
 * </pre><p>
 * <u>Общее:</u>
 * <pre>
 * <произведение> ::= MULTIPLY |
 *                    DIVIDE
 *
 *     <действие> ::= PLUS |
 *                    MINUS |
 *                    MORE |
 *                    MORE OR EQUALS |
 *                    LESS |
 *                    LESS OR EQUALS |
 *                    EQUAL
 *
 *     <значение> ::= LEFT_PARENTHESIS <выражение> RIGHT_PARENTHESIS |
 *                    LITERAL |
 *                    ID
 *
 *      <пропуск> ::= SPACE |
 *                    TAB |
 *                    NEWLINE
 *
 *       <ошибка> ::= BANNED |
 *                    UNKNOWN
 *
 * </pre>
 */
public class Parser {

    enum ExpectedToken {

        PROGRAM(Token.Type.KEYWORD, "program"),
        VAR(Token.Type.KEYWORD, "var"),
        BEGIN(Token.Type.KEYWORD, "begin"),
        END(Token.Type.KEYWORD, "end"),
        IF(Token.Type.KEYWORD, "if"),
        THEN(Token.Type.KEYWORD, "then"),
        ELSE(Token.Type.KEYWORD, "else"),
        WHILE(Token.Type.KEYWORD, "while"),
        DO(Token.Type.KEYWORD, "do"),
        WRITELN(Token.Type.KEYWORD, "writeln"),

        TYPE(Token.Type.TYPE),

        LITERAL(Token.Type.LITERAL),

        ASSIGN(Token.Type.OPERATOR, ":="),
        MULTIPLY(Token.Type.OPERATOR, "*"),
        DIVIDE(Token.Type.OPERATOR, "/"),
        PLUS(Token.Type.OPERATOR, "+"),
        MINUS(Token.Type.OPERATOR, "-"),
        LESS(Token.Type.OPERATOR, "<"),
        LESS_OR_EQUALS(Token.Type.OPERATOR, "<="),
        MORE(Token.Type.OPERATOR, ">"),
        MORE_OR_EQUALS(Token.Type.OPERATOR, ">="),
        EQUALS(Token.Type.OPERATOR, "="),

        LEFT_PARENTHESIS(Token.Type.SEPARATOR, "("),
        RIGHT_PARENTHESIS(Token.Type.SEPARATOR, ")"),
        COMMA(Token.Type.SEPARATOR, ","),
        DOT(Token.Type.SEPARATOR, "."),
        COLON(Token.Type.SEPARATOR, ":"),
        SEMICOLON(Token.Type.SEPARATOR, ";"),

        ID(Token.Type.ID);

        private final Token token;

        ExpectedToken(Token.Type type) {
            token = new Token(type);
        }

        ExpectedToken(Token.Type type, String value) {
            token = new Token(type, value);
        }

        public Token getToken() {
            return token;
        }
    }

    private ArrayDeque<Token> tokens;
    private Token previous;
    private Token pointer;

    public Parser() {
        tokens = new ArrayDeque<>();
    }

    public void setTokens(ArrayDeque<Token> tokens) {
        this.tokens = new ArrayDeque<>(tokens);
    }

    public ProgramParseTree parse() throws SyntaxParseException {
        return program();
    }

    private ProgramParseTree program() throws SyntaxParseException {
        ProgramParseTree programST = new ProgramParseTree();
        next(ExpectedToken.PROGRAM);
        next(ExpectedToken.ID);
        programST.setName(pointer);
        next(ExpectedToken.SEMICOLON);
        programST.setVarPT(var());
        programST.setCodePT(code());
        next(ExpectedToken.DOT);
        return programST;
    }

    private VarsParseTree var() throws SyntaxParseException {
        VarsParseTree varsST = new VarsParseTree();
        if (checkNext(ExpectedToken.VAR)) {
            next(ExpectedToken.VAR);
            varsST.addVars(varsDeclaration());
        }
        return varsST;
    }

    private List<VarParseTree> varsDeclaration() {
        List<VarParseTree> varsPT = new ArrayList<>();
        do {
            varsPT.addAll(vars());
        } while (!checkNext(ExpectedToken.BEGIN));
        return varsPT;
    }

    private List<VarParseTree> vars() {
        List<VarParseTree> vars = new ArrayList<>();
        do {
            next(ExpectedToken.ID);
            vars.add(new VarParseTree(pointer));
            next();
        } while (expect(ExpectedToken.COMMA));
        if (!expect(ExpectedToken.COLON)) {
            throw new ExpectedTokenException(ExpectedToken.COLON.getToken(), pointer);
        }
        next(ExpectedToken.TYPE);
        vars.forEach(s -> s.setType(pointer));
        next(ExpectedToken.SEMICOLON);
        return vars;
    }

    private CodeParseTree code() throws SyntaxParseException {
        CodeParseTree codeST = new CodeParseTree();
        next(ExpectedToken.BEGIN);
        codeST.addStatements(statements());
        next(ExpectedToken.END);
        return codeST;
    }

    private List<StatementParseTree> statements() {
        List<StatementParseTree> statements = new ArrayList<>();
        while (!checkNext(ExpectedToken.END)) {
            statements.add(statement());
            next(ExpectedToken.SEMICOLON);
        }
        return statements;
    }

    private StatementParseTree statement() {
        if (checkNext(ExpectedToken.IF)) {
            return ifStatement();
        }
        if (checkNext(ExpectedToken.WHILE)) {
            return whileStatement();
        }
        if (checkNext(ExpectedToken.ID)) {
            return assignmentStatement();
        }
        if (checkNext(ExpectedToken.WRITELN)) {
            return printStatement();
        }
        throw new SyntaxParseException(pointer, "Ожидался оператор");
    }

    private IfStatementParseTree ifStatement() {
        IfStatementParseTree iStatementPT = new IfStatementParseTree();
        next(ExpectedToken.IF);
        iStatementPT.setExpressionPT(expression());
        next(ExpectedToken.THEN);
        iStatementPT.setThenPT(code());
        if (checkNext(ExpectedToken.ELSE)) {
            next(ExpectedToken.ELSE);
            iStatementPT.setElsePT(code());
        }
        return iStatementPT;
    }

    private WhileStatementParseTree whileStatement() {
        WhileStatementParseTree wStatementPT = new WhileStatementParseTree();
        next(ExpectedToken.WHILE);
        wStatementPT.setExpressionPT(expression());
        next(ExpectedToken.DO);
        wStatementPT.setDoPT(code());
        return wStatementPT;
    }

    private AssignmentStatementParseTree assignmentStatement() {
        AssignmentStatementParseTree aStatementPT = new AssignmentStatementParseTree();
        next(ExpectedToken.ID);
        aStatementPT.setName(pointer);
        next(ExpectedToken.ASSIGN);
        aStatementPT.setExpressionPT(expression());
        return aStatementPT;
    }

    private PrintStatementParseTree printStatement() {
        PrintStatementParseTree pStatementPT = new PrintStatementParseTree();
        next(ExpectedToken.WRITELN);
        next(ExpectedToken.LEFT_PARENTHESIS);
        if (!checkNext(ExpectedToken.RIGHT_PARENTHESIS)) {
            pStatementPT.setExpressionPT(expression());
        }
        next(ExpectedToken.RIGHT_PARENTHESIS);
        return pStatementPT;
    }

    private ExpressionComponent expression() {
        ExpressionComponent expressionPT = term();
        while (checkNextAction()) {
            nextAction();
            expressionPT = new ExpressionParseTree(pointer, expressionPT, term());
        }
        return expressionPT;
    }

    private ExpressionComponent term() {
        ExpressionComponent expressionPT = value();
        while (checkNextProduct()) {
            nextProduct();
            expressionPT = new ExpressionParseTree(pointer, expressionPT, value());
        }
        return expressionPT;
    }

    private ExpressionComponent value() {
        if (checkNext(ExpectedToken.LEFT_PARENTHESIS)) {
            next(ExpectedToken.LEFT_PARENTHESIS);
            ExpressionComponent expressionPT = expression();
            next(ExpectedToken.RIGHT_PARENTHESIS);
            return expressionPT;
        }
        if (checkNext(ExpectedToken.ID)) {
            next(ExpectedToken.ID);
            return new ValueParseTree(pointer);
        }
        if (checkNext(ExpectedToken.LITERAL)) {
            next(ExpectedToken.LITERAL);
            return new ValueParseTree(pointer);
        }
        throw new SyntaxParseException(pointer, "Неопределенное значение");
    }

    private boolean checkNextAction() {
        return checkNext(ExpectedToken.PLUS)
                || checkNext(ExpectedToken.MINUS)
                || checkNext(ExpectedToken.MORE)
                || checkNext(ExpectedToken.MORE_OR_EQUALS)
                || checkNext(ExpectedToken.LESS)
                || checkNext(ExpectedToken.LESS_OR_EQUALS)
                || checkNext(ExpectedToken.EQUALS);
    }

    private void nextAction() {
        if (checkNext(ExpectedToken.PLUS)) {
            next(ExpectedToken.PLUS);
            return;
        }
        if (checkNext(ExpectedToken.MINUS)) {
            next(ExpectedToken.MINUS);
            return;
        }
        if (checkNext(ExpectedToken.MORE)) {
            next(ExpectedToken.MORE);
            return;
        }
        if (checkNext(ExpectedToken.MORE_OR_EQUALS)) {
            next(ExpectedToken.MORE_OR_EQUALS);
            return;
        }
        if (checkNext(ExpectedToken.LESS)) {
            next(ExpectedToken.LESS);
            return;
        }
        if (checkNext(ExpectedToken.LESS_OR_EQUALS)) {
            next(ExpectedToken.LESS_OR_EQUALS);
            return;
        }
        if (checkNext(ExpectedToken.EQUALS)) {
            next(ExpectedToken.EQUALS);
            return;
        }
        throw new SyntaxParseException(pointer, "Неопределенное действие");
    }

    private boolean checkNextProduct() {
        return checkNext(ExpectedToken.MULTIPLY)
                || checkNext(ExpectedToken.DIVIDE);
    }

    private void nextProduct() {
        if (checkNext(ExpectedToken.MULTIPLY)) {
            next(ExpectedToken.MULTIPLY);
            return;
        }
        if (checkNext(ExpectedToken.DIVIDE)) {
            next(ExpectedToken.DIVIDE);
            return;
        }
        throw new SyntaxParseException(pointer, "Неопределенное произведение");
    }

    private boolean checkNext(ExpectedToken expected) {
        next();
        boolean result = expect(expected);
        prev();
        return result;
    }

    private void next() {
        previous = pointer;
        do {
            pointer = tokens.poll();
            if (pointer == null || isDenied(pointer.getType())) {
                throw new SyntaxParseException(pointer);
            }
        } while (pointer.getType().equals(Token.Type.SPACE));
        if (pointer.getValue() == null) {
            throw new EmptyValueException(pointer);
        }
    }

    private void next(ExpectedToken expected) throws SyntaxParseException {
        next();
        if (!expect(expected)) {
            throw new ExpectedTokenException(expected.getToken(), pointer);
        }
    }

    private void prev() {
        tokens.addFirst(pointer);
        pointer = previous;
        previous = null;
    }

    private boolean expect(ExpectedToken expected) {
        return expected.getToken().equals(pointer);
    }

    private boolean isDenied(Token.Type tokenType) {
        return tokenType.equals(Token.Type.BANNED)
                || tokenType.equals(Token.Type.UNKNOWN);
    }

}
