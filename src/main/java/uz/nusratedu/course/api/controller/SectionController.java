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

@Slf4j
@RestController
@RequestMapping("/api/v1/section")
@RequiredArgsConstructor
public class SectionController {

    private final ISectionService service;

    @PostMapping("/create")
    public ResponseEntity<SectionResponse> create(@RequestBody SectionCreateRequest request) {
        log.info("Creating new section: {} for course: {}", request.getTitle(), request.getCourseId());
        SectionResponse response = service.create(request);
        log.debug("Section created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<SectionResponse>> getByCourseId(@RequestParam UUID courseId) {
        log.info("Fetching sections for course ID: {}", courseId);
        List<SectionResponse> sections = service.getByCourseId(courseId);
        log.debug("Found {} sections in course: {}", sections.size(), courseId);
        return ResponseEntity.ok(sections);
    }
}