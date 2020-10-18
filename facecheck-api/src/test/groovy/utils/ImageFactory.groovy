package utils

import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.model.Image

class ImageFactory {

    static Image create(String sourceImagePath) throws IOException {
        String path = "src/test/resources/"

        File file = new File(path + sourceImagePath)
        InputStream sourceStream = new FileInputStream(file)
        SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream)

        return Image.builder()
                .bytes(sourceBytes)
                .build()
    }
}
