package com.grananda.services

import javax.inject.Singleton

import groovy.transform.CompileStatic
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.model.Image

@CompileStatic
@Singleton
class FileSystemServiceImpl implements FileSystemService {
    @Override
    Image processUploadedMediaForAws(InputStream file) {
        SdkBytes sourceBytes = SdkBytes.fromInputStream(file)

        return Image
                .builder()
                .bytes(sourceBytes)
                .build()
    }
}
