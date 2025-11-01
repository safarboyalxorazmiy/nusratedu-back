package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.application.mapper.CompletedLessonMapper;
import uz.nusratedu.course.domain.service.ICompletedLessonService;
import uz.nusratedu.course.infrastructure.repository.CompletedLessonRepository;

@Service
@RequiredArgsConstructor
public class CompletedLessonService implements ICompletedLessonService {
    private final CompletedLessonMapper mapper;
    private final CompletedLessonRepository repository;

    @Override
    public void create(CompletedLessonCreateRequest dto) {
        repository.save(mapper.toEntity(dto)).subscribe();
    }
}