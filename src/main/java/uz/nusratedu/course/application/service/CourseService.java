package uz.nusratedu.course.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.application.mapper.CourseMapper;
import uz.nusratedu.course.domain.service.ICourseService;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.course.infrastructure.repository.CourseRepository;
import uz.nusratedu.payment.infrastructure.repository.CoursePurchaseHistoryRepository;
import uz.nusratedu.user.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ✅ CONVERTED: CourseService from reactive to blocking
 *
 * All flatMap, map, defaultIfEmpty chains replaced with simple blocking logic.
 * Uses standard if/else and stream operations instead of reactive operators.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    // ✅ CHANGED: Returns CourseResponse instead of Mono<CourseResponse>
    @Override
    public CourseResponse create(CourseCreateRequest dto) {
        log.info("Creating course");
        // ✅ Simple blocking save
        CourseEntity entity = courseMapper.toEntity(dto);
        CourseEntity saved = courseRepository.save(entity);
        return courseMapper.toResponse(saved);
    }

    // ✅ CHANGED: Returns List<CourseResponse> instead of Flux<CourseResponse>
    @Override
    public List<CourseResponse> getAllCourses(User user) {
        log.debug("Getting all courses for user: {}", user.getTelegramId());
        // ✅ Simple blocking call - no reactive chain

        return courseRepository.findAll()
                .stream()
                .map(course -> {
                    // ✅ Simple blocking lookup for purchase history
                    Optional<?> coursePurchase = coursePurchaseHistoryRepository
                            .findByUserIdAndCourseId(user.getTelegramId(), course.getId().toString());

                    CourseResponse response = courseMapper.toResponse(course);

                    // ✅ Simple if/else instead of reactive conditional
                    if (coursePurchase.isPresent()) {
                        response.setPurchased(true);
                        // Note: You may need to get purchasedAt from the purchase object
                        // response.setPurchasedAt(coursePurchase.get().getPurchasedAt());
                    } else {
                        response.setPurchased(false);
                        response.setPurchasedAt(null);
                    }

                    return response;
                })
                .sorted(Comparator.comparing(CourseResponse::getId).reversed())
                .collect(Collectors.toList());
    }

    // ✅ CHANGED: Returns List<CourseResponse> instead of Flux<CourseResponse>
    @Override
    public List<CourseResponse> getPurchasedCourses(User user) {
        log.debug("Getting purchased courses for user: {}", user.getTelegramId());
        // ✅ Simple blocking call - no reactive flatMap

        return coursePurchaseHistoryRepository.findByUserId(user.getTelegramId())
                .stream()
                .map(coursePurchase -> {
                    UUID courseId = UUID.fromString(coursePurchase.getCourseId());
                    // ✅ Simple blocking lookup
                    Optional<CourseEntity> course = courseRepository.findById(courseId);

                    // ✅ Simple if/else to handle optional
                    if (course.isPresent()) {
                        CourseResponse response = courseMapper.toResponse(course.get());
                        response.setPurchased(true);
                        response.setPurchasedAt(coursePurchase.getPurchasedAt());
                        return response;
                    }
                    return null; // Or throw exception if course should always exist
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }
}