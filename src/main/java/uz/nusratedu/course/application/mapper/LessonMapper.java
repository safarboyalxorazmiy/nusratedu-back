package uz.nusratedu.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import uz.nusratedu.course.infrastructure.entity.LessonEntity;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonResponse toDomain(LessonEntity entity);

    @Mapping(target = "id", expression = "java(com.datastax.oss.driver.api.core.uuid.Uuids.timeBased())")
    LessonEntity toEntity(LessonCreateRequest dto);

    LessonResponse toResponse(LessonEntity entity);
}