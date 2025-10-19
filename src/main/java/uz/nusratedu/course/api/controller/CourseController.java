package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.domain.service.ICourseService;

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
    public ResponseEntity<Flux<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(service.getAllCourses());
    }
}