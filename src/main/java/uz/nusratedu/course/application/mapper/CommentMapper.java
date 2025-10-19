package uz.nusratedu.course.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.infrastructure.entity.CommentEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponse toDomain(CommentEntity entity);

    @Mapping(target = "id", expression = "java(com.datastax.oss.driver.api.core.uuid.Uuids.timeBased())")
    CommentEntity toEntity(CommentCreateRequest dto);

    CommentResponse toResponse(CommentEntity entity);
}