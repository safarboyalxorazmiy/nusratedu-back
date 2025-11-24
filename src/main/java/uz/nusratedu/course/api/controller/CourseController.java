package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.domain.service.ICourseService;
import uz.nusratedu.user.SecurityUser;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService service;

    @PostMapping("/create")
    public ResponseEntity<CourseResponse> create(@RequestBody CourseCreateRequest courseCreateRequest) {
        log.info("Creating new course: {}", courseCreateRequest.getCourseName());
        CourseResponse response = service.create(courseCreateRequest);
        log.debug("Course created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<CourseResponse>> getAllCourses(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Fetching all courses for user: {}", user.getTelegramId());
        List<CourseResponse> courses = service.getAllCourses(user);
        log.debug("Found {} total courses for user: {}", courses.size(), user.getTelegramId());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/get/purchased")
    public ResponseEntity<List<CourseResponse>> getPurchased(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Fetching purchased courses for user: {}", user.getTelegramId());
        List<CourseResponse> courses = service.getPurchasedCourses(user);
        log.debug("User {} has purchased {} courses", user.getTelegramId(), courses.size());
        return ResponseEntity.ok(courses);
    }
}