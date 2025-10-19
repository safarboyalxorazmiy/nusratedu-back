package uz.nusratedu.course.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.course.application.dto.LessonCreateRequest;
import uz.nusratedu.course.application.dto.LessonResponse;
import uz.nusratedu.course.infrastructure.entity.LessonEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-19T20:13:35+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9-ea (Debian)"
)
@Component
public class LessonMapperImpl implements LessonMapper {

    @Override
    public LessonResponse toDomain(LessonEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LessonResponse lessonResponse = new LessonResponse();

        lessonResponse.setId( entity.getId() );
        lessonResponse.setTitle( entity.getTitle() );
        lessonResponse.setVimeoUrl( entity.getVimeoUrl() );
        lessonResponse.setPdfUrl( entity.getPdfUrl() );
        lessonResponse.setSectionId( entity.getSectionId() );

        return lessonResponse;
    }

    @Override
    public LessonEntity toEntity(LessonCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        LessonEntity.LessonEntityBuilder lessonEntity = LessonEntity.builder();

        lessonEntity.title( dto.getTitle() );
        lessonEntity.vimeoUrl( dto.getVimeoUrl() );
        lessonEntity.pdfUrl( dto.getPdfUrl() );
        lessonEntity.sectionId( dto.getSectionId() );

        lessonEntity.id( com.datastax.oss.driver.api.core.uuid.Uuids.timeBased() );

        return lessonEntity.build();
    }

    @Override
    public LessonResponse toResponse(LessonEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LessonResponse lessonResponse = new LessonResponse();

        lessonResponse.setId( entity.getId() );
        lessonResponse.setTitle( entity.getTitle() );
        lessonResponse.setVimeoUrl( entity.getVimeoUrl() );
        lessonResponse.setPdfUrl( entity.getPdfUrl() );
        lessonResponse.setSectionId( entity.getSectionId() );

        return lessonResponse;
    }
}
