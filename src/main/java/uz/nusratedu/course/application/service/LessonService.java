package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.application.mapper.LessonMapper;
import uz.nusratedu.course.domain.service.ILessonService;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;
import uz.nusratedu.course.infrastructure.entity.LessonEntity;
import uz.nusratedu.course.infrastructure.repository.CompletedLessonRepository;
import uz.nusratedu.course.infrastructure.repository.LessonRepository;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ✅ CONVERTED: LessonService from reactive to blocking
 *
 * All flatMap chains replaced with simple blocking logic.
 * Uses if/else and stream operations instead of reactive operators.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository repository;
    private final LessonMapper mapper;
    private final CompletedLessonRepository completedLessonRepository;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    // ✅ CHANGED: Returns LessonResponse instead of Mono<LessonResponse>
    @Override
    public LessonResponse create(LessonCreateRequest dto) {
        log.info("Creating lesson");
        // ✅ Simple blocking save
        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    // ✅ CHANGED: Returns List<LessonResponse> instead of Flux<LessonResponse>
    @Override
    public List<LessonResponse> getBySectionId(UUID sectionId) {
        log.debug("Getting lessons for section: {}", sectionId);
        // ✅ Simple blocking call
        return repository.findBySectionId(sectionId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // ✅ CHANGED: Returns void instead of Mono<Void>
    @Override
    public void completeLesson(UUID lessonId, User user) {
        log.info("Completing lesson {} for user: {}", lessonId, user.getTelegramId());
        // ✅ Simple blocking save
        CompletedLessonEntity completedLesson = new CompletedLessonEntity();
        completedLesson.setId(lessonId);
        completedLesson.setUserId(user.getTelegramId());
        completedLessonRepository.save(completedLesson);
        log.debug("Lesson completed");
    }

    // ✅ CHANGED: Returns List<LessonResponse> instead of Flux<LessonResponse>
    @Override
    public List<LessonResponse> getCompletedLessons(User user) {
        log.debug("Getting completed lessons for user: {}", user.getTelegramId());
        // ✅ Simple blocking call - no flatMap chain

        return completedLessonRepository.findByUserId(user.getTelegramId())
                .stream()
                .map(completedLessonEntity -> {
                    // ✅ Simple blocking lookup
                    Optional<?> lesson = repository.findById(completedLessonEntity.getLessonId());

                    // ✅ Simple if/else to handle optional
                    if (lesson.isPresent()) {
                        return mapper.toResponse((LessonEntity) lesson.get());
                    }
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }
}