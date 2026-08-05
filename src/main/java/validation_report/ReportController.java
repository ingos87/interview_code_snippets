package validation_report;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private ReportGeneratorService reportGeneratorService;

    public ReportController(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @PostMapping("/generate")
    public ValidationReport generateReport(@RequestBody HashMap<String, String> data) {
        return reportGeneratorService.generateReport(
            data.get("addressId"),
            data.get("postalCode"),
            data.get("street"),
            data.get("city"),
            data.get("recipient"),
            data.get("country")
        );
    }

    @GetMapping("/{addressId}")
    public ValidationReport getReport(@PathVariable String addressId) {
        ValidationReport report = reportGeneratorService.getStoredReport(addressId);
        if (report == null) {
            throw new RuntimeException("Report not found for addressId: " + addressId);
        }
        return report;
    }

    @GetMapping("/all")
    public List<ValidationReport> getAllReports() {
        return reportGeneratorService.getAllReports();
    }

    @DeleteMapping("/clear")
    public String clearAll() {
        reportGeneratorService.clearReports();
        return "All reports cleared";
    }
}

