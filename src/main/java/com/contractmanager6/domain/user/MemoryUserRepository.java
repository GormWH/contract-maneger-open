package com.contractmanager6.domain.user;

import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class MemoryUserRepository implements UserRepository{

    private static final Map<Long, User> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++sequence);
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return findAll().stream().filter(user -> user.getLoginId().equals(loginId)).findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteByLoginId(String loginId) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
