package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 07.04.2021.
 */
public class UsqlStore implements Store<User> {
    private final BasicDataSource pool = new BasicDataSource();
    private static MessageDigest md5;


    private UsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static final class Lazy {
        private static final UsqlStore INST = new UsqlStore();
    }

    public static UsqlStore instOf() {
        return UsqlStore.Lazy.INST;
    }


    @Override
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection con = this.pool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void save(User element) {
        if (element.getId() != 0) {
            update(element);
        } else {
            create(element);
        }
    }

    @Override
    public User findById(int id) {
        User user = null;
        try (Connection con = this.pool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id = ?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = this.pool.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id = ?;")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public User findByEmailAndPassword(String email, String password) {
        User result = null;
        try (Connection con = this.pool.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?;")) {
            ps.setString(1, email);
            ps.setString(2, encrypt(password));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void update(User user) {
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?;")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, encrypt(user.getPassword()));
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User create(User user) {
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?);", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, encrypt(user.getPassword()));
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    private String encrypt(String element) {
        StringBuilder builder = new StringBuilder();
        byte[] bytes = this.md5.digest(element.getBytes());
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
}
