package uz.nusratedu.course.application.mapper;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.application.dto.CompletedLessonResponse;
import uz.nusratedu.course.infrastructure.entity.CompletedLessonEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T02:28:35+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class CompletedLessonMapperImpl implements CompletedLessonMapper {

    @Override
    public CompletedLessonEntity toEntity(CompletedLessonCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CompletedLessonEntity.CompletedLessonEntityBuilder completedLessonEntity = CompletedLessonEntity.builder();

        completedLessonEntity.userId( dto.getUserId() );
        if ( dto.getLessonId() != null ) {
            completedLessonEntity.lessonId( UUID.fromString( dto.getLessonId() ) );
        }

        completedLessonEntity.id( com.datastax.oss.driver.api.core.uuid.Uuids.timeBased() );

        return completedLessonEntity.build();
    }

    @Override
    public CompletedLessonResponse toResponse(CompletedLessonEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CompletedLessonResponse completedLessonResponse = new CompletedLessonResponse();

        completedLessonResponse.setId( entity.getId() );
        completedLessonResponse.setUserId( entity.getUserId() );
        if ( entity.getLessonId() != null ) {
            completedLessonResponse.setLessonId( entity.getLessonId().toString() );
        }

        return completedLessonResponse;
    }
}
