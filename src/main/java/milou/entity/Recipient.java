package milou.entity;

public class Recipient {
    private int id;
    private int emailId;
    private int recipientId;
    private boolean isRead;

    public Recipient() {}

    public Recipient(int id, int emailId, int recipientId, boolean isRead) {
        this.id = id;
        this.emailId = emailId;
        this.recipientId = recipientId;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmailId() { return emailId; }
    public void setEmailId(int emailId) { this.emailId = emailId; }

    public int getRecipientId() { return recipientId; }
    public void setRecipientId(int recipientId) { this.recipientId = recipientId; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
