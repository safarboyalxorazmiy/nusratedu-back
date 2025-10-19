package uz.nusratedu.course.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.application.mapper.CourseMapper;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.course.infrastructure.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Mono<CourseResponse> create(CourseCreateRequest dto) {
        CourseEntity entity = courseMapper.toEntity(dto);

        return courseRepository.save(entity)
                .map(courseMapper::toResponse);
    }

    @Override
    public Flux<CourseResponse> getAllCourses() {
        return courseRepository.findAll().map(courseMapper::toResponse);
    }


}