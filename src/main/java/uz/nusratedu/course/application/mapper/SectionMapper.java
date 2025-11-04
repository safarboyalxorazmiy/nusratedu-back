package uz.nusratedu.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;

@Mapper(componentModel = "spring")
public interface SectionMapper {
    SectionResponse toDomain(SectionEntity entity);

    @Mapping(target = "id", expression = "java(com.datastax.oss.driver.api.core.uuid.Uuids.timeBased())")
    SectionEntity toEntity(SectionCreateRequest dto);

    SectionResponse toResponse(SectionEntity entity);
}