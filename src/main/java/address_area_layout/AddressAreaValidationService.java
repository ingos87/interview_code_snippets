package address_area_layout;

import java.awt.image.BufferedImage;
import java.util.List;

@Service
public class AddressAreaValidationService {

    public ValidationResult validatePdf(byte[] pdfBytes) throws IOException {

        PDDocument document = PDDocument.load(pdfBytes);

        PDFRenderer renderer = new PDFRenderer(document);

        BufferedImage image = renderer.renderImage(0, 300);

        boolean addressBlockValid = checkAddressBlock(image);

        document.close();

        if (addressBlockValid) {
            return new ValidationResult(true, List.of());
        } else {
            return new ValidationResult(false, List.of("Address block invalid"));
        }
    }

    private boolean checkAddressBlock(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        // Assume address block must be in left third of page
        int addressRegionWidth = width / 3;

        for (int x = 0; x < addressRegionWidth; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = image.getRGB(x, y);

                if (pixel == 0) { // assume black text pixel
                    return true;
                }
            }
        }

        return false;
    }
}

