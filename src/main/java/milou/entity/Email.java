package milou.entity;

import java.sql.Timestamp;

public class Email {
    private int id;
    private String subject;
    private String body;
    private String code;
    private int senderId;
    private Timestamp creationDate;
    private String status;
    private Integer parentEmailId; // nullable

    public Email() {}

    public Email(int id, String subject, String body, String code, int senderId, Timestamp creationDate, String status, Integer parentEmailId) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.code = code;
        this.senderId = senderId;
        this.creationDate = creationDate;
        this.status = status;
        this.parentEmailId = parentEmailId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public Timestamp getCreationDate() { return creationDate; }
    public void setCreationDate(Timestamp creationDate) { this.creationDate = creationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getParentEmailId() { return parentEmailId; }
    public void setParentEmailId(Integer parentEmailId) { this.parentEmailId = parentEmailId; }
}
