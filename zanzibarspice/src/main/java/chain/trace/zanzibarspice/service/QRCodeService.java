package chain.trace.zanzibarspice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class QRCodeService {

    @Value("${app.base-url}")
    private String baseUrl;

    public String generateQRCode(String referenceCode) {
        try {
            Path dirPath = Paths.get("uploads/qrcodes");
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String verifyUrl = baseUrl + "/verify/" + referenceCode;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    verifyUrl,
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            String fileName = referenceCode + ".png";
            Path filePath = dirPath.resolve(fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            return "qrcodes/" + fileName;

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage());
        }
    }
}