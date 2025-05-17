package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/export")
    public String showExportForm() {
        return "export-form";
    }

    @GetMapping("/export-pdf-no-differences/{id}")
    public void exportPdfWithoutDifferences(@PathVariable Long id,
                                            HttpServletResponse response) throws IOException {
        byte[] pdfBytes = exportService.generatePdfWithoutDifferences(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=corrected-text.pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }


    @GetMapping("/export-pdf-differences/{id}")
    public ResponseEntity<Resource> exportPdfWithDifferences(@PathVariable Long id) {
        byte[] pdfBytes = exportService.generatePdfWithDifferences(id);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "differences_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(resource);
    }
}
