package com.myown.demo;

import java.text.DecimalFormat;
import java.text.NumberFormat;

// Wrapper class that provides additional functionality for Records objects
public class RecordsWrapper {
    private final Records record;

    public RecordsWrapper(Records record) {
        this.record = record;
    }

    public String getIncomeCode() {
        return record.getIncomeCode();
    }
    public String getDescription() {
        return record.getDescription();
    }
    public String getDate() {
        return record.getDate();
    }
    public double getIncomeAmount() {
        return record.getIncomeAmount();
    }
    public double getWithholdingTax() {
        return record.getWithHoldingTax();
    }
    public int getChecksum() {
        return record.getChecksum();
    }
    public boolean getValid() {
        return record.isValid();
    }
    public Records getRecord() {
        return record;
    }

    // Calculates checksum by counting letters and digits, ignoring quotes, dots, and slashes
    public static int calculateStringChecksum(String dataString) {
        int uppercaseCount = 0;
        int lowercaseCount = 0;
        int numbersDecimalsCount = 0;

        for (int i = 0; i < dataString.length(); i++) {
            char ch = dataString.charAt(i);
            if (ch == '"' || ch == '.' || ch == '/') {
                continue; // Skip these characters
            }
            if (Character.isUpperCase(ch)) {
                uppercaseCount++;
            } else if (Character.isLowerCase(ch)) {
                lowercaseCount++;
            } else if (Character.isDigit(ch)) {
                numbersDecimalsCount++;
            }
        }
        return uppercaseCount + lowercaseCount + numbersDecimalsCount;
    }

    // Creates data string with formatted amounts (2 decimal places) and calculates checksum
    public static int calculateItemChecksum(String incomeCode, String description, String date, double incomeAmount, double withHoldingTax) {
        NumberFormat nf = new DecimalFormat("#0.00"); // Force 2 decimal places
        String data = incomeCode + description + date + nf.format(incomeAmount) + nf.format(withHoldingTax);
        return calculateStringChecksum(data);
    }
}