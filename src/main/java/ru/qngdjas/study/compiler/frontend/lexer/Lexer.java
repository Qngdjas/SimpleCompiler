package ru.qngdjas.study.compiler.frontend.lexer;

import ru.qngdjas.study.compiler.frontend.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private final static Pattern TOKEN_PATTERN = Pattern.compile(Token.Type.getRegex(), Pattern.MULTILINE);
    private final String programText;

    private int parsedSymbols;

    public Lexer(String programText) {
        this.programText = programText;
        parsedSymbols = 0;
    }

    public ArrayDeque<Token> parse() {
        Matcher matcher = TOKEN_PATTERN.matcher(programText);

        ArrayDeque<Token> tokens = new ArrayDeque<>();
        int column, row = 1;
        while (matcher.find()) {
            column = matcher.start() + 1 - parsedSymbols;
            Token token = getToken(matcher, row, column);
            tokens.addLast(token);
            if (token.getType().equals(Token.Type.SPACE) && token.getValue().equals("\n")) {
                row++;
                parsedSymbols = matcher.end();
            }
        }
        return tokens;
    }

    private Token getToken(Matcher matcher, int row, int column) {
        String value = matcher.group();
        Token.Type type = Token.Type.valueOf(
                matcher.namedGroups().keySet().stream()
                        .filter(s -> matcher.group(s) != null)
                        .findFirst()
                        .orElse("UNKNOWN")
        );
        return new Token(type, value, row, column);
    }
}
