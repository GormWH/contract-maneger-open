package com.contractmanager6.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByLoginId(String loginId);
    List<User> findAll();
    void deleteByLoginId(String loginId);

    void deleteById(Long id);
}
