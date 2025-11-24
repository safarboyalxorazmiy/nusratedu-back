package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.user.User;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: ILessonService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Returns simple objects and List<> for collection operations.
 * Mono<Void> → void
 */
public interface ILessonService {

    // ✅ CHANGED: Mono<LessonResponse> → LessonResponse
    LessonResponse create(LessonCreateRequest dto);

    // ✅ CHANGED: Flux<LessonResponse> → List<LessonResponse>
    List<LessonResponse> getBySectionId(UUID sectionId);

    // ✅ CHANGED: Mono<Void> → void
    void completeLesson(UUID lessonId, User user);

    // ✅ CHANGED: Flux<LessonResponse> → List<LessonResponse>
    List<LessonResponse> getCompletedLessons(User user);
}