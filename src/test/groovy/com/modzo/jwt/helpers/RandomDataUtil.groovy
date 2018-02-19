package com.modzo.jwt.helpers

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class RandomDataUtil {
    static String randomEmail() {
        String rand = randomAlphanumeric(5)
        return "${rand}@${rand}.com"
    }

    static String randomClientId() {
        return randomAlphanumeric(5)
    }

    static String randomSecret() {
        return randomAlphanumeric(5)
    }

}
