package ru.qngdjas.study.compiler.frontend;

import java.util.HashSet;
import java.util.StringJoiner;

/**
 * Класс токенов.
 */
public class Token {

    /**
     * Типы токенов.
     */
    public enum Type {
        /**
         * Перечень лексем, распознаваемых как ключевые слова.<p>
         * В данном примере, массивы и строки, заключенные в двойные кавычки считаем запретными конструкциями.
         */
        BANNED {
            private static final HashSet<String> BANS = new HashSet<>();

            static {
                // Строки в кавычках
                BANS.add("\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"");
                // Массивы
                BANS.add("[A-Za-z]\\w*\\[\\d+\\]");
            }

            @Override
            public String toRegex() {
                return String.join("|", BANS);
            }
        },
        /**
         * Перечень лексем, распознаваемых как ключевые слова.
         */
        KEYWORD {
            private static final HashSet<String> KEYWORDS = new HashSet<>();

            static {
                KEYWORDS.add("begin");
                KEYWORDS.add("do");
                KEYWORDS.add("else");
                KEYWORDS.add("end");
                KEYWORDS.add("if");
                KEYWORDS.add("program");
                KEYWORDS.add("then");
                KEYWORDS.add("var");
                KEYWORDS.add("while");
                KEYWORDS.add("writeln");
            }

            @Override
            public String toRegex() {
                return String.join("|", KEYWORDS);
            }
        },
        /**
         * Перечень лексем, распознаваемых как типы данных.
         */
        TYPE {
            private static final HashSet<String> TYPES = new HashSet<>();

            static {
                TYPES.add("boolean");
                TYPES.add("integer");
                TYPES.add("string");
            }

            @Override
            public String toRegex() {
                return String.join("|", TYPES);
            }
        },
        /**
         * Лексемы, распознаваемые как имена переменных, функций, программ: 'a_1', 'a', 'a1'.
         */
        ID {
            @Override
            public String toRegex() {
                return "[A-Za-z]\\w*";
            }
        },
        /**
         * Перечень лексем, распознаваемых как константы (числовые и строковые): 'abc', 123.
         */
        LITERAL {
            private static final HashSet<String> LITERALS = new HashSet<>();

            static {
                LITERALS.add("'[^'\\\\]*(?:\\\\.[^'\\\\]*)*'");
                LITERALS.add("true");
                LITERALS.add("false");
                LITERALS.add("-?\\d+");
            }

            @Override
            public String toRegex() {
                return String.join("|", LITERALS);
            }
        },
        /**
         * Перечень лексем, распознаваемых как операторы.
         */
        OPERATOR {
            private static final HashSet<String> OPERATORS = new HashSet<>();

            static {
                OPERATORS.add("\\*");
                OPERATORS.add("\\+");
                OPERATORS.add("-");
                OPERATORS.add("\\/");
                OPERATORS.add(":=");
                OPERATORS.add("<");
                OPERATORS.add("<=");
                OPERATORS.add("=");
                OPERATORS.add(">");
                OPERATORS.add(">=");
            }

            @Override
            public String toRegex() {
                return String.join("|", OPERATORS);
            }
        },
        /**
         * Перечень лексем, распознаваемых как разделители.
         */
        SEPARATOR {
            private static final HashSet<String> SEPARATORS = new HashSet<>();

            static {
                SEPARATORS.add("\\(");
                SEPARATORS.add("\\)");
                SEPARATORS.add(",");
                SEPARATORS.add("\\.");
                SEPARATORS.add(":");
                SEPARATORS.add(";");
            }

            @Override
            public String toRegex() {
                return String.format("[%s]", String.join("", SEPARATORS));
            }
        },
        /**
         * Лексемы, распознаваемых как пробел, знак табуляции, переноса каретки.
         */
        SPACE {
            @Override
            public String toRegex() {
                return "\\s";
            }
        },
        /**
         * Неопознанные лексемы считаем неизвестными.
         */
        UNKNOWN {
            @Override
            public String toRegex() {
                return ".";
            }
        };

        /**
         * Токены должны предоставлять регулярное выражение в соответствии со своим типом.
         *
         * @return Регулярное выражение.
         */
        protected abstract String toRegex();

        /**
         * Регулярные выражения типов токенов собираются в одно.
         *
         * @return Общее регулярное выражение.
         */
        public static String getRegex() {
            StringJoiner regex = new StringJoiner("|");
            for (Type type : values()) {
                regex.add(String.format("(?<%s>%s)", type, type.toRegex()));
            }
            return regex.toString();
        }
    }

    private final Type type;
    private String value;
    private int row;
    private int column;

    /**
     * Создание простого токена, учитывающего лишь {@link Type}.
     *
     * @param type Тип токена.
     */
    public Token(Type type) {
        this.type = type;
    }


    /**
     * Создание токена, учитывающего {@link Type} и значение.
     *
     * @param type  Тип токена.
     * @param value Значение.
     */
    public Token(Type type, String value) {
        this(type);
        this.value = value;
    }

    public Token(Type type, String value, int row, int column) {
        this(type, value);
        this.row = row;
        this.column = column;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    /**
     * Сравнение токенов выполняется по типу.<p>
     * Если у текущего токена {@code value != null}, то при операции сравнения учитывается его значение.
     *
     * @param obj Токен с которым сравнивается {@code this}.
     * @return {@code true}, если токены равны.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (super.equals(obj)) {
            return true;
        }
        return (obj instanceof Token aToken)
                && this.type == aToken.type
                && (this.value == null || this.value.equals(aToken.value));
    }

    /**
     * Строковое представление {@link Token}.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return String.format("%s(%s): [%d, %d]", type, value, row, column);
    }
}
