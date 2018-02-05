package com.modzo.jwt.helpers

import org.apache.commons.lang3.RandomStringUtils

class RandomDataUtil {
    static String randomEmail(){
        String rand = RandomStringUtils.randomAlphanumeric(5)
        return "${rand}@${rand}.com"
    }
}
