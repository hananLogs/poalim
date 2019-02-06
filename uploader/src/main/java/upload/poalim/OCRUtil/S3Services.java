package upload.poalim.OCRUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface S3Services {
    public void uploadFile(String keyName, MultipartFile file);
}