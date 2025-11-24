package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.user.User;

import java.util.List;

/**
 * ✅ CONVERTED: ICourseService from reactive to blocking
 *
 * All Mono<> and Flux<> removed.
 * Returns simple objects and List<> for collection operations.
 */
public interface ICourseService {

    // ✅ CHANGED: Mono<CourseResponse> → CourseResponse
    CourseResponse create(CourseCreateRequest dto);

    // ✅ CHANGED: Flux<CourseResponse> → List<CourseResponse>
    List<CourseResponse> getAllCourses(User user);

    // ✅ CHANGED: Flux<CourseResponse> → List<CourseResponse>
    List<CourseResponse> getPurchasedCourses(User user);
}