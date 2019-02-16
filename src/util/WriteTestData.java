package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteTestData {

	private static List<ResultTest> results = new ArrayList<>();

	public void addResult(ResultTest rt) {
		results.add(rt);
	}

	public void addResultToFile() throws IOException {
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for
												// generating `.xls` file

		CreationHelper createHelper = workbook.getCreationHelper();
		// Create a Sheet
		Sheet sheet = workbook.createSheet("Result");
		// Create a Row
		Row headerRow = sheet.createRow(0);
		// Create cells
		for (int i = 0; i < results.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(results.toString());

		}

		// Create Other rows and cells with data
		int rowNum = 1;
		for (ResultTest result : results) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(result.getTestcaseName());

			row.createCell(1).setCellValue(result.getTestcaseComment());
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("SigTupleTests.xlsx");
		workbook.write(fileOut);
		fileOut.close();
		// Closing the workbook
		workbook.close();
	}
}
