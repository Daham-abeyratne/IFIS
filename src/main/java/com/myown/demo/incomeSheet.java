package com.myown.demo;



import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class incomeSheet {
    private List<Records> incomeItems;
    private double totalIncome;
    private double totalTax;

    public incomeSheet(double totalIncome, double totalTax) {
        this.incomeItems = new ArrayList<>();
        this.totalIncome = totalIncome;
        this.totalTax = totalTax;
    }

    public void addItem(Records item) {
        this.incomeItems.add(item);
    }

    public List<Records> getIncomeItems() {
        return incomeItems;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public int calculateStringChecksum(String dataString) {
        int uppercaseCount = 0;
        int lowercaseCount = 0;
        int numberCount = 0;

        for(int i = 0; i < dataString.length(); i++) {
            char ch = dataString.charAt(i);
            if(Character.isUpperCase(ch)) {
                uppercaseCount++;
            } else if(Character.isLowerCase(ch)) {
                lowercaseCount++;
            } else if(Character.isDigit(ch)) {
                numberCount++;
            }
        }
        return uppercaseCount + lowercaseCount + numberCount;
    }

//    public int calculateItemChecksum(Records item) {
//        NumberFormat nf = new DecimalFormat("#0.00");
//        JSONObject itemData = new JSONObject();
//    }
}
