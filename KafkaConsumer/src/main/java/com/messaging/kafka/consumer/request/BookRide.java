package com.messaging.kafka.consumer.request;

import java.util.Date;

public class BookRide {    
	private String username;
	private String starting_point;
	private String ending_point;
	private String journey_date_time;
	private String timeOfRequest;		
	public BookRide() {
		super();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStarting_point() {
		return starting_point;
	}
	public void setStarting_point(String starting_point) {
		this.starting_point = starting_point;
	}
	public String getEnding_point() {
		return ending_point;
	}
	public void setEnding_point(String ending_point) {
		this.ending_point = ending_point;
	}
	public String getJourney_date_time() {
		return journey_date_time;
	}
	public void setJourney_date_time(String journey_date_time) {
		this.journey_date_time = journey_date_time;
	}
	public String getTimeOfRequest() {
		return timeOfRequest;
	}
	public void setTimeOfRequest(String timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}
}
