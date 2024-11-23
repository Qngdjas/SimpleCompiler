package ru.qngdjas.study.compiler.frontend.parser.exception;

import ru.qngdjas.study.compiler.frontend.Token;

/**
 * Класс исключений парсинга, сообщающий об ожидаемом токене.
 */
public final class ExpectedTokenException extends SyntaxParseException {

    /**
     * Создает исключение парсинга со стандартным сообщением об ошибке.
     *
     * @param expected Ожидаемый токен.
     * @param actual   Токен из-за которого произошла ошибка.
     */
    public ExpectedTokenException(Token expected, Token actual) {
        super(actual, String.format("Ожидался %s(%s)", expected.getType(), expected.getValue()));
    }

}
