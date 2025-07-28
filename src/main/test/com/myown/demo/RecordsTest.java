package com.myown.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RecordsTest {

    private Records validRecord;
    private String validIncomeCode = "AB123";
    private String validDescription = "Test Income";
    private String validDate = "01/15/2024";
    private double validIncomeAmount = 1000.0;
    private double validWithHoldingTax = 200.0;
    private int validChecksum;

    @BeforeEach
    void setUp() {
        // Use RecordsWrapper checksum calculation for valid records
        validChecksum = RecordsWrapper.calculateItemChecksum(validIncomeCode, validDescription, validDate, validIncomeAmount, validWithHoldingTax);
        validRecord = new Records(validIncomeCode, validDescription, validDate, validIncomeAmount, validWithHoldingTax, validChecksum);
    }

    @Test
    void getIncomeCode() {
        assertEquals(validIncomeCode, validRecord.getIncomeCode());
    }

    @Test
    void setIncomeCode() {
        String newCode = "CD789";
        validRecord.setIncomeCode(newCode);
        assertEquals(newCode, validRecord.getIncomeCode());
    }

    @Test
    void getDescription() {
        assertEquals(validDescription, validRecord.getDescription());
    }

    @Test
    void setDescription() {
        String newDescription = "New Description";
        validRecord.setDescription(newDescription);
        assertEquals(newDescription, validRecord.getDescription());
    }

    @Test
    void getDate() {
        assertEquals(validDate, validRecord.getDate());
    }

    @Test
    void setDate() {
        String newDate = "02/20/2024";
        validRecord.setDate(newDate);
        assertEquals(newDate, validRecord.getDate());
    }

    @Test
    void getIncomeAmount() {
        assertEquals(validIncomeAmount, validRecord.getIncomeAmount());
    }

    @Test
    void setIncomeAmount() {
        double newAmount = 1500.0;
        validRecord.setIncomeAmount(newAmount);
        assertEquals(newAmount, validRecord.getIncomeAmount());
    }

    @Test
    void getWithHoldingTax() {
        assertEquals(validWithHoldingTax, validRecord.getWithHoldingTax());
    }

    @Test
    void setWithHoldingTax() {
        double newTax = 300.0;
        validRecord.setWithHoldingTax(newTax);
        assertEquals(newTax, validRecord.getWithHoldingTax());
    }

    @Test
    void getChecksum() {
        assertEquals(validChecksum, validRecord.getChecksum());
    }

    @Test
    void setChecksum() {
        int newChecksum = 999;
        validRecord.setChecksum(newChecksum);
        assertEquals(newChecksum, validRecord.getChecksum());
    }

    @Test
    void isValid() {
        // Valid record should be valid
        assertTrue(validRecord.isValid());

        // Test with invalid income code
        Records invalidRecord = new Records("INVALID", validDescription, validDate, validIncomeAmount, validWithHoldingTax,
                RecordsWrapper.calculateItemChecksum("INVALID", validDescription, validDate, validIncomeAmount, validWithHoldingTax));
        assertFalse(invalidRecord.isValid());

        // Test with invalid description (too long)
        String longDesc = "This description is way too long for validation";
        invalidRecord = new Records(validIncomeCode, longDesc, validDate, validIncomeAmount, validWithHoldingTax,
                RecordsWrapper.calculateItemChecksum(validIncomeCode, longDesc, validDate, validIncomeAmount, validWithHoldingTax));
        assertFalse(invalidRecord.isValid());

        // Test with invalid date
        invalidRecord = new Records(validIncomeCode, validDescription, "invalid-date", validIncomeAmount, validWithHoldingTax,
                RecordsWrapper.calculateItemChecksum(validIncomeCode, validDescription, "invalid-date", validIncomeAmount, validWithHoldingTax));
        assertFalse(invalidRecord.isValid());

        // Test with invalid income amount
        invalidRecord = new Records(validIncomeCode, validDescription, validDate, -100.0, validWithHoldingTax,
                RecordsWrapper.calculateItemChecksum(validIncomeCode, validDescription, validDate, -100.0, validWithHoldingTax));
        assertFalse(invalidRecord.isValid());
    }

    @Test
    void setValid() {
        // Test that setValid() correctly sets the valid flag based on all validations
        assertTrue(validRecord.isValid());

        // Modify record to make it invalid and call setValid()
        validRecord.setIncomeAmount(-100.0);
        validRecord.setValid();
        assertFalse(validRecord.isValid());

        // Fix the record and call setValid()
        validRecord.setIncomeAmount(1000.0);
        validRecord.setValid();
        assertTrue(validRecord.isValid());

        // Test with invalid checksum
        validRecord.setChecksum(999);
        validRecord.setValid();
        assertFalse(validRecord.isValid());

        // Fix checksum using RecordsWrapper calculation
        validRecord.setChecksum(RecordsWrapper.calculateItemChecksum(
                validRecord.getIncomeCode(), validRecord.getDescription(), validRecord.getDate(),
                validRecord.getIncomeAmount(), validRecord.getWithHoldingTax()));
        validRecord.setValid();
        assertTrue(validRecord.isValid());
    }

    @Test
    void isChecksumValid() {
        // Valid checksum
        assertTrue(validRecord.isChecksumValid());

        // Invalid checksum
        validRecord.setChecksum(999);
        assertFalse(validRecord.isChecksumValid());

        // Reset to valid checksum
        validRecord.setChecksum(validChecksum);
        assertTrue(validRecord.isChecksumValid());
    }

    @Test
    void isDescriptionValid() {
        // Valid description
        assertTrue(validRecord.isDescriptionValid());

        // Null description
        validRecord.setDescription(null);
        assertFalse(validRecord.isDescriptionValid());

        // Empty description (valid)
        validRecord.setDescription("");
        assertTrue(validRecord.isDescriptionValid());

        // Maximum length description (valid)
        validRecord.setDescription("12345678901234567890"); // 20 characters
        assertTrue(validRecord.isDescriptionValid());

        // Too long description (invalid)
        validRecord.setDescription("123456789012345678901"); // 21 characters
        assertFalse(validRecord.isDescriptionValid());
    }

    @Test
    void isIncomeCodeValid() {
        // Valid income code
        assertTrue(validRecord.isIncomeCodeValid());

        // Null income code
        validRecord.setIncomeCode(null);
        assertFalse(validRecord.isIncomeCodeValid());

        // Wrong length
        validRecord.setIncomeCode("AB12");
        assertFalse(validRecord.isIncomeCodeValid());

        validRecord.setIncomeCode("AB1234");
        assertFalse(validRecord.isIncomeCodeValid());

        // Wrong patterns
        validRecord.setIncomeCode("ABC12");
        assertFalse(validRecord.isIncomeCodeValid());

        validRecord.setIncomeCode("A1234");
        assertFalse(validRecord.isIncomeCodeValid());

        validRecord.setIncomeCode("12ABC");
        assertFalse(validRecord.isIncomeCodeValid());

        // Invalid characters
        validRecord.setIncomeCode("AB1@3");
        assertFalse(validRecord.isIncomeCodeValid());

        // Valid pattern
        validRecord.setIncomeCode("AB123");
        assertTrue(validRecord.isIncomeCodeValid());

        validRecord.setIncomeCode("A1B23");
        assertTrue(validRecord.isIncomeCodeValid());
    }

    @Test
    void isDateValid() {
        // Valid date
        assertTrue(validRecord.isDateValid());

        // Null date
        validRecord.setDate(null);
        assertFalse(validRecord.isDateValid());

        // Invalid format
        validRecord.setDate("2024-01-15");
        assertFalse(validRecord.isDateValid());

        validRecord.setDate("1/15/2024");
        assertFalse(validRecord.isDateValid());

        validRecord.setDate("01/1/2024");
        assertFalse(validRecord.isDateValid());

        validRecord.setDate("01/15/24");
        assertFalse(validRecord.isDateValid());

        // Valid formats
        validRecord.setDate("01/15/2024");
        assertTrue(validRecord.isDateValid());

        validRecord.setDate("12/31/1999");
        assertTrue(validRecord.isDateValid());

        validRecord.setDate("00/00/0000");
        assertTrue(validRecord.isDateValid()); // Only checks format, not validity of actual date
    }

    @Test
    void isIncomeAmountValid() {
        // Valid income amount
        assertTrue(validRecord.isIncomeAmountValid());

        // Zero income amount (invalid)
        validRecord.setIncomeAmount(0.0);
        assertFalse(validRecord.isIncomeAmountValid());

        // Negative income amount (invalid)
        validRecord.setIncomeAmount(-100.0);
        assertFalse(validRecord.isIncomeAmountValid());

        // Small positive amount (valid)
        validRecord.setIncomeAmount(0.01);
        assertTrue(validRecord.isIncomeAmountValid());

        // Large amount (valid)
        validRecord.setIncomeAmount(999999.99);
        assertTrue(validRecord.isIncomeAmountValid());
    }

    @Test
    void constructorWithValidData() {
        String code = "XY789";
        String desc = "Bonus";
        String date = "06/30/2024";
        double income = 2000.0;
        double tax = 400.0;
        int checksum = RecordsWrapper.calculateItemChecksum(code, desc, date, income, tax);

        Records record = new Records(code, desc, date, income, tax, checksum);

        assertTrue(record.isValid());
        assertEquals(code, record.getIncomeCode());
        assertEquals(desc, record.getDescription());
        assertEquals(date, record.getDate());
        assertEquals(income, record.getIncomeAmount());
        assertEquals(tax, record.getWithHoldingTax());
    }

    @Test
    void constructorWithInvalidData() {
        Records record = new Records("INVALID", "This description is way too long", "invalid", -100.0, 50.0, 999);

        assertFalse(record.isValid());
        assertFalse(record.isIncomeCodeValid());
        assertFalse(record.isDescriptionValid());
        assertFalse(record.isDateValid());
        assertFalse(record.isIncomeAmountValid());
        assertFalse(record.isChecksumValid());
    }
}