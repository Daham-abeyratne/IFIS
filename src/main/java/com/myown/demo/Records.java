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
    }

    public static int calculateChecksum(String incomeCode, String description, String date, double incomeAmount, double withHoldingTax) {
        return (incomeCode + description + date).length() + (int) (incomeAmount + withHoldingTax);
    }

    public String getIncomeCode() { return incomeCode; }
    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
        isIncomeCodeValid();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        isDescriptionValid();
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getIncomeAmount() { return incomeAmount; }
    public void setIncomeAmount(double incomeAmount) { this.incomeAmount = incomeAmount; }

    public double getWithHoldingTax() { return withHoldingTax; }
    public void setWithHoldingTax(double withHoldingTax) { this.withHoldingTax = withHoldingTax; }

    public int getChecksum() { return checksum; }
    public void setChecksum(int checksum) { this.checksum = checksum; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public void isDescriptionValid(){
        if(this.description == null || this.description.length() > 20 || !this.valid){
            setValid(false);
        }else {
            setValid(true);
        }
    }

    public void isIncomeCodeValid(){
        if(this.incomeCode == null || this.incomeCode.length() !=5 ){
            setValid(false);
        }
        int letterCount = 0;
        int digitCount = 0;

        for (int i = 0; i < 5; i++) {
            char ch = this.incomeCode.charAt(i);
            if (Character.isLetter(ch)) {
                letterCount++;
            } else if (Character.isDigit(ch)) {
                digitCount++;
            } else {
                setValid(false); // invalid char
                return;
            }
        }
        if(letterCount == 2 && digitCount == 3){
            setValid(true);
        }
    }
}
