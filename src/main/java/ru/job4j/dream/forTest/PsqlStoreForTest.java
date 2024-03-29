package ru.job4j.dream.forTest;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.Store;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 12.04.2021.
 */
public class PsqlStoreForTest implements Store<Post> {
    private final List<Post> posts = new ArrayList<>();

    @Override
    public Collection<Post> findAll() {
        return this.posts;
    }

    @Override
    public void save(Post element) {
        this.posts.add(element);

    }

    @Override
    public Post findById(int id) {
        return posts.stream().filter(x -> x.getId() == id).collect(Collectors.toList()).get(0);
    }

    @Override
    public boolean delete(int id) {
        this.posts.removeIf(post -> post.getId() == id);
        return true;
    }
}
