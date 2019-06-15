package com.notification.notification;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	
	/**
	 * TEST 22
	 * 
	 * Send notification with destination null, subject not empty and body not empty
	 * 
	 */
	@Test
	public void test22_sendNotification_DestNull_SubNotEmpty_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("subject", "sub")
				.put("body", "bod");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	
	/**
	 * TEST 23
	 * 
	 * Send notification with destination empty, subject not empty and body not empty
	 * 
	 */
	@Test
	public void test23_sendNotification_DestEmpty_SubNotEmpty_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "")
				.put("subject", "sub")
				.put("body", "bod");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 24
	 * 
	 * Send notification with destination not valid, subject not Empty and body not empty
	 * 
	 */
	@Test
	public void test24_sendNotification_DestNotValid_SubNotEmpty_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "loren.s")
				.put("subject", "sub")
				.put("body", "bod");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 25
	 * 
	 * Send notification with destination valid, subject null and body not empty
	 * 
	 */
	@Test
	public void test25_sendNotification_DestValid_SubNull_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "l.vito@hotmail.com")
				.put("body", "bod");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 26
	 * 
	 * Send notification with destination valid, subject empty and body not empty
	 * 
	 */
	@Test
	public void test26_sendNotification_DestValid_SubEmpty_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "l.vito@hotmail.com")
				.put("subject", "")
				.put("body", "bod");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 27
	 * 
	 * Send notification with destination valid, subject not empty and body null
	 * 
	 */
	@Test
	public void test27_sendNotification_DestValid_SubNotEmpty_BodyNull() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "l.vito@hotmail.com")
				.put("subject", "sub");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 28
	 * 
	 * Send notification with destination valid, subject not empty and body empty
	 * 
	 */
	@Test
	public void test28_sendNotification_DestValid_SubNotEmpty_BodyEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "l.vito@hotmail.com")
				.put("subject", "sub")
				.put("body", "");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isPartialContent());
	}
	
	/**
	 * TEST 29
	 * 
	 * Send notification with destination valid, subject not empty and body not empty
	 * 
	 */
	@Test
	public void test29_sendNotification_DestValid_SubNotEmpty_BodyNotEmpty() throws Exception {
		JSONObject notificationEntity = new JSONObject()
				.put("destination", "l.vito@hotmail.com")
				.put("body", "bod")
				.put("subject", "sub");
		
		mvc.perform(MockMvcRequestBuilders.post("/api/send-notification")
				.contentType(MediaType.APPLICATION_JSON)
				.content(notificationEntity.toString()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
