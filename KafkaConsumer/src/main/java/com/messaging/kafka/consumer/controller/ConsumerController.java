package com.messaging.kafka.consumer.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.messaging.kafka.consumer.model.Driver;
import com.messaging.kafka.consumer.model.Journey;
import com.messaging.kafka.consumer.repository.JourneyRepository;
import com.messaging.kafka.consumer.response.ApiResponse;
import com.messaging.kafka.consumer.utility.JBSUtility;
import com.messaging.kafka.consumer.request.BookRide;

@Service
@CrossOrigin
@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

	public BookRide bookRideRequest;
	
    @Autowired
    JourneyRepository journeyRepository;
	
	@KafkaListener(topics = "BOOK_TOPIC", groupId = "001")
	public ResponseEntity<ApiResponse> consumer(BookRide request) {
		SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat request_time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSSSSS");
		this.bookRideRequest = request;
		try{
            List<Journey> allJourneysForUser = journeyRepository.findByUsername(request.getUsername());
            if(allJourneysForUser.isEmpty()){
            	Date date = simpleformat.parse(request.getJourney_date_time());
            	Date request_date_time = request_time.parse(request.getTimeOfRequest());
                Journey newJourney = journeyRepository.save(new Journey(request.getUsername(), request.getStarting_point(), request.getEnding_point(), date, request_date_time, JBSUtility.JOURNEY_BOOKED_STATUS));
                return ResponseEntity.ok(new ApiResponse(0, JBSUtility.JOURNEY_BOOK_SUCCESS));
            }else {
                List<Date> bookedSlots = new ArrayList<>();
                for(Journey bookedJourney : allJourneysForUser){
                    if(bookedJourney.getStatus().equalsIgnoreCase(JBSUtility.JOURNEY_BOOKED_STATUS)){
                        bookedSlots.add(bookedJourney.getJourney_date_time());
                    }
                }
                if(bookedSlots.contains(request.getJourney_date_time())){
                    return ResponseEntity.ok(new ApiResponse(1, JBSUtility.JOURNEY_BOOK_FAILURE_SLOT_BOOKED));
                }else{
                	Date date = simpleformat.parse(request.getJourney_date_time());
                	Date request_date_time = request_time.parse(request.getTimeOfRequest());
                    Journey newJourney = journeyRepository.save(new Journey(request.getUsername(), request.getStarting_point(), request.getEnding_point(), date, request_date_time, JBSUtility.JOURNEY_BOOKED_STATUS));
                    return ResponseEntity.ok(new ApiResponse(0, JBSUtility.JOURNEY_BOOK_SUCCESS));
                }
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(-1, JBSUtility.JBS_SERVER_ERROR));
        }
	}
	
	@KafkaListener(topics = "CANCEL_TOPIC", groupId = "001")
    public ResponseEntity<ApiResponse> cancelJourney(BookRide request) {
        try{
            Optional<Journey> journeyData = journeyRepository.findById(request.getUsername());
            if(journeyData.isPresent()){
                Journey updatedJourney = journeyData.get();
                updatedJourney.setStatus(JBSUtility.JOURNEY_CANCELLED_STATUS);
                journeyRepository.save(updatedJourney);
                return ResponseEntity.ok(new ApiResponse(0, JBSUtility.JOURNEY_CANCEL_SUCCESS));
            }else{
                return ResponseEntity.ok(new ApiResponse(1, JBSUtility.JOURNEY_CANCEL_FAILURE_NON_EXIST));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse(-1, JBSUtility.JBS_SERVER_ERROR));
        }
    }
	
	@PostMapping("/getJourneys")
    public ResponseEntity<List<Journey>> getJourneys(@RequestBody Driver driver){
        try {
            if(driver.getUsername()!=null){
                String username = driver.getUsername();
                List<Journey> tmp = journeyRepository.findByUsername(username);
                List<Journey> allJourneys = journeyRepository.findByUsername(username)
                        .stream()
                        .filter(j -> j.getStatus().equals(JBSUtility.JOURNEY_BOOKED_STATUS))
                        .collect(Collectors.toList());
                return new ResponseEntity<>(allJourneys, HttpStatus.OK);
            }else{
                throw new Exception();
            }
        }catch (Exception e){
            return new ResponseEntity(JBSUtility.JBS_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}
