package com.kakao.pay;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetTodoListTest {
	
	@BeforeClass
    public static void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }
	
	@Test
	public void getTodoListTest_성공() {
		get("/test/works?page=1");
	}
}
