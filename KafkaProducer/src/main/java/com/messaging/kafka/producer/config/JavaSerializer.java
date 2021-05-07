package com.messaging.kafka.producer.config;

import java.io.Serializable;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.messaging.kafka.producer.request.BookRide;


public class JavaSerializer implements Serializer<BookRide>{

	@Override
	public byte[] serialize(String topic, BookRide data) {
		byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(data).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	}
	
}