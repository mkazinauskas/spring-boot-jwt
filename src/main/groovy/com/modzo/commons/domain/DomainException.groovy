package com.modzo.commons.domain

class DomainException extends RuntimeException {
    final String code

    private DomainException(String code, String message) {
        super(message)
        this.code = code
    }

    static DomainException passwordsDoNotMatch() {
        new DomainException('PASSWORDS_DO_NOT_MATCH', 'Passwords do not match')
    }

    static DomainException clientByUniqueIdWasNotFound(String uniqueId) {
        new DomainException('CLIENT_BY_UNIQUE_ID_WAS_NOT_FOUND',
                "Client by unique id `${uniqueId}` was not found")
    }

    static DomainException clientByClientIdWasNotFound(String clientId) {
        new DomainException('CLIENT_BY_CLIENT_ID_WAS_NOT_FOUND',
                "Client by client id `${clientId}` was not found")
    }

    static DomainException userByUniqueIdWasNotFound(String uniqueId) {
        new DomainException('USER_BY_UNIQUE_ID_WAS_NOT_FOUND',
                "User by unique id `${uniqueId}` was not found")
    }

    static DomainException userByEmailWasNotFound(String email) {
        new DomainException('USER_BY_EMAIL_WAS_NOT_FOUND',
                "User by email `${email}` was not found")
    }

    static DomainException userActivationCodeIsIncorrect(String activationCode) {
        new DomainException('USER_ACTIVATION_CODE_IS_INCORRECT',
                "User activation code is incorrect for uniqueId `${activationCode}`")
    }

    static DomainException userWithActivationCodeIsAlreadyActivated(String activationCode) {
        new DomainException('USER_WITH_ACTIVATION_CODE_IS_ACTIVATED',
                "User with activation code `${activationCode}` is already activated")
    }
}
