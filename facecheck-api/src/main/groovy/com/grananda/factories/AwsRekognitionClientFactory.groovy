package com.grananda.factories

import javax.inject.Singleton

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.rekognition.RekognitionClient

@Factory
class AwsRekognitionClientFactory {

    @Value('${aws.aws_access_key_id}')
    String accessKey

    @Value('${aws.aws_secret_access_key}')
    String secret

    @Value('${aws.aws_region}')
    String region

    @Singleton
    RekognitionClient rekognitionClient() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secret)
        Region region = Region.of(region)

        return RekognitionClient
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(region)
                .build();
    }
}
