package com.modzo.jwt.server.security.bruteforce

import com.modzo.jwt.helpers.RandomDataUtil
import spock.lang.Specification

class BruteForceIPsRegisterSpec extends Specification {

    def 'should count how many times address and email failed'() {
        given:
            String ipAddress = RandomDataUtil.randomIPAddress()
            String email = RandomDataUtil.randomEmail()
        and:
            BruteForceIPsRegister register = new BruteForceIPsRegister()
        when:
            register.failed(ipAddress, email)
        then:
            register.failedCount(ipAddress, email) == 1
    }

    def 'ip address and email should be blacklisted if failed less than 10 times'() {
        given:
            String ipAddress = RandomDataUtil.randomIPAddress()
            String email = RandomDataUtil.randomEmail()
        and:
            BruteForceIPsRegister register = new BruteForceIPsRegister()
        when:
            9.times { register.failed(ipAddress, email) }
        then:
            !register.isBlacklisted(ipAddress, email)
    }

    def 'ip address and email should be blacklisted if failed more than 10 times'() {
        given:
            String ipAddress = RandomDataUtil.randomIPAddress()
            String email = RandomDataUtil.randomEmail()
        and:
            BruteForceIPsRegister register = new BruteForceIPsRegister()
        when:
            10.times { register.failed(ipAddress, email) }
        then:
            register.isBlacklisted(ipAddress, email)
    }

    def 'ip address and email should be removed from blacklisted after successful'() {
        given:
            String ipAddress = RandomDataUtil.randomIPAddress()
            String email = RandomDataUtil.randomEmail()
        and:
            BruteForceIPsRegister register = new BruteForceIPsRegister()
        and:
            10.times { register.failed(ipAddress, email) }
        when:
            register.success(ipAddress, email)
        then:
            !register.isBlacklisted(ipAddress, email)
    }

    def 'failed ip address and email should expire'() {
        given:
            String ipAddress = RandomDataUtil.randomIPAddress()
            String email = RandomDataUtil.randomEmail()
        and:
            BruteForceIPsRegister register = new BruteForceIPsRegister(
                    new BruteForceProtectionConfig(cacheExpirationTimeInSeconds: 1)
            )
        and:
            10.times { register.failed(ipAddress, email) }
        when:
            sleep(1000L)
        then:
            !register.isBlacklisted(ipAddress, email)
    }
}
