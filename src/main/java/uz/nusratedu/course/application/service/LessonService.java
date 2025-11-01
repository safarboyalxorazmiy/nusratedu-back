package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.application.mapper.LessonMapper;
import uz.nusratedu.course.domain.service.ILessonService;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;
import uz.nusratedu.course.infrastructure.repository.CompletedLessonRepository;
import uz.nusratedu.course.infrastructure.repository.LessonRepository;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.user.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LessonService implements ILessonService {
    private final LessonRepository repository;
    private final LessonMapper mapper;
    private final CompletedLessonRepository completedLessonRepository;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;
    private final LessonRepository lessonRepository;

    @Override
    public Mono<LessonResponse> create(LessonCreateRequest dto) {
        return repository.save(mapper.toEntity(dto)).map(mapper::toResponse);
    }

    @Override
    public Flux<LessonResponse> getBySectionId(UUID sectionId) {
        return repository.findBySectionId(sectionId).map(mapper::toResponse);
    }

    @Override
    public Mono<Void> completeLesson(UUID lessonId, User user) {
        CompletedLessonEntity completedLesson = new CompletedLessonEntity();
        completedLesson.setId(lessonId);
        completedLesson.setUserId(user.getTelegramId());
        return completedLessonRepository.save(completedLesson).then();
    }

    @Override
    public Flux<LessonResponse> getCompletedLessons(User user) {
        return completedLessonRepository.findByUserId(user.getTelegramId())
                .flatMap(completedLessonEntity ->
                        lessonRepository.findById(completedLessonEntity.getLessonId())
                                .map(mapper::toResponse)
                );
    }
}