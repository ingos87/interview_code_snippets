package ocr_validation;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/ocr")
public class OcrValidationController {

    OcrValidationService service = new OcrValidationService();

    @PostMapping("/validate")
    public OcrResult validate(@RequestBody HashMap<String, String> body) {
        String address = body.get("address");
        return service.validateAddress(address);
    }

    @GetMapping("/cache/clear")
    public String clearCache() {
        OcrValidationService.getCache().clear();
        return "Cache cleared";
    }

    @GetMapping("/result/{hash}")
    public OcrResult getResult(@PathVariable String hash) {
        OcrResult result = OcrValidationService.getCache().get(hash);
        if (result == null) {
            throw new RuntimeException("Result not found: " + hash);
        }
        return result;
    }

    @PostMapping("/batch")
    public HashMap<String, OcrResult> validateBatch(@RequestBody String[] addresses) {
        HashMap<String, OcrResult> results = new HashMap<>();
        for (String addr : addresses) {
            OcrResult r = service.validateAddress(addr);
            results.put(addr, r);
        }
        return results;
    }
}

