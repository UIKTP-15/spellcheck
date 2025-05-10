package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/export/pdf/{id}")
    public void exportPdf(@PathVariable Long id,
                          HttpServletResponse response) throws IOException {
        byte[] pdfBytes = exportService.generatePdf(id);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=corrected-text.pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }
}
