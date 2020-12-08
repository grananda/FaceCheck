package com.grananda.controllers

import javax.inject.Inject

import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.exchange.collection.CreateFaceMemoryCollectionRequest
import com.grananda.exchange.collection.DescribeFaceMemoryCollectionResponse
import com.grananda.exchange.collection.ListFaceMemoryCollectionResponse
import com.grananda.exchange.collection.UpdateFaceMemoryCollectionRequest
import com.grananda.services.FaceMemoryCollectionService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller('/collections')
@Secured(SecurityRule.IS_AUTHENTICATED)
class FaceMemoryCollectionController {

    @Inject
    FaceMemoryCollectionService collectionService

    @Get('/organization/{organizationId}')
    HttpResponse<ListFaceMemoryCollectionResponse> index(@PathVariable String organizationId) {
        List<FaceMemoryCollectionDto> collections = collectionService.list(organizationId)

        return HttpResponse.ok(ListFaceMemoryCollectionResponse.getInstance(collections))
    }

    @Get('/{id}')
    HttpResponse<DescribeFaceMemoryCollectionResponse> show(@PathVariable String id) {
        FaceMemoryCollectionDto collection = collectionService.describe(id)

        return HttpResponse.ok(DescribeFaceMemoryCollectionResponse.getInstance(collection))
    }

    @Post('/')
    HttpResponse<DescribeFaceMemoryCollectionResponse> save(@Body CreateFaceMemoryCollectionRequest request) {
        FaceMemoryCollectionDto collection = collectionService.registerFaceMemoryCollection(request.organizationId, request.collectionName)

        return HttpResponse.created(DescribeFaceMemoryCollectionResponse.getInstance(collection))
    }

    @Put('/{id}')
    HttpResponse<DescribeFaceMemoryCollectionResponse> update(@PathVariable String id, @Body UpdateFaceMemoryCollectionRequest request) {
        FaceMemoryCollectionDto collection = collectionService.update(id, request.collectionName)

        return HttpResponse.ok(DescribeFaceMemoryCollectionResponse.getInstance(collection))
    }

    @Delete('/{id}')
    HttpResponse delete(@PathVariable String id) {
        collectionService.removeFaceMemoryCollection(id)

        return HttpResponse.noContent()
    }
}
