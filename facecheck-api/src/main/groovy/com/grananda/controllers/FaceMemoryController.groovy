package com.grananda.controllers

import javax.inject.Inject

import com.grananda.domain.FaceMemory
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.User
import com.grananda.exchange.face.RegisterFaceMemoryRequest
import com.grananda.exchange.face.RegisterFaceMemoryResponse
import com.grananda.services.FaceMemoryCollectionService
import com.grananda.services.FaceMemoryService
import com.grananda.services.FileSystemService
import com.grananda.services.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import software.amazon.awssdk.services.rekognition.model.Image

@Controller('/faces')
@Secured(SecurityRule.IS_AUTHENTICATED)
class FaceMemoryController {

    @Inject
    FaceMemoryCollectionService collectionService

    @Inject
    FaceMemoryService faceMemoryService

    @Inject
    UserService userService

    @Inject
    FileSystemService fileSystemService

    @Post(value = '/register', consumes = MediaType.MULTIPART_FORM_DATA)
    HttpResponse<RegisterFaceMemoryResponse> registerFace(@Body RegisterFaceMemoryRequest request, CompletedFileUpload file, Authentication auth) {
        final User user = userService.describe(auth.name)

        FaceMemoryCollection collection = collectionService.describe(request.collectionId)

        Image image = fileSystemService.processUploadedMediaForAws(file.inputStream)

        FaceMemory face = faceMemoryService.storeFacialMemory(user, collection, image)

        return HttpResponse.ok(RegisterFaceMemoryResponse.getInstance(face))
    }

    @Delete('/{faceId}')
    HttpResponse unregisterFace(String faceId) {

    }

    @Post(consumes = MediaType.MULTIPART_FORM_DATA)
    HttpResponse identifyFace(@Body IdentifyFaceMemoryRequest, CompletedFileUpload file) {

    }
}
