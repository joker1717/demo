package com.example.demo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.service.GetQueryResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest
class DemoApplicationTests {

//	@Autowired
//	DataSource dataSource;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() throws SQLException {
//		System.out.println(dataSource);
//		Connection connection = dataSource.getConnection();
//		System.out.println(connection);
//		connection.close();

	}
}
