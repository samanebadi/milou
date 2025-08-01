package milou.entity;

public class Recipient {
    private Email email;
    private User recipient;
    private boolean isRead;

    public Recipient(Email email, User recipient, boolean isRead) {
        this.email = email;
        this.recipient = recipient;
        this.isRead = isRead;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
