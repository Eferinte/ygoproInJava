package com.ocg.Server;

import com.google.gson.Gson;


public class MessageFormatter {
    public enum MessageType {

        AUTHORIZE("AUTHORIZE"),
        MSG("MSG"),
        GAME_MSG("GAME_MSG"),
        DECLARE("DECLARE"),
        STATE("STATE"),
        JOIN("JOIN"),
        SELECT_CARD("SELECT_CARD"),
        SELECT_OPERATE("SELECT_OPERATE"),
        OPERATE("OPERATE");


        private String value;

        MessageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class Message<T> {
        T data;
        String target;
        MessageType type;
        String zone;

        public Message(T data, String target, MessageType type, String zone) {
            this.data = data;
            this.target = target;
            this.type = type;
            this.zone = zone;
        }
    }

    public static Gson JSON = new Gson();

    public static <T> String format(T data, MessageType type, String target, String zone) {
        return JSON.toJson(new Message<T>(data, target, type, zone));
    }

    public static <T> String format(T data, MessageType type, String target) {
        return JSON.toJson(new Message<T>(data, target, type, null));
    }

    public static <T> String format(T data, MessageType type) {
        return JSON.toJson(new Message<T>(data, null, type, null));
    }
}
