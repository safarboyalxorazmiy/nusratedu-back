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

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionService implements ISectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;

    @Override
    public SectionResponse create(SectionCreateRequest dto) {
        log.info("Creating section: {} for course: {}", dto.getTitle(), dto.getCourseId());
        var entity = sectionMapper.toEntity(dto);
        var saved = sectionRepository.save(entity);
        log.debug("Section created with ID: {}", saved.getId());
        return sectionMapper.toResponse(saved);
    }

    @Override
    public List<SectionResponse> getByCourseId(UUID courseId) {
        log.debug("Retrieving sections for course: {}", courseId);
        var sections = sectionRepository.findByCourseId(courseId)
                .stream()
                .sorted(Comparator.comparing(SectionEntity::getId))
                .map(sectionMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} sections for course: {}", sections.size(), courseId);
        return sections;
    }
}