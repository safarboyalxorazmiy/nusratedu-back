package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.domain.service.ILessonService;
import uz.nusratedu.user.SecurityUser;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: LessonController from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Simple blocking calls with List<> responses.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final ILessonService service;

    // ✅ CHANGED: ResponseEntity<Mono<>> → ResponseEntity<>
    @PostMapping("/create")
    public ResponseEntity<LessonResponse> create(
            @RequestBody LessonCreateRequest request
    ) {
        log.info("Creating new lesson");
        // ✅ Simple blocking call
        LessonResponse response = service.create(request);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get")
    public ResponseEntity<List<LessonResponse>> getBySectionId(
            @RequestParam UUID sectionId
    ) {
        log.debug("Getting lessons for section: {}", sectionId);
        // ✅ Simple blocking call returning List
        List<LessonResponse> lessons = service.getBySectionId(sectionId);
        return ResponseEntity.ok(lessons);
    }

    // ✅ CHANGED: ResponseEntity<Mono<Void>> → ResponseEntity<Void>
    @PostMapping("/complete/{lessonId}")
    public ResponseEntity<Void> complete(
            @PathVariable UUID lessonId,
            Authentication authentication
    ) {
        // ✅ CHANGED: Cast to SecurityUser
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Marking lesson {} as completed for user: {}", lessonId, user.getTelegramId());
        // ✅ Simple blocking call
        service.completeLesson(lessonId, user);
        return ResponseEntity.status(201).build();
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get/completed")
    public ResponseEntity<List<LessonResponse>> getCompletedLessons(
            Authentication authentication
    ) {
        // ✅ CHANGED: Cast to SecurityUser
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Getting completed lessons for user: {}", user.getTelegramId());
        // ✅ Simple blocking call returning List
        List<LessonResponse> lessons = service.getCompletedLessons(user);
        return ResponseEntity.ok(lessons);
    }
}