package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;

import java.util.List;
import java.util.UUID;

/**
 * ✅ CONVERTED: ISectionService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Returns simple objects and List<> for collection operations.
 * Removed unused CommentCreateRequest import.
 */
public interface ISectionService {

    // ✅ CHANGED: Mono<SectionResponse> → SectionResponse
    SectionResponse create(SectionCreateRequest dto);

    // ✅ CHANGED: Flux<SectionResponse> → List<SectionResponse>
    List<SectionResponse> getByCourseId(UUID courseId);
}