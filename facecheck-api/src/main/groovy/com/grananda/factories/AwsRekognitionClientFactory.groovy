package com.grananda.factories

import io.micronaut.context.annotation.Factory
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.services.rekognition.RekognitionClient

import javax.inject.Singleton

@Factory
class AwsRekognitionClientFactory {

    @Singleton
    RekognitionClient rekognitionClient(Optional<String> profile) {

        return RekognitionClient.builder()
                .credentialsProvider(generateProfileCredentialsProvider(profile))
                .build();
    }

    private ProfileCredentialsProvider generateProfileCredentialsProvider(Optional<String> profile) {

        return ProfileCredentialsProvider.builder()
                .profileName(profile.orElse("default"))
                .build();
    }
}
