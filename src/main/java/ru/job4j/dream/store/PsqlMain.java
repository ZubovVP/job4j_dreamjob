package ru.job4j.dream.store;


import ru.job4j.dream.model.Post;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 27.03.2021.
 */
public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", "Description", LocalDate.now()));
        for (Post post :  (List<Post>) store.findAll()) {
            System.out.println(post.getId() + " " + post.getName());
        }
    }
}
