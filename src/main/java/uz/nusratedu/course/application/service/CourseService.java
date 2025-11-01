package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.application.mapper.CourseMapper;
import uz.nusratedu.course.domain.service.ICourseService;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.course.infrastructure.repository.CourseRepository;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.user.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    @Override
    public Mono<CourseResponse> create(CourseCreateRequest dto) {
        CourseEntity entity = courseMapper.toEntity(dto);

        return courseRepository.save(entity)
                .map(courseMapper::toResponse);
    }

    @Override
    public Flux<CourseResponse> getAllCourses(User user) {
        return courseRepository.findAll()
                .flatMap(course ->
                        coursePurchaseHistoryRepository
                                .findByUserIdAndCourseId(user.getTelegramId(), course.getId().toString())
                                .map(coursePurchase -> {
                                    CourseResponse resp = courseMapper.toResponse(course);
                                    resp.setPurchased(true);
                                    resp.setPurchasedAt(coursePurchase.getPurchasedAt());
                                    return resp;
                                })
                                .defaultIfEmpty(courseMapper.toResponse(course))
                                .map(resp -> {
                                    if (resp.getPurchased() == null) {
                                        resp.setPurchased(false);
                                        resp.setPurchasedAt(null);
                                    }
                                    return resp;
                                })
                );
    }


    @Override
    public Flux<CourseResponse> getPurchasedCourses(User user) {
        return coursePurchaseHistoryRepository.findByUserId(user.getTelegramId())
                .flatMap(coursePurchase -> {
                    UUID courseId = UUID.fromString(coursePurchase.getCourseId());
                    return courseRepository.findById(courseId)
                            .map(course -> {
                                CourseResponse response = courseMapper.toResponse(course);
                                response.setPurchased(true);
                                response.setPurchasedAt(coursePurchase.getPurchasedAt());
                                return response;
                            });
                });
    }



}