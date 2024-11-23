package ru.qngdjas.study.compiler.frontend.parser.exception;

import ru.qngdjas.study.compiler.frontend.Token;

/**
 * Класс исключений парсинга, сообщающий о пустом значении токена.
 */
public final class EmptyValueException extends SyntaxParseException {

    /**
     * Создает исключение парсинга со стандартным сообщением об ошибке.
     *
     * @param token Токен из-за которого произошла ошибка.
     */
    public EmptyValueException(Token token) {
        super(token, "Значение токена не может быть пустым.");
    }

}
