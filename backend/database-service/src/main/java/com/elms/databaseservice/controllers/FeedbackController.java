package com.elms.databaseservice.controllers;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.elms.databaseservice.models.Feedback;
import com.elms.databaseservice.models.StudentCourse;
import com.elms.databaseservice.models.StudentCourseId;
import com.elms.databaseservice.proxy.AuthClient;
import com.elms.databaseservice.repos.FeedbackRepo;
import com.elms.databaseservice.repos.StudentCourseRepo;
import com.elms.databaseservice.services.FeedbackService;

@RestController
public class FeedbackController {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(FeedbackController.class);
	@Autowired
	FeedbackService service;
	@Autowired
	StudentCourseRepo studentCourseRepo;
	@Autowired
	FeedbackRepo repo;
	@Autowired
	AuthClient client;


	@GetMapping(path = "/feedbacks")
	public ResponseEntity<List<Feedback>> fetchAllStudents(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader) throws Exception {
		if(client.authorizeTheRequest(requestTokenHeader))
			return service.getAllFeedbacks();
		else
			return new ResponseEntity<List<Feedback>>(Collections.EMPTY_LIST,HttpStatus.BAD_REQUEST);

	}

	@GetMapping(path = "/course/{courseId}/feedback")
	public ResponseEntity<List<Feedback>> fetchAllFeedbackByCourseId(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,@PathVariable("courseId") int courseId) {
		if(client.authorizeTheRequest(requestTokenHeader))
			return service.getAllFeedbacksByCourseId(courseId);
		else
			return new ResponseEntity<List<Feedback>>(Collections.EMPTY_LIST,HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/student/{studentId}/course/{courseId}/feedback")
	public ResponseEntity<String> isFeedbackPresent(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId) {
		if(client.authorizeTheRequest(requestTokenHeader))
			return service.existFeedbackById(studentId, courseId);
		else
			return new ResponseEntity<>("User not authenticated",HttpStatus.BAD_REQUEST);
	}

//
	@PostMapping(path = "/student/{id}/course/{courseId}/feedback")
	public ResponseEntity<String> storeFeedback(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,@PathVariable("id") int id,
			@PathVariable("courseId") int courseId,
			@RequestBody Feedback feedback) {
		if(client.authorizeTheRequest(requestTokenHeader))
		{
			StudentCourse studentCourse=studentCourseRepo.findById(new StudentCourseId(id,courseId)).get();
			feedback.setStudentCourseId(studentCourse);
			return service.storeFeedback(feedback);
		}
		else
			return new ResponseEntity<>("User not authenticated",HttpStatus.BAD_REQUEST);
		
	}

	@DeleteMapping(path = "/student/{studentId}/course/{courseId}/feedback")
	public ResponseEntity<String> deleteFeedback(@RequestHeader(value = "Authorization", required = true) String requestTokenHeader,@PathVariable("studentId") int studentId,
			@PathVariable("courseId") int courseId) {
		logger.info("feedback savings");
		if(client.authorizeTheRequest(requestTokenHeader))
			return service.deleteFeedback(studentId, courseId);
		else
			return new ResponseEntity<>("User not authenticated",HttpStatus.BAD_REQUEST);
	
	}

}
