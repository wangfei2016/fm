/*
 * Copyright © 2021 https://www.cestc.cn/ All rights reserved.
 */
package com.xff.servicesmep.common;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 请对类注释说明.
 *
 * @author wang_fei
 * @since 2022/4/16 16:29
 */
public class ExcelToSql {

    public static void main(String[] args) {
        // Excel路径
        String excelPath = "D:\\基础画像数据模型.xlsx";
        //初始化输入流
        InputStream is = null;
        try {
            //根据临时文件实例化输入流
            is = Files.newInputStream(new File(excelPath).toPath());
            //创建Workbook的对象
            Workbook wb = new XSSFWorkbook(is);
            //得到第一个shell
            Sheet sheet = wb.getSheetAt(0);
            //得到Excel的总行数
            int totalRows = sheet.getLastRowNum();
            //得到Excel的总列数
            int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
            List<ExcelRow> list = new ArrayList<>();
            for (int r = 0; r <= totalRows; r++) {
                Row row = sheet.getRow(r);
                ExcelRow excelRow = new ExcelRow();
                excelRow.setBmlxywm(row.getCell(0).getStringCellValue());
                excelRow.setBmlxzwm(row.getCell(1).getStringCellValue());
                excelRow.setDmz(row.getCell(2).getStringCellValue());
                excelRow.setDmmc(row.getCell(3).getStringCellValue());
                excelRow.setBz(row.getCell(4).getStringCellValue());
                list.add(excelRow);
            }
            Map<String, List<ExcelRow>> map = list.stream().collect(Collectors.groupingBy(ExcelRow::getDmz));
            list.forEach(v -> {
                if (v.getDmz().length() <= 2) {
                    v.setCj("1");
                    v.setFdmz(null);
                } else if (v.getDmz().length() == 3) {
                    v.setCj("2");
                    v.setFdmz(v.getDmz().substring(0, 2));
                } else if (v.getDmz().length() == 4) {
                    List<ExcelRow> ls = map.get(v.getDmz().substring(0, 3));
                    if (null == ls || ls.size() == 0) {
                        v.setCj("2");
                        v.setFdmz(v.getDmz().substring(0, 2));
                    } else {
                        v.setCj("3");
                        v.setFdmz(v.getDmz().substring(0, 3));
                    }
                }
            });
            list.forEach(excelRow -> {
                String strSql =
                        "INSERT INTO gg_zd (id,bmlxywm,bmlxzwm,dmz,dmmc,fdmz,cj,bz) VALUES " +
                                "('" + UUID.randomUUID().toString().replace("-", "") + "','" + excelRow.getBmlxywm() + "','" + excelRow.getBmlxzwm() +
                                "','" + excelRow.getDmz() + "','" + excelRow.getDmmc() + "','" + excelRow.getFdmz() + "'," + "'" + excelRow.getCj() + "','" + excelRow.getBz() +
                                "');";
                System.out.println(strSql);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Getter
    @Setter
    static class ExcelRow {
        String bmlxywm;
        String bmlxzwm;
        String dmz;
        String dmmc;
        String fdmz;
        String cj;
        String bz;
    }
}
