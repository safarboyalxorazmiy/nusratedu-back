package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.domain.service.ICommentService;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: CommentController from reactive to blocking
 *
 * All Flux<> and Mono<> removed.
 * Replaced with simple List<> and object returns.
 * Virtual threads handle concurrency efficiently.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    // ✅ CHANGED: ResponseEntity<Mono<>> → ResponseEntity<>
    @PostMapping("/create")
    public ResponseEntity<CommentResponse> createComment(
            @RequestBody CommentCreateRequest request
    ) {
        log.info("Creating comment");
        // ✅ Simple blocking call
        CommentResponse response = commentService.create(request);
        return ResponseEntity.status(201).body(response);
    }

    // ✅ CHANGED: ResponseEntity<Flux<>> → ResponseEntity<List<>>
    @GetMapping("/get")
    public ResponseEntity<List<CommentResponse>> getByLessonId(
            @RequestParam UUID lessonId
    ) {
        log.debug("Getting comments for lesson: {}", lessonId);
        // ✅ Simple blocking call returning List
        List<CommentResponse> comments = commentService.getByLessonId(lessonId);
        return ResponseEntity.ok(comments);
    }
}