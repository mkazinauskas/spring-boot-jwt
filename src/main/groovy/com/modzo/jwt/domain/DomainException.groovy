package com.modzo.jwt.domain

class DomainException extends RuntimeException {
    final String code

    DomainException(String code, String message) {
        super(message)
        this.code = code
    }

    static DomainException passwordsDoNotMatch() {
        return new DomainException('PASSWORDS_DO_NOT_MATCH', 'Passwords do not match')
    }

    static DomainException clientByUniqueIdWasNotFound(String uniqueId) {
        return new DomainException('CLIENT_BY_UNIQUE_ID_WAS_NOT_FOUND', "Client by unique id `${uniqueId}` was not found")
    }

    static DomainException clientByClientIdWasNotFound(String clientId) {
        return new DomainException('CLIENT_BY_CLIENT_ID_WAS_NOT_FOUND', "Client by client id `${clientId}` was not found")
    }


    static DomainException userByUniqueIdWasNotFound(String uniqueId) {
        return new DomainException('USER_BY_UNIQUE_ID_WAS_NOT_FOUND', "User by unique id `${uniqueId}` was not found")
    }

    static DomainException userByEmailWasNotFound(String email) {
        return new DomainException('USER_BY_EMAIL_WAS_NOT_FOUND', "User by email `${email}` was not found")
    }
}
