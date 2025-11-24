package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.application.mapper.CompletedLessonMapper;
import uz.nusratedu.course.domain.service.ICompletedLessonService;
import uz.nusratedu.course.infrastructure.repository.CompletedLessonRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompletedLessonService implements ICompletedLessonService {

    private final CompletedLessonMapper mapper;
    private final CompletedLessonRepository repository;

    @Override
    public void create(CompletedLessonCreateRequest dto) {
        log.debug("Processing lesson completion request for lesson: {} by user: {}",
                dto.getLessonId(), dto.getUserId());
        var entity = mapper.toEntity(dto);
        repository.save(entity);
        log.info("Lesson {} marked as completed for user: {}", dto.getLessonId(), dto.getUserId());
    }
}