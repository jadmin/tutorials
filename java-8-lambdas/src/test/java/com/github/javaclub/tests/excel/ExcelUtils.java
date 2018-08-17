/*
 * @(#)ExcelUtils.java	2017年6月27日
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package com.github.javaclub.tests.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.javaclub.sword.core.Strings;
import com.google.common.collect.Lists;

/**
 * ExcelUtils
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: ExcelUtils.java 2017年6月27日 17:33:46 Exp $
 */
public class ExcelUtils {
	
	public static List<List<String>> readXlsxRows(InputStream in) throws Exception {
		
		List<List<String>> result = Lists.newArrayList();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
        // 获取每一个工作薄
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) {
            return Lists.newArrayList();
        }
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            List<String> row = Lists.newArrayList();
            if (xssfRow != null) {
                XSSFCell one = xssfRow.getCell(0);
                row.add(getXSSFCellValue(one));
                row.add(getXSSFCellValue(xssfRow.getCell(1)));
                row.add(getXSSFCellValue(xssfRow.getCell(2)));
            }
            if(row.size() > 0) {
            		result.add(row);
            }
        }
		
		return result;
		
	}
	
	/**
	 * 读取xlsx文件第1列数据
	 */
	public static List<String> readXlsx1rdCol(InputStream in) throws Exception {
		List<String> result = Lists.newArrayList();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
        // 获取每一个工作薄
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        if (xssfSheet == null) {
            return Lists.newArrayList();
        }
        // 获取当前工作薄的每一行
        for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                XSSFCell one = xssfRow.getCell(0);
                result.add(getXSSFCellValue(one));
            }
        }
		
		return result;
	}

	/**
	 * 读取xls文件第1列数据
	 */
	public static List<String> readXls1rdCol(InputStream in) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(in);
		List<String> result = Lists.newArrayList();
		HSSFSheet sheet = workbook.getSheetAt(0);
		int numberofrows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < numberofrows; i++) { // 跳过第一行
			HSSFRow row = sheet.getRow(i); // 遍历每一行
			int cellcount = row.getPhysicalNumberOfCells();
			if(cellcount > 0) {
				result.add(getCellAsString(row.getCell(0)));
			}
		}
		
		return result;
	}
	
	public static String getXSSFCellValue(XSSFCell xssfRow) {

		if(null == xssfRow) {
			return null;
		}
        if (xssfRow.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
        	xssfRow.setCellType(XSSFCell.CELL_TYPE_STRING);
            return xssfRow.getStringCellValue();
        } else {
            return String.valueOf(xssfRow.getStringCellValue());
        }
    }

	public static String getCellAsString(HSSFCell cell) {
		if (null == cell)
			return Strings.EMPTY_STRING;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return Strings.EMPTY_STRING;
		}
	}
	
	public static void main(String[] args) throws Exception {
		InputStream stream = new FileInputStream("/Users/chenzq/Documents/globalscanner/风控/商品标题过滤/本地生活商品.xlsx");
		List<List<String>> lines = readXlsxRows(stream);
		for (List<String> row : lines) {
			System.out.println(row);
		}
	}

}
