package com.modzo.jwt.email;

public interface EmailService {
    void sendMessage(Message message);

    class Message {
        private final String to;
        private final String subject;
        private final String text;

        public Message(String to, String subject, String text) {
            this.to = to;
            this.subject = subject;
            this.text = text;
        }

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getText() {
            return text;
        }
    }
}
