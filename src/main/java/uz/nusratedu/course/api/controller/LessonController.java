package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.domain.service.ILessonService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final ILessonService service;

    @PostMapping("/create")
    public ResponseEntity<Mono<LessonResponse>> create(
            @RequestBody LessonCreateRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/get")
    public ResponseEntity<Flux<LessonResponse>> getBySectionId(
            @RequestParam UUID sectionId
    ) {
        return ResponseEntity.ok(service.getBySectionId(sectionId));
    }
}
