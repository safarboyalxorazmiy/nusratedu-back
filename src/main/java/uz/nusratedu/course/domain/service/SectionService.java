package uz.nusratedu.course.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.application.mapper.SectionMapper;
import uz.nusratedu.course.infrastructure.repository.SectionRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService {
    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    @Override
    public Mono<SectionResponse> create(SectionCreateRequest dto) {
        return sectionRepository.save(sectionMapper.toEntity(dto))
                .map(sectionMapper::toResponse);
    }

    @Override
    public Flux<SectionResponse> getByCourseId(UUID courseId) {
        return sectionRepository.findByCourseId(courseId).map(sectionMapper::toResponse);
    }


}
