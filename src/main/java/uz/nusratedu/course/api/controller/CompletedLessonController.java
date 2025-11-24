package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.domain.service.ICompletedLessonService;

/**
 * ✅ CONVERTED: CompletedLessonController from reactive to blocking
 *
 * Removed all Mono<> and reactive patterns.
 * Now returns simple ResponseEntity with blocking calls.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/completed/lesson")
@RequiredArgsConstructor
public class CompletedLessonController {

    private final ICompletedLessonService service;

    // ✅ CHANGED: Returns void directly instead of Mono<Void>
    @PostMapping("/create")
    public ResponseEntity<Void> create(
            @RequestBody CompletedLessonCreateRequest dto
    ) {
        log.info("Marking lesson as completed");
        // ✅ Simple blocking call
        service.create(dto);
        return ResponseEntity.status(201).build();
    }
}