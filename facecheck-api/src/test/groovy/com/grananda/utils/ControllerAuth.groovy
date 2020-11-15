package com.grananda.utils

import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.AccessRefreshToken

final class ControllerAuth {
    static HttpRequest login(MutableHttpRequest request, String username, String password, HttpClient client) {
        HttpRequest loginRequest = HttpRequest.create(HttpMethod.POST, '/login')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(new UsernamePasswordCredentials(username, password))

        HttpResponse<AccessRefreshToken> loginResponse = client.toBlocking().exchange(loginRequest, AccessRefreshToken)

        String token = loginResponse.body.get().accessToken

        request.bearerAuth(token)
    }
}
