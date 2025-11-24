package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: ICommentService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Returns simple objects and List<> for collection operations.
 */
public interface ICommentService {
    CommentResponse create(CommentCreateRequest dto);

    List<CommentResponse> getByLessonId(UUID lessonId);
}