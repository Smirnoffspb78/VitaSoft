package com.smirnov.enums;

/**
 * Роли пользователей
 */
public enum UserRight {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_OPERATOR {
        @Override
        public String getTextRequest(String text) {
            StringBuilder operatorMessage = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                operatorMessage.append(currentChar);
                if (i != text.length() - 1) {
                    operatorMessage.append("-");
                }
            }
            return operatorMessage.toString();
        }
    };

    /**
     * Возвращает текст сообщения.
     * @param text Текст сообщения.
     * @return Текст сообщения
     */
    public String getTextRequest(String text) {
        return text;
    }
}
