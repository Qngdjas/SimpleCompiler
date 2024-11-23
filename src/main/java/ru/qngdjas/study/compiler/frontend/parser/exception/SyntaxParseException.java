package ru.qngdjas.study.compiler.frontend.parser.exception;

import ru.qngdjas.study.compiler.frontend.Token;

/**
 * Базовый класс исключений парсинга, указывающий на синтаксические ошибки.
 */
public class SyntaxParseException extends RuntimeException {

    /**
     * Создает исключение парсинга с указанным сообщением.
     *
     * @param token   Токен из-за которого произошла ошибка.
     * @param message Сообщение об ошибке.
     */
    public SyntaxParseException(Token token, String message) {
        super(String.format("Встречен токен %s. %s", token, message));
    }

    /**
     * Создает исключение парсинга со стандартным сообщением об ошибке.
     *
     * @param token Токен из-за которого произошла ошибка.
     */
    public SyntaxParseException(Token token) {
        this(token, "Допущена синтаксическая ошибка");
    }
}
