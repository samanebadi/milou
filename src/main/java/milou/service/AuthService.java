package milou.service;

import milou.entity.User;
import milou.util.HibernateUtil;
import milou.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AuthService {
    private User currentUser;

    public boolean signup(String name, String email, String password) {
        email = Util.emailInput(email);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            User existing = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (existing != null) {
                System.out.println("Email already exists!");
                tx.rollback();
                return false;
            }

            User user = new User(name, email, password);
            session.persist(user);

            tx.commit();
            System.out.println("User registered successfully!");
            return true;
        }
    }

    public boolean login(String email, String password) {
        email = Util.emailInput(email);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("from User where email = :email and password = :password", User.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null) {
                currentUser = user;
                System.out.println("Login successful! Welcome " + user.getName());
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return false;
            }
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
