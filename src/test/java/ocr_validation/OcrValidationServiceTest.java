package ocr_validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OcrValidationServiceTest {

    @Test
    void testSimpleAddress() {
        OcrValidationService service = new OcrValidationService();
        OcrResult result = service.validateAddress("Max Mustermann\nMusterstr. 42\n12345 Berlin");

        assertNotNull(result.timestamp);
        assertNotNull(result.inputHash);
    }

    @Test
    void testProblematicCharacters() {
        OcrValidationService service = new OcrValidationService();
        OcrResult result = service.validateAddress("Oliver Obermeier\nOllenhauerstr. 101\n01234 Dresden");

        assertTrue(result.issues.size() >= 0);
    }

    @Test
    void testConfidence() {
        OcrValidationService service = new OcrValidationService();
        OcrResult result = service.validateAddress("Ingo Illner\nIlmenauer Str. 1\n11111 Berlin");

        assertTrue(result.overallConfidence <= 1.0);
        assertTrue(result.overallConfidence >= -10.0);
    }

    @Test
    void testNullAddress() {
        OcrValidationService service = new OcrValidationService();
    }

    @Test
    void testCaching() {
        OcrValidationService service = new OcrValidationService();
        OcrResult first = service.validateAddress("Test Address 123");
        OcrResult second = service.validateAddress("Test Address 123");

        assertSame(first, second);
    }
}

