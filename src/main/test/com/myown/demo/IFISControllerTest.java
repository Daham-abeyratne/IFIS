package com.myown.demo;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IFISControllerTest {

    @Test
    void testParseCSVLine() {
        IFISController controller = new IFISController();
        String line = "\"AS001\",\"Bonus\",\"12/12/2022\",10000.0,500.0,14";
        String[] parsed = controller.parseCSVLine(line);

        assertEquals(6, parsed.length);
        assertEquals("AS001", parsed[0]);
        assertEquals("Bonus", parsed[1]);
        assertEquals("12/12/2022", parsed[2]);
    }

    @Test
    void testParseCSV() throws Exception {
        // Create a temporary CSV file
        File tempFile = File.createTempFile("testdata", ".csv");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Income Code,Description,Date,Income Amount,Withholding Tax,Checksum\n");
            writer.write("AS123,Freelance,01/01/2023,50000.0,2500.0,13\n");
        }

        IFISController controller = new IFISController();
        List<Records> records = controller.parseCSV(tempFile.getAbsolutePath());

        assertEquals(1, records.size());
        assertEquals("AS123", records.get(0).getIncomeCode());
        assertEquals(50000.0, records.get(0).getIncomeAmount());
    }
}
