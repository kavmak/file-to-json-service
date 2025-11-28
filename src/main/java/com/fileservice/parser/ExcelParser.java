package com.fileservice.parser;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;

public class ExcelParser {
    public static List<Map<String, String>> parse(InputStream is) throws IOException {
        List<Map<String, String>> out = new ArrayList<>();
        
        try {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            
            Iterator<Row> iter = sheet.iterator();
            if (!iter.hasNext()) {
                return out;
            }
            
            Row headerRow = iter.next();
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell c = headerRow.getCell(i);
                String header = "";
                if (c != null) {
                    if (c.getCellType() == CellType.STRING) {
                        header = c.getStringCellValue();
                    } else {
                        header = c.toString();
                    }
                }
                headers.add(header);
            }
            
            while (iter.hasNext()) {
                Row r = iter.next();
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell c = r.getCell(i);
                    String cellValue = "";
                    if (c != null) {
                        switch (c.getCellType()) {
                            case NUMERIC:
                                double numValue = c.getNumericCellValue();
                                if (numValue == Math.floor(numValue)) {
                                    cellValue = String.valueOf((long) numValue);
                                } else {
                                    cellValue = String.valueOf(numValue);
                                }
                                break;
                            case STRING:
                                cellValue = c.getStringCellValue();
                                break;
                            case BOOLEAN:
                                cellValue = String.valueOf(c.getBooleanCellValue());
                                break;
                            case FORMULA:
                                cellValue = c.getCellFormula();
                                break;
                            default:
                                cellValue = c.toString();
                        }
                    }
                    row.put(headers.get(i), cellValue);
                }
                out.add(row);
            }
            
            wb.close();
            
        } catch (Exception e) {
            throw new IOException("Failed to parse Excel file: " + e.getMessage(), e);
        }
        
        return out;
    }
}