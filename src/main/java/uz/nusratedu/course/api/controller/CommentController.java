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

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest request) {
        log.info("Creating comment for lesson: {}", request.getLessonId());
        CommentResponse response = commentService.create(request);
        log.debug("Comment created successfully with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CommentResponse>> getByLessonId(@RequestParam UUID lessonId) {
        log.info("Fetching comments for lesson ID: {}", lessonId);
        List<CommentResponse> comments = commentService.getByLessonId(lessonId);
        log.debug("Retrieved {} comments for lesson: {}", comments.size(), lessonId);
        return ResponseEntity.ok(comments);
    }
}