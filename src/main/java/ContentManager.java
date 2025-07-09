import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ContentManager {

    private int income;
    private int incomeChange;
    private int totalAllocated;
    private int expended;
    private int expendedChange;
    private HashMap<String, Category> categories;
    private String fileName;

    public ContentManager(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.expended = 0;
        this.expendedChange = 0;
    }

    public void pullContent() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/workbooks/" + fileName + "xlsx")) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            //take in the budget
            Sheet budget = workbook.getSheet("budget");
            for (int i = 1; i < budget.getPhysicalNumberOfRows(); i++) {
                Row row = budget.getRow(i);
                Category category = new Category();
                double allocatedAmount = row.getCell(1).getNumericCellValue();
                category.setName(row.getCell(0).getStringCellValue());
                category.setAllocated((int) allocatedAmount);
                category.setAllocatedChange((int) (allocatedAmount * 100) % 100);
                category.setRemaining(category.getAllocated());
                category.setRemainingChange(category.getAllocatedChange());
                categories.put(category.getName(), category);
            }
            //take in the transactions
            Sheet transactions = workbook.getSheet("transactions");
            for (int i = 1; i < transactions.getPhysicalNumberOfRows(); i++) {
                Row row = transactions.getRow(i);
                Category category = categories.get(row.getCell(2).getStringCellValue());
                if (category == null) {throw new RuntimeException("transactions has unbudgeted category");}
                double spentAmount = row.getCell(3).getNumericCellValue();
                int spentChange = (int) Math.round(spentAmount - (int) spentAmount) * 100;
                //Update category spent and remaining
                category.addSpent((int) spentAmount);
                category.addSpentChange(spentChange);
                category.updateRemaining(category.getRemaining() - category.getSpent());
                category.updateRemainingChange(category.getRemainingChange() - category.getSpentChange());
                //Update total expended
                expended += (int) spentAmount;
                expendedChange += spentChange;
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
            i++;
            year.append(chars[i]);
        }
        return "Budget for " + month + " " + year;
    }

    public boolean isAlpha(char c) {
       return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public String getText() {
        StringBuilder calculatedTransaction = new StringBuilder();
        double totalSpent = expended + ((double) expendedChange / 100);
        calculatedTransaction.append("Spent $" + totalSpent);
        /*TODO: if spent > total allocated then append "you overdrew on budget by x"
            else "you did not exceed you budget congrats, you have an additional x amount to allocate.
         */
        calculatedTransaction.append("-------------Category Breakdown-------------");
        for (String key: categories.keySet()) {
            Category category = categories.get(key);
            double spent = category.getSpent() + ((double) category.getSpentChange() / 100);
            double remaining = category.getRemaining() + ((double) category.getSpentChange() / 100);
            String categoryData = category.getName() + "    " +
                    "amount spent:" + "   " + spent + "   " +
                    "amount remaining:" + "   " + remaining + "\n";
            calculatedTransaction.append(categoryData);
        }
        //monthly income vs spent
        /*loop through all categories and pulling amount spent and remaining balance

         */
        //show excceeded budget by or saved by

        return null;
    }

}
