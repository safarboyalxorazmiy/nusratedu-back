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

/**
 * ✅ CONVERTED: CourseController from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Simple blocking calls with List<> responses.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService service;

    // ✅ CHANGED: ResponseEntity<Mono<>> → ResponseEntity<>
    @PostMapping("/create")
    public ResponseEntity<CourseResponse> create(
            @RequestBody CourseCreateRequest courseCreateRequest
    ) {
        log.info("Creating new course");
        // ✅ Simple blocking call
        CourseResponse response = service.create(courseCreateRequest);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get/all")
    public ResponseEntity<List<CourseResponse>> getAllCourses(
            Authentication authentication
    ) {
        // ✅ CHANGED: Cast to SecurityUser instead of User
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Getting all courses for user: {}", user.getTelegramId());
        // ✅ Simple blocking call returning List
        List<CourseResponse> courses = service.getAllCourses(user);
        return ResponseEntity.ok(courses);
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get/purchased")
    public ResponseEntity<List<CourseResponse>> getPurchased(
            Authentication authentication
    ) {
        // ✅ CHANGED: Cast to SecurityUser
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Getting purchased courses for user: {}", user.getTelegramId());
        // ✅ Simple blocking call returning List
        List<CourseResponse> courses = service.getPurchasedCourses(user);
        return ResponseEntity.ok(courses);
    }
}