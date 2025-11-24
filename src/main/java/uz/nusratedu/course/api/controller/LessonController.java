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

@Slf4j
@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {

    private final ILessonService service;

    @PostMapping("/create")
    public ResponseEntity<LessonResponse> create(@RequestBody LessonCreateRequest request) {
        log.info("Creating new lesson: {} for section: {}", request.getTitle(), request.getSectionId());
        LessonResponse response = service.create(request);
        log.debug("Lesson created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<LessonResponse>> getBySectionId(@RequestParam UUID sectionId) {
        log.info("Fetching lessons for section ID: {}", sectionId);
        List<LessonResponse> lessons = service.getBySectionId(sectionId);
        log.debug("Found {} lessons in section: {}", lessons.size(), sectionId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/complete/{lessonId}")
    public ResponseEntity<Void> complete(@PathVariable UUID lessonId, Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("User {} completing lesson: {}", user.getTelegramId(), lessonId);
        service.completeLesson(lessonId, user);
        log.debug("Lesson {} marked as completed for user: {}", lessonId, user.getTelegramId());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/get/completed")
    public ResponseEntity<List<LessonResponse>> getCompletedLessons(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Retrieving completed lessons for user: {}", user.getTelegramId());
        List<LessonResponse> lessons = service.getCompletedLessons(user);
        log.debug("User {} has completed {} lessons", user.getTelegramId(), lessons.size());
        return ResponseEntity.ok(lessons);
    }
}