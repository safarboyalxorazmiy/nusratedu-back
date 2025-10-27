package uz.nusratedu.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;
import com.datastax.oss.driver.api.core.uuid.Uuids;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "id", expression = "java(com.datastax.oss.driver.api.core.uuid.Uuids.timeBased())")
    CourseEntity toEntity(CourseCreateRequest dto);

    @Mapping(target = "purchased", ignore = true)
    @Mapping(target = "purchasedAt", ignore = true)
    CourseResponse toResponse(CourseEntity entity);
}