package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.City;

import java.io.BufferedReader;
import java.io.FileReader;
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
 * Date: 19.04.2021.
 */
public class CityStore implements Store<City> {
    private final BasicDataSource pool = new BasicDataSource();

    private CityStore() {
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

        if (this.findAll().size() == 0) {
            this.save((new City(0, "Moscow")));
            this.save((new City(0, "Kazan")));
            this.save((new City(0, "Orel")));
        }
    }

    private static final class Lazy {
        private static final Store INST = new CityStore();
    }

    public static Store instOf() {
        return CityStore.Lazy.INST;
    }

    @Override
    public Collection<City> findAll() {
        List<City> candidates = new ArrayList<>();
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cites")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(City element) {
        if (element.getId() == 0) {
            create(element);
        } else {
            update(element);
        }

    }

    @Override
    public City findById(int id) {
        City result = null;
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cites WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result = new City(it.getInt("id"), it.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = this.pool.getConnection();
             PreparedStatement st = conn.prepareStatement("DELETE FROM cites WHERE id = ?;")) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void update(City can) {
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE  cytes SET name = ? WHERE id = ?")) {
            ps.setString(1, can.getName());
            ps.setInt(2, can.getId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private City create(City can) {
        try (Connection cn = this.pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO cites (name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, can.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    can.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return can;
    }
}
