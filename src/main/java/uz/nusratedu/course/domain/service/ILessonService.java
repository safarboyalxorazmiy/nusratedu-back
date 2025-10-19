package uz.nusratedu.course.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;

import java.util.UUID;

public interface ILessonService {
    Mono<LessonResponse> create(LessonCreateRequest dto);

    Flux<LessonResponse> getBySectionId(UUID sectionId);
}