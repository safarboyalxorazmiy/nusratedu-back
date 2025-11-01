package uz.nusratedu.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.application.dto.CompletedLessonResponse;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;

@Mapper(componentModel = "spring")
public interface CompletedLessonMapper {
    @Mapping(target = "id", expression = "java(com.datastax.oss.driver.api.core.uuid.Uuids.timeBased())")
    CompletedLessonEntity toEntity(CompletedLessonCreateRequest dto);

    CompletedLessonResponse toResponse(CompletedLessonEntity entity);
}