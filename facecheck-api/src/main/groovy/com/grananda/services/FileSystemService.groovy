package com.grananda.services

import javax.inject.Singleton

import groovy.transform.CompileStatic
import software.amazon.awssdk.services.rekognition.model.Image

@CompileStatic
@Singleton
interface FileSystemService {
    Image processUploadedMediaForAws(InputStream file)
}
