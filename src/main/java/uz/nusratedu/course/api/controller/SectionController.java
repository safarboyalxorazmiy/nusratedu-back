package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.domain.service.ISectionService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/section")
@RequiredArgsConstructor
public class SectionController {
    private final ISectionService service;

    @PostMapping("/create")
    public ResponseEntity<Mono<SectionResponse>> create(
            @RequestBody SectionCreateRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/get")
    public ResponseEntity<Flux<SectionResponse>> getByCourseId(
            @RequestParam UUID courseId
    ) {
        return ResponseEntity.ok(service.getByCourseId(courseId));
    }
}