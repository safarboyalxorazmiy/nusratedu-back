package uz.nusratedu.course.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.course.application.dto.SectionCreateRequest;
import uz.nusratedu.course.application.dto.SectionResponse;
import uz.nusratedu.course.infrastructure.entity.SectionEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-27T23:19:02+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9-ea (Debian)"
)
@Component
public class SectionMapperImpl implements SectionMapper {

    @Override
    public SectionResponse toDomain(SectionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SectionResponse sectionResponse = new SectionResponse();

        sectionResponse.setId( entity.getId() );
        sectionResponse.setTitle( entity.getTitle() );
        sectionResponse.setCourseId( entity.getCourseId() );

        return sectionResponse;
    }

    @Override
    public SectionEntity toEntity(SectionCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        SectionEntity.SectionEntityBuilder sectionEntity = SectionEntity.builder();

        sectionEntity.title( dto.getTitle() );
        sectionEntity.courseId( dto.getCourseId() );

        sectionEntity.id( com.datastax.oss.driver.api.core.uuid.Uuids.timeBased() );

        return sectionEntity.build();
    }

    @Override
    public SectionResponse toResponse(SectionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        SectionResponse sectionResponse = new SectionResponse();

        sectionResponse.setId( entity.getId() );
        sectionResponse.setTitle( entity.getTitle() );
        sectionResponse.setCourseId( entity.getCourseId() );

        return sectionResponse;
    }
}
