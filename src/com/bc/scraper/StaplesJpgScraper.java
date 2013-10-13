package com.bc.scraper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bc.scraper.amazon.AmazonProductPage;

public class StaplesJpgScraper {

	public static void main(String[] args) throws Exception {

		String baseDir = "";
		String seedFileName = baseDir + "staples_seed.xlsx";
		Map<String, String> seedProducts = loadSeedProducts(seedFileName);
		Document doc;
		Map<String, AmazonProductPage> resultMap = new HashMap<String, AmazonProductPage>();
		int count = 0;
		int totalUrls = seedProducts.size();
		for (String sku : seedProducts.keySet()) {
			count++;
			System.out.println("Status:(" + count + "/" + totalUrls + ")");

			String url = seedProducts.get(sku);
			AmazonProductPage amazonProductPage = new AmazonProductPage();
			resultMap.put(sku, amazonProductPage);
			try {
				doc = Jsoup.connect(url).get();
				try {
					Element elementById = doc.getElementById("ctl00_cm_s1_ucSkuSet1_webidDiv");
					String[] skuA = elementById.text().split("-");
					String staplesSku = skuA[1].trim();
					amazonProductPage.setSku(staplesSku);
					

				} catch (Exception e) {
				}
				
				amazonProductPage.setUrl(url);
				// System.out.println(AmazonProductPage);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH_mm_ss");
		Date date = new Date();
		String outputFileName = "staples_url_cm_data_" + dateFormat.format(date) + ".xlsx";
		dumpAmazonCMData(resultMap, outputFileName);
	}

	private static Map<String, String> loadSeedProducts(String seedFileName) throws Exception {
		int totalRows = 0, badUPCCount = 0, badProductIdCount = 0;
		Map<String, String> products = new LinkedHashMap<String, String>();

		XSSFWorkbook wb = readFile(seedFileName);

		for (int k = 0; k < 1; k++) {

			XSSFSheet sheet = wb.getSheetAt(k);
			int rows = sheet.getPhysicalNumberOfRows();
			totalRows = rows - 1;

			for (int r = 1; r < rows; r++) {
				try {
					XSSFRow row = sheet.getRow(r);
					if (row == null) {
						System.out.println("Ignoring Empty Row: " + r);
						continue;
					}

					XSSFCell cell0 = row.getCell(0);// SKU
					XSSFCell cell1 = row.getCell(1);// Viking URL
					if (cell0 != null) {
						String sku = getCellData(cell0);
						if (cell1 != null) {
							String url = cell1.getStringCellValue();
							products.put(sku, url);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("row = " + r + " has invalid data.");
					badUPCCount++;
					throw ex;
				}
			}
		}

		System.out.println("Valid Seed Products = " + products.size());
		System.out.println("Bad UPCs =            " + badUPCCount);
		System.out.println("No RSProduct Ids =      " + badProductIdCount);
		System.out.println("                     -----");
		System.out.println("Total Seed Products   = " + totalRows);

		return products;
	}
	
	
	private static String getCellData(XSSFCell cell){
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getRichStringCellValue().getString();
		case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
               return cell.getDateCellValue().toString();
            } else {
                return Double.toString(cell.getNumericCellValue());
            }
		case Cell.CELL_TYPE_BOOLEAN:
            return Boolean.toString(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
		default:
            return "";
    }
	}

	private static void dumpAmazonCMData(Map<String, AmazonProductPage> cmData, String outputFileName) throws IOException {

		Workbook wb = new XSSFWorkbook();
		FileOutputStream fileOut = new FileOutputStream(outputFileName);

		Sheet mainSheet = wb.createSheet("Staples page views");

		int rowCount = 0;
		Row row = mainSheet.createRow(rowCount++);

		row.createCell(0).setCellValue("Staples Ref");
		row.createCell(1).setCellValue("URL");
		row.createCell(2).setCellValue("Staples SKU");

		for (String sku : cmData.keySet()) {
			AmazonProductPage amazonProductPage = cmData.get(sku);
			row = mainSheet.createRow(rowCount++);
			row.createCell(0).setCellValue(sku);
			row.createCell(1).setCellValue(amazonProductPage.getUrl());
			row.createCell(2).setCellValue(amazonProductPage.getSku());
		}

		autoResizeColumns(mainSheet);

		wb.write(fileOut);
		fileOut.close();
	}

	private static XSSFWorkbook readFile(String filename) throws IOException {
		return new XSSFWorkbook(new FileInputStream(filename));
	}

	private static void autoResizeColumns(Sheet mainSheet) {
		for (int i = 0; i < 8; i++) {
			mainSheet.autoSizeColumn(i);
		}
	}
}
