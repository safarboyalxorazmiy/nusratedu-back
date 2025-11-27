package uz.nusratedu.test.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.test.application.dto.TestCreateRequest;
import uz.nusratedu.test.application.dto.TestResponse;
import uz.nusratedu.test.infastructure.entity.TestEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T20:03:00+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class TestMapperImpl implements TestMapper {

    @Override
    public TestEntity toEntity(TestCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TestEntity.TestEntityBuilder testEntity = TestEntity.builder();

        testEntity.lessonId( dto.getLessonId() );
        testEntity.title( dto.getTitle() );
        testEntity.description( dto.getDescription() );

        return testEntity.build();
    }

    @Override
    public TestResponse toResponse(TestEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TestResponse.TestResponseBuilder testResponse = TestResponse.builder();

        testResponse.id( entity.getId() );
        testResponse.lessonId( entity.getLessonId() );
        testResponse.title( entity.getTitle() );
        testResponse.description( entity.getDescription() );
        testResponse.totalQuestions( entity.getTotalQuestions() );
        testResponse.passingScore( entity.getPassingScore() );
        testResponse.maxScore( entity.getMaxScore() );
        testResponse.createdAt( entity.getCreatedAt() );

        return testResponse.build();
    }
}
