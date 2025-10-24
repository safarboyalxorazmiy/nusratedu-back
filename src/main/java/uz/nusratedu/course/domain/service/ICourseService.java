package uz.nusratedu.course.domain.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.user.User;

public interface ICourseService {
    Mono<CourseResponse> create(CourseCreateRequest dto);

    Flux<CourseResponse> getAllCourses(User user);

    Flux<CourseResponse> getPurchasedCourses(User user);
}