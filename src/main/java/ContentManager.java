import org.apache.poi.ss.formula.eval.UnaryMinusEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.xpath.XPath;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ContentManager {

    private int income;
    private int change;
    private HashMap<String, Category> categories;
    private String fileName;

    public ContentManager(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        pullContent();
    }

    public void pullContent() {
        try (FileInputStream fileInputStream = new FileInputStream("src/main/workbooks/" + fileName + "xlsx")) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet budget = workbook.getSheet("budget");
            for (int i = 1; i < budget.getPhysicalNumberOfRows(); i++) {
                Row row = budget.getRow(i);
                Category category = new Category();
                category.setName(row.getCell(0).getStringCellValue());
                category.setAllocated((int) row.getCell(1).getNumericCellValue());
                categories.put(category.getName(), category);
            }
            Sheet transactions = workbook.getSheet("transactions");
            for (int i = 1; i < transactions.getPhysicalNumberOfRows(); i++) {
                Row row = transactions.getRow(i);
                Category category = categories.get(row.getCell(2).getStringCellValue());
                if (category == null) {throw new RuntimeException("transaction has unbudgeted category");}
                category.setSpent();
                category.setRemaining();
            }
        } catch (IOException e) {
            System.out.println("workbook not found in the workbooks directory");
            e.printStackTrace();
        }
    }

    public String getSubject() {
        return null;
    }

    public String getText() {
        return null;
    }

}
