package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.application.mapper.SectionMapper;
import uz.nusratedu.course.domain.service.ISectionService;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;
import uz.nusratedu.course.infrastructure.repository.SectionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ✅ CONVERTED: SectionService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Simple blocking operations with stream for sorting.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    // ✅ CHANGED: Returns SectionResponse instead of Mono<SectionResponse>
    @Override
    public SectionResponse create(SectionCreateRequest dto) {
        log.info("Creating section");
        // ✅ Simple blocking save
        var entity = sectionMapper.toEntity(dto);
        var saved = sectionRepository.save(entity);
        return sectionMapper.toResponse(saved);
    }

    // ✅ CHANGED: Returns List<SectionResponse> instead of Flux<SectionResponse>
    @Override
    public List<SectionResponse> getByCourseId(UUID courseId) {
        log.debug("Getting sections for course: {}", courseId);
        // ✅ Simple blocking call with sorting
        return sectionRepository.findByCourseId(courseId)
                .stream()
                .sorted(Comparator.comparing(SectionEntity::getId))
                .map(sectionMapper::toResponse)
                .collect(Collectors.toList());
    }
}