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

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {

    private final LessonRepository repository;
    private final LessonMapper mapper;
    private final CompletedLessonRepository completedLessonRepository;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    @Override
    public LessonResponse create(LessonCreateRequest dto) {
        log.info("Creating lesson: {} for section: {}", dto.getTitle(), dto.getSectionId());
        var entity = mapper.toEntity(dto);
        var saved = repository.save(entity);
        log.debug("Lesson created with ID: {}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    public List<LessonResponse> getBySectionId(UUID sectionId) {
        log.debug("Retrieving lessons for section: {}", sectionId);
        var lessons = repository.findBySectionId(sectionId)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} lessons in section: {}", lessons.size(), sectionId);
        return lessons;
    }

    @Override
    public void completeLesson(UUID lessonId, User user) {
        log.info("Processing lesson completion - Lesson: {}, User: {}", lessonId, user.getTelegramId());
        CompletedLessonEntity completedLesson = new CompletedLessonEntity();
        completedLesson.setId(lessonId);
        completedLesson.setUserId(user.getTelegramId());
        completedLessonRepository.save(completedLesson);
        log.debug("Lesson {} marked as completed for user: {}", lessonId, user.getTelegramId());
    }

    @Override
    public List<LessonResponse> getCompletedLessons(User user) {
        log.info("Fetching completed lessons for user: {}", user.getTelegramId());

        return completedLessonRepository.findByUserId(user.getTelegramId())
                .stream()
                .map(completedLessonEntity -> {
                    log.trace("Looking up lesson details for ID: {}", completedLessonEntity.getLessonId());
                    Optional<?> lesson = repository.findById(completedLessonEntity.getLessonId());

                    if (lesson.isPresent()) {
                        return mapper.toResponse((LessonEntity) lesson.get());
                    }
                    log.warn("Lesson not found for completed lesson ID: {}", completedLessonEntity.getLessonId());
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }
}