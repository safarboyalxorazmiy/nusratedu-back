package uz.nusratedu.course.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.course.application.dto.CourseCreateRequest;
import uz.nusratedu.course.application.dto.CourseResponse;
import uz.nusratedu.course.infrastructure.entity.CourseEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-05T02:47:11+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9-ea (Debian)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseEntity toEntity(CourseCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        CourseEntity.CourseEntityBuilder courseEntity = CourseEntity.builder();

        courseEntity.courseName( dto.getCourseName() );
        courseEntity.courseDescription( dto.getCourseDescription() );
        courseEntity.courseStatus( dto.getCourseStatus() );
        courseEntity.courseField( dto.getCourseField() );
        courseEntity.courseAttachUrl( dto.getCourseAttachUrl() );
        courseEntity.teacher( dto.getTeacher() );
        courseEntity.courseDescriptionLg( dto.getCourseDescriptionLg() );

        courseEntity.id( com.datastax.oss.driver.api.core.uuid.Uuids.timeBased() );

        return courseEntity.build();
    }

    @Override
    public CourseResponse toResponse(CourseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CourseResponse courseResponse = new CourseResponse();

        courseResponse.setId( entity.getId() );
        courseResponse.setCourseName( entity.getCourseName() );
        courseResponse.setCourseDescription( entity.getCourseDescription() );
        courseResponse.setCourseDescriptionLg( entity.getCourseDescriptionLg() );
        courseResponse.setCourseStatus( entity.getCourseStatus() );
        courseResponse.setCourseField( entity.getCourseField() );
        courseResponse.setCourseAttachUrl( entity.getCourseAttachUrl() );
        courseResponse.setTeacher( entity.getTeacher() );

        return courseResponse;
    }
}
