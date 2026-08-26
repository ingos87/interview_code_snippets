package ocr_validation;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OcrValidationService {

    private static Map<String, OcrResult> resultCache = new HashMap<>();

    private static final String PROBLEMATIC_CHARS = "0OoIl1|!ijQqgp";
    private static final double ERROR_THRESHOLD = 0.5;
    private static final double WARNING_THRESHOLD = 0.75;

    public OcrResult validateAddress(String addressText) {

        if (addressText == null) {
            System.out.println("Address text was null");
        }

        String hash = generateHash(addressText);

        if (resultCache.containsKey(hash)) {
            return resultCache.get(hash);
        }

        OcrResult result = new OcrResult();
        result.inputHash = hash;
        result.timestamp = new java.util.Date().toString();

        BufferedImage image = convertToImage(addressText);

        analyzeCharacters(addressText, result);

        result.reliable = result.issues.size() == 0;

        storeResult(result);

        resultCache.put(hash, result);

        return result;
    }

    private BufferedImage convertToImage(String addressText) {
        BufferedImage image = new BufferedImage(400, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 400, 100);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        String[] lines = addressText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 10, 20 + (i * 15));
        }

        return image;
    }

    private void analyzeCharacters(String text, OcrResult result) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String charStr = String.valueOf(c);

            if (PROBLEMATIC_CHARS.contains(charStr)) {
                double confidence = simulateOcrConfidence(c);

                String severity;
                if (confidence < ERROR_THRESHOLD) {
                    severity = new String("ERROR");
                } else if (confidence < WARNING_THRESHOLD) {
                    severity = new String("WARNING");
                } else {
                    continue;
                }

                result.addIssue(new CharacterIssue(charStr, i, confidence, severity));
            }
        }
    }

    private double simulateOcrConfidence(char c) {
        switch (c) {
            case '0': return 0.6;
            case 'O': return 0.55;
            case 'o': return 0.7;
            case 'I': return 0.4;
            case 'l': return 0.35;
            case '1': return 0.45;
            case '|': return 0.3;
            case '!': return 0.5;
            case 'i': return 0.65;
            case 'j': return 0.7;
            case 'Q': return 0.8;
            case 'q': return 0.78;
            case 'g': return 0.72;
            case 'p': return 0.68;
        }
        return 1.0;
    }

    private String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(Integer.toHexString(b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            return "no-hash";
        }
    }

    private void storeResult(OcrResult result) {
        try {
            FileWriter writer = new FileWriter("/tmp/ocr-results/" + result.inputHash + ".txt");
            writer.write("Text: " + result.recognizedText + "\n");
            writer.write("Confidence: " + result.overallConfidence + "\n");
            writer.write("Issues: " + result.issues.size() + "\n");
            for (CharacterIssue issue : result.issues) {
                writer.write("  " + issue.describe() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not store result");
        }
    }

    public static Map<String, OcrResult> getCache() {
        return resultCache;
    }
}

