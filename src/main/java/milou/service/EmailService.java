package milou.service;

import milou.entity.Email;
import milou.entity.Recipient;
import milou.entity.User;
import milou.util.HibernateUtil;
import milou.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmailService {
    private final AuthService authService;

    public EmailService(AuthService authService) {
        this.authService = authService;
    }

    public void sendEmail(String subject, String body, List<String> toList) {
        User sender = authService.getCurrentUser();
        if (sender == null) {
            System.out.println("Please login first.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Email email = new Email();
            email.setSender(sender);
            email.setSubject(subject);
            email.setBody(body);
            email.setCode(Util.generateCode());

            Set<Recipient> recipients = new HashSet<>();
            for (String toEmail : toList) {
                String normalized = Util.emailInput(toEmail);
                User recipientUser = session.createQuery("from User where email = :email", User.class)
                        .setParameter("email", normalized)
                        .uniqueResult();

                if (recipientUser == null) {
                    System.out.println("Recipient not found: " + normalized);
                    continue;
                }

                Recipient recipient = new Recipient();
                recipient.setEmail(email);
                recipient.setRecipient(recipientUser);
                recipient.setSubject(subject);
                recipient.setBody(body);
                recipients.add(recipient);
            }

            email.setRecipients(recipients);
            session.persist(email);

            tx.commit();
            System.out.println("Email sent successfully!");
        }
    }

    public void viewInbox() {
        User user = authService.getCurrentUser();
        if (user == null) {
            System.out.println("Please login first.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Recipient> inbox = session.createQuery(
                            "from Recipient r where r.user = :user order by r.id desc", Recipient.class)
                    .setParameter("user", user)
                    .list();

            if (inbox.isEmpty()) {
                System.out.println("Your inbox is empty.");
                return;
            }

            System.out.println("Inbox of " + user.getEmail() + ":");
            for (Recipient r : inbox) {
                Email email = r.getEmail();
                System.out.println("[" + email.getId() + "] " + email.getSubject() +
                        " (from: " + email.getSender().getEmail() + ")" +
                        (r.isRead() ? " [Read]" : " [Unread]"));
            }
        } catch (Exception e) {
            System.out.println("Error while fetching inbox: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void replyEmail(int recipientId, String body) {
        User user = authService.getCurrentUser();
        if (user == null) {
            System.out.println("Please login first.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Recipient original = session.get(Recipient.class, recipientId);
            if (original == null) {
                System.out.println("Email not found.");
                return;
            }

            Email reply = new Email();
            reply.setSender(user);
            reply.setSubject("Re: " + original.getSubject());
            reply.setBody(body);
            reply.setCode(Util.generateCode());

            Recipient replyRecipient = new Recipient();
            replyRecipient.setEmail(reply);
            replyRecipient.setRecipient(original.getEmail().getSender());
            replyRecipient.setSubject(reply.getSubject());
            replyRecipient.setBody(body);

            reply.getEmailRecipients().add(replyRecipient);

            session.persist(reply);
            tx.commit();

            System.out.println("Reply sent successfully!");
        }
    }

    public void forwardEmail(int recipientId, String forwardTo) {
        User user = authService.getCurrentUser();
        if (user == null) {
            System.out.println("Please login first.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Recipient original = session.get(Recipient.class, recipientId);
            if (original == null) {
                System.out.println("Email not found.");
                return;
            }

            String normalized = Util.emailInput(forwardTo);
            User recipientUser = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", normalized)
                    .uniqueResult();

            if (recipientUser == null) {
                System.out.println("Recipient not found.");
                return;
            }

            Email forward = new Email();
            forward.setSender(user);
            forward.setSubject("Fwd: " + original.getSubject());
            forward.setBody(original.getBody());
            forward.setCode(Util.generateCode());

            Recipient forwardRecipient = new Recipient();
            forwardRecipient.setEmail(forward);
            forwardRecipient.setRecipient(recipientUser);
            forwardRecipient.setSubject(forward.getSubject());
            forwardRecipient.setBody(forward.getBody());

            forward.getEmailRecipients().add(forwardRecipient);

            session.persist(forward);
            tx.commit();

            System.out.println("Email forwarded successfully!");
        }
    }
}
