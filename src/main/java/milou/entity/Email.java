package milou.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    @Column(length = 255)
    private String subject;
    @Lob
    private String body;
    @Column(name = "sent_at")
    private LocalDateTime sentAt = LocalDateTime.now();

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Recipient> Recipients = new HashSet<>();



    @PrePersist
    protected void create() {
        sentAt = LocalDateTime.now();
    }
    public long getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Set<Recipient> getEmailRecipients() {
        return Recipients;
    }
    public void setRecipients(Set<Recipient> emailRecipients) {
        this.Recipients = Recipients;
    }


    @Column(name = "code", unique = true)
    private String code;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}