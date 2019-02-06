package upload.poalim.OCRUtil;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.TextDetection;

import java.util.List;

public class AmazonRekognitionUtil {

    //use AmazonRekognition
    public static String getText(String photo, String bucketName) throws Exception {

        try {
            com.amazonaws.services.rekognition.AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1).build();


            DetectTextRequest request = new DetectTextRequest()
                    .withImage(new Image()
                            .withS3Object(new S3Object()
                                    .withName(photo)
                                    .withBucket(bucketName)));

            DetectTextResult result = rekognitionClient.detectText(request);
            List<TextDetection> textDetections = result.getTextDetections();

            for (TextDetection text : textDetections) {

                return text.getDetectedText();

            }
        } catch (Exception e) {

            throw e;
        }
        return null;
    }
}