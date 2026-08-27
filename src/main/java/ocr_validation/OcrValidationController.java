package ocr_validation;


import java.util.HashMap;

@RestController
@RequestMapping("/ocr")
public class OcrValidationController {

    @Autowired
    private OcrValidationService service;

    @PostMapping("/validate")
    public OcrResult validate(@RequestBody HashMap<String, String> body) {
        String address = body.get("address");
        return service.validateAddress(address);
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

