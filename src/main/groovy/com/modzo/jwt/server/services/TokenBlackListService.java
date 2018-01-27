package com.modzo.jwt.server.services;

import com.modzo.jwt.server.domain.Token;
import com.modzo.jwt.server.domain.TokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TokenBlackListService {

    @Autowired
    TokensRepository tokenBlackListRepo;

    public Boolean isBlackListed( String jti ) throws TokenNotFoundException {
        Optional<Token> token = tokenBlackListRepo.findByJti(jti);
        if ( token.isPresent() ) {
            return token.get().isBlackListed();
        } else {
            throw new TokenNotFoundException(jti);
        }
    }

    @Async
    public void addToEnabledList(Long userId, String jti, Long expired ) {
        // clean all black listed tokens for user
        List<Token> list = tokenBlackListRepo.queryAllByUserIdAndBlackListedTrue(userId);
        if (list != null && list.size() > 0) {
            list.forEach(
                    token -> {
                        token.setBlackListed(true);
                        tokenBlackListRepo.save(token);
                    }
            );
        }
        // Add new token white listed
        Token tokenBlackList = new Token(userId, jti, expired);
        tokenBlackList.setBlackListed(false);
        tokenBlackListRepo.save(tokenBlackList);
        tokenBlackListRepo.deleteAllByUserIdAndExpiresBefore(userId, new Date().getTime());
    }

    @Async
    public void addToBlackList(String jti ) throws TokenNotFoundException {
        Optional<Token> tokenBlackList = tokenBlackListRepo.findByJti(jti);
        if ( tokenBlackList.isPresent() ) {
            tokenBlackList.get().setBlackListed(true);
            tokenBlackListRepo.save(tokenBlackList.get());
        } else throw new TokenNotFoundException(jti);
    }

    public static class TokenNotFoundException extends Exception {
        public String jti;
        public String message;
        public TokenNotFoundException(String jti) {
            super();
            this.jti = jti;
            message = String.format("Token with jti[%s] not found.",jti);
        }
    }
}
