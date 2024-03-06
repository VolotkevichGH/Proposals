package com.example.testjwt.repositories;

import com.example.testjwt.entity.RefreshToken;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableRedisRepositories
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken (String token);
    void deleteByUserId(Long userId);


    Optional<RefreshToken> findByUserId (Long userId);
}
