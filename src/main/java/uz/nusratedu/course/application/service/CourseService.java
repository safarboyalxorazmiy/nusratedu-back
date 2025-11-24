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

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService implements ICourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CoursePurchaseHistoryRepository coursePurchaseHistoryRepository;

    @Override
    public CourseResponse create(CourseCreateRequest dto) {
        log.info("Initiating course creation for: {}", dto.getCourseName());
        CourseEntity entity = courseMapper.toEntity(dto);
        CourseEntity saved = courseRepository.save(entity);
        log.debug("Course persisted with ID: {}", saved.getId());
        return courseMapper.toResponse(saved);
    }

    @Override
    public List<CourseResponse> getAllCourses(User user) {
        log.debug("Fetching all courses with purchase status for user: {}", user.getTelegramId());

        return courseRepository.findAll()
                .stream()
                .map(course -> {
                    log.trace("Processing course: {} for user: {}", course.getId(), user.getTelegramId());
                    Optional<?> coursePurchase = coursePurchaseHistoryRepository
                            .findByUserIdAndCourseId(user.getTelegramId(), course.getId().toString());

                    CourseResponse response = courseMapper.toResponse(course);

                    if (coursePurchase.isPresent()) {
                        response.setPurchased(true);
                        log.trace("Course {} marked as purchased", course.getId());
                    } else {
                        response.setPurchased(false);
                        response.setPurchasedAt(null);
                    }

                    return response;
                })
                .sorted(Comparator.comparing(CourseResponse::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getPurchasedCourses(User user) {
        log.info("Retrieving purchased courses for user: {}", user.getTelegramId());

        return coursePurchaseHistoryRepository.findByUserId(user.getTelegramId())
                .stream()
                .map(coursePurchase -> {
                    UUID courseId = UUID.fromString(coursePurchase.getCourseId());
                    log.trace("Looking up course details for ID: {}", courseId);
                    Optional<CourseEntity> course = courseRepository.findById(courseId);

                    if (course.isPresent()) {
                        CourseResponse response = courseMapper.toResponse(course.get());
                        response.setPurchased(true);
                        response.setPurchasedAt(coursePurchase.getPurchasedAt());
                        return response;
                    }
                    log.warn("Course not found for ID: {} in purchase history", courseId);
                    return null;
                })
                .filter(response -> response != null)
                .collect(Collectors.toList());
    }
}