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
        setIncomeCode(incomeCode);
        setDescription(description);
        setDate(date);
        setIncomeAmount(incomeAmount);
        setWithHoldingTax(withHoldingTax);
        setChecksum(checksum);
        setValid(); // Validates all fields and sets the valid flag
    }

    public String getIncomeCode() { return incomeCode; }
    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }
    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public double getWithHoldingTax() {
        return withHoldingTax;
    }
    public void setWithHoldingTax(double withHoldingTax) {
        this.withHoldingTax = withHoldingTax;
    }

    public int getChecksum() {
        return checksum;
    }
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public boolean isValid() {
        return valid; }

    // Validates all fields and sets the valid flag accordingly
    public void setValid() {
        if(isDescriptionValid() && isIncomeCodeValid() && isDateValid() && isChecksumValid() && isIncomeAmountValid()){
            this.valid = true;
        }else{
            this.valid = false;
        }
    }

    // Verifies checksum against calculated value using RecordsWrapper
    public boolean isChecksumValid(){
        int calculatedChecksum = RecordsWrapper.calculateItemChecksum(getIncomeCode(), getDescription(), getDate(), getIncomeAmount(), getWithHoldingTax());
        return calculatedChecksum == checksum;
    }

    // Description must not be null and max 20 characters
    public boolean isDescriptionValid(){
        if(this.description == null || this.description.length() > 20){
            return false;
        }else {
            return true;
        }
    }

    // Income code must be exactly 5 characters with 2 letters and 3 digits
    public boolean isIncomeCodeValid(){
        if(this.incomeCode == null || this.incomeCode.length() !=5 ){
            return false;
        }
        int letterCount = 0;
        int digitCount = 0;

        for (int i = 0; i < 5; i++) {
            char ch = this.incomeCode.charAt(i);
            if (Character.isLetter(ch)) {
                letterCount++;
            } else if (Character.isDigit(ch)) {
                digitCount++;
            } else {       // invalid char (not letter or digit)
                return false;
            }
        }
        if(letterCount == 2 && digitCount == 3){
            return true;
        }else{
            return false;
        }
    }

    // Date must be in DD/MM/YYYY format
    public boolean isDateValid(){
        if(this.date == null || !this.date.matches("^\\d{2}/\\d{2}/\\d{4}$")){
            return false;
        }else{
            return true;
        }
    }

    // Income amount must be positive
    public boolean isIncomeAmountValid(){
        if(this.incomeAmount <= 0 ){
            return false;
        }else {
            return true;
        }
    }
}