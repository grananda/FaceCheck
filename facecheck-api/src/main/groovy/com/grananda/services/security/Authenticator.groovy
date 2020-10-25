package com.grananda.services.security

import com.grananda.domain.User
import com.grananda.repositories.UserRepository
import edu.umd.cs.findbugs.annotations.Nullable
import groovy.transform.CompileStatic
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.Flowable
import org.jasypt.util.password.StrongPasswordEncryptor
import org.reactivestreams.Publisher

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@CompileStatic
class Authenticator implements AuthenticationProvider {

    @Inject
    private UserRepository userRepository;

    @Inject
    private StrongPasswordEncryptor strongPasswordEncryptor

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        final AuthenticationResponse result = performAuthentication(authenticationRequest.identity.toString(), authenticationRequest.secret.toString())

        return Flowable.just(result) as Publisher<AuthenticationResponse>
    }

    private AuthenticationResponse performAuthentication(String identity, String secret) {
        final User user = userRepository.findByUsernameOrEmail(identity, identity)

        if (!user) {
            return new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)
        }

        if (strongPasswordEncryptor.checkPassword(secret, user.password)) {
            return new UserDetails(identity, []);
        }

        return new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
    }
}
