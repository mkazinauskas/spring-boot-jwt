package com.modzo.jwt.domain.clients.exceptions

class ClientNotFoundException extends RuntimeException {

    private ClientNotFoundException(String message) {
        super(message)
    }

    static ClientNotFoundException byClientId(String clienId) {
        new ClientNotFoundException("Client with name `$clienId` was not found")
    }

    static ClientNotFoundException byUniqueId(String uniqueId) {
        new ClientNotFoundException("Client with uniqueId `$uniqueId` was not found")
    }
}
