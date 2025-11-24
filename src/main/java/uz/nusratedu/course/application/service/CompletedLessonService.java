package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.application.mapper.CompletedLessonMapper;
import uz.nusratedu.course.domain.service.ICompletedLessonService;
import uz.nusratedu.course.infrastructure.repository.CompletedLessonRepository;

/**
 * ✅ CONVERTED: CompletedLessonService from reactive to blocking
 *
 * Removed .subscribe() call.
 * Now uses simple blocking repository save.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompletedLessonService implements ICompletedLessonService {

    private final CompletedLessonMapper mapper;
    private final CompletedLessonRepository repository;

    // ✅ CHANGED: Removed .subscribe() from the end
    @Override
    public void create(CompletedLessonCreateRequest dto) {
        log.debug("Marking lesson as completed");
        // ✅ Simple blocking save - no .subscribe() needed
        var entity = mapper.toEntity(dto);
        repository.save(entity);
        log.debug("Lesson marked as completed");
    }
}