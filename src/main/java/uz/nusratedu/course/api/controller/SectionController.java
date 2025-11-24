package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.domain.service.ISectionService;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: SectionController from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Simple blocking calls with List<> responses.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/section")
@RequiredArgsConstructor
public class SectionController {

    private final ISectionService service;

    // ✅ CHANGED: ResponseEntity<Mono<>> → ResponseEntity<>
    @PostMapping("/create")
    public ResponseEntity<SectionResponse> create(
            @RequestBody SectionCreateRequest request
    ) {
        log.info("Creating new section");
        // ✅ Simple blocking call
        SectionResponse response = service.create(request);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get")
    public ResponseEntity<List<SectionResponse>> getByCourseId(
            @RequestParam UUID courseId
    ) {
        log.debug("Getting sections for course: {}", courseId);
        // ✅ Simple blocking call returning List
        List<SectionResponse> sections = service.getByCourseId(courseId);
        return ResponseEntity.ok(sections);
    }
}