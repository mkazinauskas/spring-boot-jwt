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
}
