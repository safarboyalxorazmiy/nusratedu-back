package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.domain.service.ICourseService;
import uz.nusratedu.user.User;

@Slf4j
@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final ICourseService service;

    @PostMapping("/create")
    public ResponseEntity<Mono<CourseResponse>> create(
            @RequestBody CourseCreateRequest courseCreateRequest
    ) {
        return ResponseEntity.ok(service.create(courseCreateRequest));
    }

    @GetMapping("/get/all")
    public ResponseEntity<Flux<CourseResponse>> getAllCourses(
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();

        log.info("/get/all, Request: ");
        return ResponseEntity.ok(service.getAllCourses(user));
    }

    @GetMapping("/get/purchased")
    public ResponseEntity<Flux<CourseResponse>> getPurchased(
            Authentication authentication
    ) {
        var user = (User) authentication.getPrincipal();
        log.info("/get/purchased, Request: ");

        return ResponseEntity.ok(service.getPurchasedCourses(user));
    }
}