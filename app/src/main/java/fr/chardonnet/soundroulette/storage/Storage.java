package fr.chardonnet.soundroulette.storage;

import java.util.List;

public interface Storage<T> {
    T find(int id);

    List<T> findAll();

    int size();

    boolean insert(T t);

    boolean update(int id, T t);

    boolean delete(int id);
}
