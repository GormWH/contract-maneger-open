package com.contractmanager6.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDataUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    void deleteByLoginId(String loginId);

}
