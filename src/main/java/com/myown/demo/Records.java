package com.myown.demo;

public class Records {
    private String incomeCode;
    private String description;
    private String date;
    private double incomeAmount;
    private double withHoldingTax;
    private int checksum;
    private boolean valid;

    public Records(String incomeCode, String description, String date, double incomeAmount, double withHoldingTax, int checksum) {
        this.incomeCode = incomeCode;
        this.description = description;
        this.date = date;
        this.incomeAmount = incomeAmount;
        this.withHoldingTax = withHoldingTax;
        this.checksum = checksum;
        this.valid = false;
    }

    public String getIncomeCode() {
        return incomeCode;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public double getWithHoldingTax() {
        return withHoldingTax;
    }

    public int getChecksum() {
        return checksum;
    }

    public boolean isValid() {
        return valid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }
    public void setWithHoldingTax(double withHoldingTax) {
        this.withHoldingTax = withHoldingTax;
    }
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public static int calculateChecksum(String code,String description,String date,double incomeAmount,double withHoldingTax) {
        String line = code + description + date + incomeAmount + withHoldingTax;
        int uppercaseCount = 0;
        int lowercaseCount = 0;
        int digitCount = 0;

        for(char ch : line.toCharArray()) {
            if(Character.isUpperCase(ch)) {
                uppercaseCount++;
            }else if(Character.isLowerCase(ch)) {
                lowercaseCount++;
            }else if(Character.isDigit(ch)) {
                digitCount++;
            }
            return uppercaseCount + lowercaseCount + digitCount;
        }
    }
}

