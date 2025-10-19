package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.domain.service.ICommentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<Mono<CommentResponse>> createComment(
            @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.ok(commentService.create(request));
    }

    @GetMapping("/get")
    public ResponseEntity<Flux<CommentResponse>> getByLessonId(
            @RequestParam UUID lessonId
    ) {
        return ResponseEntity.ok(commentService.getByLessonId(lessonId));
    }
}