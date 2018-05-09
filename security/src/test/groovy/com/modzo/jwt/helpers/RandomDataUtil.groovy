package com.modzo.jwt.helpers

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric

class RandomDataUtil {
    static String randomEmail() {
        String rand = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(5)
        return "${rand}@${rand}.com"
    }

    static String randomIPAddress() {
        String rand = org.apache.commons.lang3.RandomStringUtils.randomNumeric(3)
        return "${rand}.${rand}.${rand}.${rand}"
    }

    static String randomClientId() {
        return org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(5)
    }

    static String randomSecret() {
        return org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(5)
    }

}
