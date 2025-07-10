import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ContentManager {

    private int income;
    private int incomeChange;
    private int totalAllocated;
    private int totalAllocatedChange;
    private int expended;
    private int expendedChange;
    private HashMap<String, Category> categories;
    private String fileName;

    public ContentManager(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.expended = 0;
        this.expendedChange = 0;
        this.totalAllocated = 0;
        this.totalAllocatedChange = 0;
        this.categories = new HashMap<>();
    }

    public void pullContent() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/workbooks/" + fileName + ".xlsx")) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            //Take in the budget sheet
            Sheet budget = workbook.getSheet("budget");
            for (int i = 1; i < budget.getPhysicalNumberOfRows(); i++) {
                Row row = budget.getRow(i);
                Category category = new Category();
                double allocatedAmount = row.getCell(1).getNumericCellValue();
                int allocatedChange = (int) (allocatedAmount - ((int) allocatedAmount)) * 100;
                //Set category name
                category.setName(row.getCell(0).getStringCellValue());
                //Set allocated amounts
                category.setAllocated((int) allocatedAmount);
                category.setAllocatedChange(allocatedChange);
                //Update totals for budget
                totalAllocated += (int) allocatedAmount;
                totalAllocatedChange +=  allocatedChange;
                //Set Remaining amounts in category
                category.setRemaining(category.getAllocated());
                category.setRemainingChange(category.getAllocatedChange());
                category.setBudgeted(true);
                categories.put(category.getName(), category);
            }
            //Take in the transactions
            Sheet transactions = workbook.getSheet("transactions");
            for (int i = 1; i < transactions.getPhysicalNumberOfRows() - 1; i++) {
                Row row = transactions.getRow(i);
                Category category = categories.get(row.getCell(2).getStringCellValue());
                if (category == null) {
                    category = new Category();
                    category.setName(row.getCell(2).getStringCellValue());
                    category.setAllocated(0);
                    category.setAllocatedChange(0);
                    category.setRemaining(0);
                    category.setRemainingChange(0);
                    category.setBudgeted(false);
                }
                double spentAmount = row.getCell(3).getNumericCellValue();
                int spentChange = (int) Math.round(spentAmount - (int) spentAmount) * 100;
                //Update category spent and remaining
                category.addSpent((int) spentAmount);
                category.addSpentChange(spentChange);
                category.updateRemaining(category.getRemaining() - ((int) spentAmount));
                category.updateRemainingChange(category.getRemainingChange() - spentChange);
                //Update total expended
                expended += (int) spentAmount;
                expendedChange += spentChange;
                categories.put(category.getName(), category);
            }
            //Take In Income Sheet
            Sheet incomeSheet = workbook.getSheet("income");
            for (int i = 1; i < incomeSheet.getPhysicalNumberOfRows(); i++) {
                Row row = incomeSheet.getRow(i);
                for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                    double amount = row.getCell(j).getNumericCellValue();
                    income += (int) amount;
                    incomeChange += (int) ((amount - (int) amount) * 100);
                }
            }
        } catch (IOException e) {
            System.out.println("workbook not found in the workbooks directory");
            e.printStackTrace();
        }
    }

    /*
    Parses file name to produce subject title
    Must use expected formate of month then year with no spaces
    TODO: add error message if month or year do not match expected formate
     */
    public String getSubject() {
        char[] chars = fileName.toCharArray();
        int i = 0;
        char c = chars[i];
        StringBuilder month = new StringBuilder();
        StringBuilder year = new StringBuilder();
        while (isAlpha(c)) {
            month.append(c);
            i++;
            c = chars[i];
        }
        while (i < chars.length) {
            year.append(chars[i]);
            i++;
        }
        return "Budget for " + month + " " + year;
    }

    public boolean isAlpha(char c) {
       return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        StringBuilder unbudgetedData = new StringBuilder();
        double totalSpent = expended + ((double) expendedChange / 100);
        double totalIncome = income + ((double) incomeChange / 100);
        double allocated = totalAllocated - ((double) totalAllocatedChange / 100);
        text.append("Spent $").append(totalSpent).append("\n");
        text.append("Total Income $").append(totalIncome).append("\n");
        if (totalSpent > allocated) {
            text.append("You overdrew on your budget by $")
                    .append(allocated - totalSpent)
                    .append("\n");
        } else {
            text.append("You did not exceed your budget congrats, you have an additional ")
                    .append(allocated - totalSpent)
                    .append(" to allocate.\n");
        }
        text.append("------------Category Breakdown---------------------\n");
        unbudgetedData.append("-----------Unbudgeted Breakdown----------------------\n");
        for (String key: categories.keySet()) {
            Category category = categories.get(key);
            double spent = category.getSpent() + ((double) category.getSpentChange() / 100);
            double remaining = category.getRemaining() + ((double) category.getSpentChange() / 100);
            if (category.isBudgeted()) {
                String categoryData = String.format("%-20s amount spent: %8.2f amount remaining %8.2f\n",
                        category.getName(),
                        spent,
                        remaining);
                text.append(categoryData);
            } else {
                unbudgetedData.append(String.format("%-15s amount spent: %8.2f amount remaining %8.2f\n",
                        category.getName(),
                        spent,
                        remaining));
            }
        }
        text.append(unbudgetedData);
        return text.toString();
    }

    public void getEmailPreview() {
        System.out.println("-----------Subject----------\n" + getSubject());
        System.out.println("-----------Body-------------\n" + getText());
    }

}
