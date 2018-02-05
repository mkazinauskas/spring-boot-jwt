package com.modzo.jwt.domain;

import com.modzo.jwt.domain.Token;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TokensRepository extends Repository<Token, Long> {
    Optional<Token> findByJti(String jti);

    List<Token> queryAllByUserIdAndBlackListedTrue(Long userId);

    void save(Token tokenBlackList);

    List<Token> deleteAllByUserIdAndExpiresBefore(Long userId, Long date);
}
