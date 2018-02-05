package com.modzo.jwt.domain.exceptions

class UserNotFoundException extends RuntimeException {

    private UserNotFoundException(String message) {
        super(message)
    }

    static UserNotFoundException byEmail(String email) {
        new UserNotFoundException("User with email `$email` was not found")
    }

    static UserNotFoundException byUniqueId(String uniqueId) {
        new UserNotFoundException("User with email `$uniqueId` was not found")
    }
}
