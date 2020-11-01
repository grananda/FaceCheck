package com.grananda.controllers

import com.devskiller.friendly_id.FriendlyId
import com.grananda.exceptions.FaceCheckException
import com.grananda.exchange.ErrorResponse
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponseFactory
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.rules.SecurityRule

@Slf4j
@CompileStatic
@Controller("/error")
@Secured(SecurityRule.DENY_ALL)
class GlobalErrorController {

    @Value('${facecheck.debug.exception-message:false}')
    private boolean debug

    @Error(global = true)
    HttpResponse handleException(HttpRequest request, Throwable exception) {
        if (exception instanceof AuthenticationException)
            return HttpResponse.unauthorized()
        if (exception instanceof AuthorizationException)
            return exception.forbidden ? HttpResponseFactory.INSTANCE.status(HttpStatus.FORBIDDEN) : HttpResponse.unauthorized()

        handle(exception, debug)
    }

    static protected HttpResponse<ErrorResponse> handle(Throwable throwable, boolean debug) {
        String msg = throwable.message

        if (throwable instanceof FaceCheckException && msg) {
            log.warn msg
        } else {
            if (debug && !msg)
                msg = throwable.cause?.message
            if (!debug || !msg)
                msg = "Unable to process request"

            msg += " - Error ID: ${FriendlyId.toFriendlyId(UUID.randomUUID())}"

            log.error(msg, throwable)
        }

        final resp = new ErrorResponse(msg)

        return HttpResponse.badRequest(resp)
    }
}
