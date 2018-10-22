package com.kakao.pay;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CompleteTodoWorkTest {

	@BeforeClass
    public static void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
	
	@Test
	public void completeTodoWorkTest_성공() {
		put("/test/works?isCompletion=true").content("[workId=1, workTitle=테스트, uprWorkId=null, firstRegDtm=2018-10-25 08:55:33]");
	}
}
