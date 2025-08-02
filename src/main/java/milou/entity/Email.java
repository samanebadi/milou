package milou.entity;

import java.time.LocalDateTime;

    public class Email {
        private int id;
        private String subject;
        private String body;
        private String code;
        private User sender;
        private LocalDateTime creationDate;
        private EmailStatus status;

        public Email(int id, String subject, String body, String code, User sender, LocalDateTime creationDate, EmailStatus status) {
            this.id = id;
            this.subject = subject;
            this.body = body;
            this.code = code;
            this.sender = sender;
            this.creationDate = creationDate;
            this.status = status;
        }


        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getSubject() {
            return subject;
        }
        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }
        public void setBody(String body) {
            this.body = body;
        }

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }

        public User getSender() {
            return sender;
        }
        public void setSender(User sender) {
            this.sender = sender;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }
        public void setCreationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
        }

        public EmailStatus getStatus() {
            return status;
        }
        public void setStatus(EmailStatus status) {
            this.status = status;
        }
    }

