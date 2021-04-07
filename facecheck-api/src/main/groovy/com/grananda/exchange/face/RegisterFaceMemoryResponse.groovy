package com.grananda.exchange.face

import com.grananda.domain.FaceMemory
import com.grananda.dto.FaceMemoryDto
import com.grananda.dto.FaceMemoryMapper

class RegisterFaceMemoryResponse {

    FaceMemoryDto faceMemory

    RegisterFaceMemoryResponse() {
    }

    RegisterFaceMemoryResponse(FaceMemory faceMemory) {
        this.faceMemory = FaceMemoryMapper.map(faceMemory)
    }

    static getInstance(FaceMemory faceMemory) {
        return new RegisterFaceMemoryResponse(faceMemory)
    }
}
