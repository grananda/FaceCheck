package com.grananda.services


import javax.inject.Inject

import com.grananda.Application
import io.micronaut.core.io.ResourceResolver
import io.micronaut.core.io.scan.ClassPathResourceLoader
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.Image
import spock.lang.*

@MicronautTest(application = Application.class, startApplication = false)
class FileSystemServiceImplTest extends Specification {

    @Inject
    FileSystemService fileSystemService

    def "an uploaded file is process for AWS use"() {
        given: 'an uploaded file'
        ClassPathResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get()
        Optional<URL> resource = loader.getResource("classpath:assets/image1a.jpg");
        File file = new File(resource.get().file)
        InputStream sourceStream = new FileInputStream(file)

        when: 'the file is processed'
        Image response = fileSystemService.processUploadedMediaForAws(sourceStream)

        then: 'the expected image is received'
        response.toString()
    }
}
