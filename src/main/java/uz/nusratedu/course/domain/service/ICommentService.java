package uz.nusratedu.course.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;

import java.util.UUID;

public interface ICommentService {
    Mono<CommentResponse> create(CommentCreateRequest dto);

    Flux<CommentResponse> getByLessonId(UUID lessonId);
}