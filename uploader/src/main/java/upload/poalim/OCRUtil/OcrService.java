package upload.poalim.OCRUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OcrService {

    @Autowired
    S3Services s3Services;

    public String parseOcr(String fileName, String bucketName) throws Exception {
        try {
            return AmazonRekognitionUtil.getText(fileName, bucketName);
        } catch (Exception e) {
            throw new Exception(" problem parsing file: " + fileName);
        }
    }
}




