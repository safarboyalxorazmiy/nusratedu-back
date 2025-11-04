package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.application.mapper.SectionMapper;
import uz.nusratedu.course.domain.service.ISectionService;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;
import uz.nusratedu.course.infrastructure.repository.SectionRepository;

import java.util.Comparator;
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
        return sectionRepository.findByCourseId(courseId)
                .sort(Comparator.comparing(SectionEntity::getId))
                .map(sectionMapper::toResponse);
    }


}
