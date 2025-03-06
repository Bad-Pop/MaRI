package io.github.badpop.mari.application;

import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
@TestSecurity(user = "test@mari.fr")
@JwtSecurity(
        claims = {
                @Claim(key = "sub", value = "mari-oidc|1234567890"),
                @Claim(key = "name", value = "test@mari.fr"),
                @Claim(key = "nickname", value = "test"),
                @Claim(key = "email", value = "test@mari.fr"),
                @Claim(key = "exp", value = "5685073033"),
                @Claim(key = "iat", value = "1708858633"),
        })
public @interface WithAuthenticatedUser {
}
