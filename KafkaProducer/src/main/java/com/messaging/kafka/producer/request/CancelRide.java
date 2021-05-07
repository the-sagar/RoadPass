package com.messaging.kafka.producer.request;

public class CancelRide {
	private String rideId;
	public String getRideId() {
		return rideId;
	}
	public void setRideId(String rideId) {
		this.rideId = rideId;
	}
}
