package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 18.03.2021.
 */
public class MemStore {
    private static final MemStore INST = new MemStore();
    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();


    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Description for junior", LocalDate.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Description for middle", LocalDate.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Description for senior", LocalDate.now()));
        candidates.put(1, new Candidate(1, "Junior Java", 1));
        candidates.put(2, new Candidate(2, "Middle Java", 1));
        candidates.put(3, new Candidate(3, "Senior Java", 1));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(POST_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }
}
