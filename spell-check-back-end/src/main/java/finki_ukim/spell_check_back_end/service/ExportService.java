package finki_ukim.spell_check_back_end.service;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.repository.GrammarCheckRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final GrammarCheckRepository grammarCheckRepository;

    public byte[] generatePdf(Long id) throws IOException {
        GrammarCheck grammarCheck = grammarCheckRepository.findById(id).orElseThrow(NoSuchFieldError::new);
        String content = grammarCheck.getCorrectedText();

        try (PDDocument document = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PDType1Font font = PDType1Font.HELVETICA;
            float fontSize = 12;
            float leading = 1.5f * fontSize;
            float margin = 50;

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDRectangle mediaBox = page.getMediaBox();
            float width = mediaBox.getWidth() - 2 * margin;
            float startY = mediaBox.getHeight() - margin;
            float yPosition = startY;

            List<String> lines = wrapText(content, font, fontSize, width);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);

            for (String line : lines) {
                if (yPosition <= margin) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontSize);
                    yPosition = startY;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                }

                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
                yPosition -= leading;
            }

            contentStream.endText();
            contentStream.close();

            document.save(baos);
            return baos.toByteArray();
        }
    }

    private List<String> wrapText(String text, PDType1Font font, float fontSize, float width) throws IOException {
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.split("\\r?\\n")) {
            String[] words = paragraph.split(" ");
            StringBuilder line = new StringBuilder();
            for (String word : words) {
                String testLine = line + word + " ";
                float size = font.getStringWidth(testLine) / 1000 * fontSize;
                if (size > width) {
                    lines.add(line.toString().trim());
                    line = new StringBuilder(word).append(" ");
                } else {
                    line.append(word).append(" ");
                }
            }
            lines.add(line.toString().trim());
        }
        return lines;
    }

}
