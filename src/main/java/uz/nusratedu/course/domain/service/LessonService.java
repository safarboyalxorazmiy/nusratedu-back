package uz.nusratedu.course.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.application.mapper.LessonMapper;
import uz.nusratedu.course.infrastructure.repository.LessonRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {
    private final LessonRepository repository;
    private final LessonMapper mapper;

    @Override
    public Mono<LessonResponse> create(LessonCreateRequest dto) {
        return repository.save(mapper.toEntity(dto)).map(mapper::toResponse);
    }

    @Override
    public Flux<LessonResponse> getBySectionId(UUID sectionId) {
        return repository.findBySectionId(sectionId).map(mapper::toResponse);
    }
}