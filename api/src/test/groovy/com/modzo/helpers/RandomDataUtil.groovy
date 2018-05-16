package com.modzo.helpers

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric

class RandomDataUtil {
    static String randomEmail() {
        String rand = randomAlphanumeric(5)
        return "${rand}@${rand}.com"
    }

    static String randomIPAddress() {
        String rand = randomNumeric(3)
        return "${rand}.${rand}.${rand}.${rand}"
    }

    static String randomClientId() {
        return randomAlphanumeric(5)
    }

    static String randomSecret() {
        return randomAlphanumeric(5)
    }

}
