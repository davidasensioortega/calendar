package org.dasensio.calendario.service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import org.dasensio.calendario.util.TestDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DocumentServiceTests {

    private DocumentService service;

    @BeforeEach
    void beforeEach() {
        service = new DocumentService();
    }

    @Test
    void generateDocument_generatesCalendar() {
        ByteArrayOutputStream baos = service.generateDocument(TestDataUtils.getCalendar());
        Assertions.assertNotNull(baos);
    }

    @Test
    void generateDocument_generatesCalendarUngrouped() {
        ByteArrayOutputStream baos = service.generateDocument(TestDataUtils.getCalendarUngrouped());
        Assertions.assertNotNull(baos);
    }

}
