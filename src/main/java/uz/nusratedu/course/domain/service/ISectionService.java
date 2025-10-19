package uz.nusratedu.course.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;

import java.util.UUID;

public interface ISectionService {
    Mono<SectionResponse> create(SectionCreateRequest dto);

    Flux<SectionResponse> getByCourseId(UUID courseId);
}