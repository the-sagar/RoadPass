package com.messaging.kafka.producer.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messaging.kafka.producer.constants.Constants;
import com.messaging.kafka.producer.request.BookRide;
import com.messaging.kafka.producer.request.CancelRide;
import com.messaging.kafka.producer.response.RequestResponse;

@CrossOrigin
@RestController
@RequestMapping("/api/producer")
public class ProducerController {

	@Autowired
	KafkaTemplate<String, BookRide> template;
	
	private static final String topic = "BOOK_TOPIC";
	private static final String cancelTopic = "CANCEL_TOPIC";
	
	@PostMapping("/bookRide")
	public RequestResponse bookRide(@RequestBody BookRide bookRide) {
		RequestResponse response = new RequestResponse();
		SimpleDateFormat request_time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSSSS");
		try {
			Calendar calendar = Calendar.getInstance();	
			String timeOfRequest = request_time.format(calendar.getInstance().getTime());
			bookRide.setTimeOfRequest(timeOfRequest.toString());
			template.send(topic, new BookRide(bookRide.getUsername(), bookRide.getStarting_point(), bookRide.getEnding_point(), bookRide.getJourney_date_time(), bookRide.getTimeOfRequest()));
			response.setCode(Constants.CREATED);
			response.setMessage("Request sent successfully");
			return response;
		}catch(Exception e) {
			response.setMessage("Request couldn't be processed, please try again");
			response.setCode(Constants.INTERNAL_SERVER_ERROR);
			return response;
		}
	}
	
	@PostMapping("/cancelRide")
	public RequestResponse cancelRide(@RequestBody BookRide bookRide) {
		RequestResponse response = new RequestResponse();
		try {			
			template.send(cancelTopic, new BookRide(bookRide.getUsername()));
			response.setCode(Constants.CREATED);
			response.setMessage("Request sent successfully");
			return response;
		}catch(Exception e) {
			response.setMessage("Request couldn't be processed, please try again");
			response.setCode(Constants.INTERNAL_SERVER_ERROR);
			return response;
		}
	}
}
