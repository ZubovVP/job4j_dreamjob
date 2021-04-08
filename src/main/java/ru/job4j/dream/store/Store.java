package ru.job4j.dream.store;

import java.util.Collection;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 07.04.2021.
 */
public interface Store<E> {
    Collection<E> findAll();

    void save(E element);

    E findById (int id);

    boolean delete (int id);
}
