package com.messaging.kafka.consumer.config;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.kafka.consumer.request.BookRide;

public class JavaDeserializer implements Deserializer<BookRide> {

	@Override
	public BookRide deserialize(String topic, byte[] data) {
		 ObjectMapper mapper = new ObjectMapper();
		 	BookRide bookRide = null;
		    try {
		    	bookRide = mapper.readValue(data, BookRide.class);
		    } catch (Exception e) {

		      e.printStackTrace();
		    }
		    return bookRide;
	}

}
