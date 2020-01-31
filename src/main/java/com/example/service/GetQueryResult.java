package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetQueryResult {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DepartmentMapper mapper;

    public void splitResultsBySheets(){

        // Path of the SQL file
        String sqlPath = "input.sql";

        // Column name used for order by
        // Excel sheet will be split by this column
        String orderBy = "spcode";
        String currentOrderBy = "";
        String tmpOrderBy = "";
        int sheetNo = 0;

        // Get result from database
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(loadSqlQueryFromFile(sqlPath));
        System.out.println(mapList.get(0).keySet());

        // Format the headers
        List<List<String>> headers = head(mapList);

        // Initialize data list
        List<List<Object>> datalist = new ArrayList<List<Object>>();

        // Output path
        String fileName = ".\\export\\aaa.xlsx";

        // Initialize ExcelWriter
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();

        // Iterate each row of the result List
        for (int i = 0; i < mapList.size(); i++) {
            // Clear the data row at the beginning
            List<Object> datarow = new ArrayList<Object>();

            // orderBy of current row
            currentOrderBy = (String) mapList.get(i).get(orderBy);
            System.out.println("i="+i+" -> Current: "+currentOrderBy);

            // Compare current row with previous row
            if (!currentOrderBy.equals(tmpOrderBy) && !"".equals(tmpOrderBy)){
                String sheetName = tmpOrderBy.replace("/", "_");
                sheetNo++;

                // Write new sheet for all data from previous orderBy
                WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo, sheetName).head(headers).build();
                excelWriter.write(datalist, writeSheet);

                System.out.println("Write to " + sheetNo + "<>"+ sheetName);

                // Clear the list
                datalist.clear();
            }

            // Add current row
            for(List<String> x : headers){
                datarow.add(mapList.get(i).get(x.get(0)));
            }
            datalist.add(datarow);

            // Store current orderBy
            tmpOrderBy = currentOrderBy;
        }


        // Final write
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo+1, currentOrderBy.replace("/", "_")).head(headers).build();
        excelWriter.write(datalist, writeSheet);
        System.out.println("Final Write to " + sheetNo + "<>"+ currentOrderBy.replace("/", "_"));

        // Close
        excelWriter.finish();
    }

    public void splitResultsByWorkbooks(){

        // Path of the SQL file
        String sqlPath = "input.sql";

        // Column name used for order by
        // Excel sheet will be split by this column
        String orderBy = "spcode";
        String currentOrderBy = "";
        String tmpOrderBy = "";
        int sheetNo = 0;

        // Get result from database
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(loadSqlQueryFromFile(sqlPath));
        System.out.println(mapList.get(0).keySet());

        // Format the headers
        List<List<String>> headers = head(mapList);

        // Initialize data list
        List<List<Object>> datalist = new ArrayList<List<Object>>();

        // Output path
        String path = ".\\export\\";



        // Iterate each row of the result List
        for (int i = 0; i < mapList.size(); i++) {
            // Clear the data row at the beginning
            List<Object> datarow = new ArrayList<Object>();

            // orderBy of current row
            currentOrderBy = (String) mapList.get(i).get(orderBy);
            System.out.println("i="+i+" -> Current: "+currentOrderBy);

            // Compare current row with previous row
            if (!currentOrderBy.equals(tmpOrderBy) && !"".equals(tmpOrderBy)){
                String sheetName = tmpOrderBy.replace("/", "_");
                sheetNo++;

                // Write new workboot for all data from previous orderBy
                // Initialize ExcelWriter
                ExcelWriter excelWriter = EasyExcel.write(path + sheetName+".xlsx").build();
                WriteSheet writeSheet = EasyExcel.writerSheet("Data").head(headers).build();
                excelWriter.write(datalist, writeSheet);
                excelWriter.finish();

                System.out.println("Write to " + sheetNo + "<>"+ sheetName);

                // Clear the list
                datalist.clear();
            }

            // Add current row
            for(List<String> x : headers){
                datarow.add(mapList.get(i).get(x.get(0)));
            }
            datalist.add(datarow);

            // Store current orderBy
            tmpOrderBy = currentOrderBy;
        }

        // Final write
        ExcelWriter excelWriter = EasyExcel.write(path + currentOrderBy.replace("/", "_")+".xlsx").build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Data").head(headers).build();
        excelWriter.write(datalist, writeSheet);

        // Close
        excelWriter.finish();
    }

    /**
     * Loads the sql query from the given filename
     *
     * @param filename The name of the file containing the sql query
     * @return A String representation of the sql query
     */
    private String loadSqlQueryFromFile(String filename){
        final int CHUNKSIZE = 512;
        StringBuffer output = new StringBuffer();
        try{
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            char[] buff = new char[CHUNKSIZE];
            for(int len = br.read(buff); len > 0; len = br.read(buff)){
                output.append(buff, 0, len);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());;
        }
        return output.toString();
    }

    private List<List<String>> head(List<Map<String, Object>> in) {
        List<List<String>> list = new ArrayList<List<String>>();
        in.get(0).keySet().forEach(x -> {List<String> head = new ArrayList<String>();
            head.add(x);
            list.add(head);});

        return list;
    }
}
