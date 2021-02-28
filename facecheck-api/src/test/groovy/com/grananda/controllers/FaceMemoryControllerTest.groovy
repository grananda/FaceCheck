package com.grananda.controllers

import javax.inject.Inject

import com.grananda.Application
import com.grananda.domain.FaceMemory
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.exchange.face.RegisterFaceMemoryResponse
import com.grananda.repositories.FaceMemoryCollectionRepository
import com.grananda.repositories.OrganizationRepository
import com.grananda.services.FaceMemoryService
import com.grananda.services.FaceMemoryServiceImpl
import com.grananda.utils.ControllerAuth
import io.micronaut.core.io.ResourceResolver
import io.micronaut.core.io.scan.ClassPathResourceLoader
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.multipart.MultipartBody
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import utils.FaceMemoryCollectionFactory
import utils.FaceMemoryFactory
import utils.OrganizationFactory

@MicronautTest(application = Application.class, transactional = false)
class FaceMemoryControllerTest extends GlobalControllerTest {

    final static private String FACE_API_PREFIX = '/faces'

    @Inject
    OrganizationRepository organizationRepository

    @Inject
    FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    @Inject
    FaceMemoryService faceMemoryService

    @MockBean(FaceMemoryServiceImpl)
    FaceMemoryService mockFaceMemoryService() {
        Mock(FaceMemoryService)
    }

    def 'a new face is registered'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()

        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        and: 'a FaceMemory response'
        FaceMemory faceMemory = FaceMemoryFactory.create()

        when: 'an face is registered'
        ClassPathResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get()
        Optional<URL> resource = loader.getResource("classpath:assets/image1a.jpg");
        File file = new File(resource.get().file)

        MultipartBody multipartBody = MultipartBody
                .builder()
                .addPart('file', file)
                .addPart('collectionId', collection.id)
                .build()

        HttpRequest request = ControllerAuth.login(
                HttpRequest.POST("${FACE_API_PREFIX}/register", multipartBody).contentType(MediaType.MULTIPART_FORM_DATA),
                user.username,
                password,
                client
        )
        HttpResponse<RegisterFaceMemoryResponse> response = client.toBlocking().exchange(request, RegisterFaceMemoryResponse.class)

        then: 'the following mock interactions occur'
        1 * faceMemoryService.storeFacialMemory(_, _, _) >> faceMemory

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK
    }

    def 'an existing face is unregistered'() {

    }

    def 'a face is identified'() {

    }

}
