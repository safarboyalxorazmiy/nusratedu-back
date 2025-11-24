package uz.nusratedu.course.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.course.application.dto.CommentCreateRequest;
import uz.nusratedu.course.application.dto.CommentResponse;
import uz.nusratedu.course.infrastructure.entity.CommentEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T01:12:59+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponse toDomain(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setId( entity.getId() );
        commentResponse.setContent( entity.getContent() );
        commentResponse.setLessonId( entity.getLessonId() );

        return commentResponse;
    }

    @Override
    public CommentEntity toEntity(CommentCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CommentEntity.CommentEntityBuilder commentEntity = CommentEntity.builder();

        commentEntity.content( dto.getContent() );
        commentEntity.lessonId( dto.getLessonId() );

        commentEntity.id( com.datastax.oss.driver.api.core.uuid.Uuids.timeBased() );

        return commentEntity.build();
    }

    @Override
    public CommentResponse toResponse(CommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommentResponse commentResponse = new CommentResponse();

        commentResponse.setId( entity.getId() );
        commentResponse.setContent( entity.getContent() );
        commentResponse.setLessonId( entity.getLessonId() );

        return commentResponse;
    }
}
