package uz.nusratedu.test.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.test.application.dto.TestQuestionCreateRequest;
import uz.nusratedu.test.application.dto.TestQuestionResponse;
import uz.nusratedu.test.application.dto.TestQuestionUpdateRequest;
import uz.nusratedu.test.infastructure.entity.TestQuestionEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T02:28:35+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class TestQuestionMapperImpl implements TestQuestionMapper {

    @Override
    public TestQuestionEntity toEntity(TestQuestionCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TestQuestionEntity.TestQuestionEntityBuilder testQuestionEntity = TestQuestionEntity.builder();

        testQuestionEntity.lessonId( dto.getLessonId() );
        testQuestionEntity.questionText( dto.getQuestionText() );
        testQuestionEntity.questionType( dto.getQuestionType() );
        testQuestionEntity.pointValue( dto.getPointValue() );
        testQuestionEntity.orderIndex( dto.getOrderIndex() );
        testQuestionEntity.testId( dto.getTestId() );

        return testQuestionEntity.build();
    }

    @Override
    public TestQuestionResponse toResponse(TestQuestionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TestQuestionResponse.TestQuestionResponseBuilder testQuestionResponse = TestQuestionResponse.builder();

        testQuestionResponse.id( entity.getId() );
        testQuestionResponse.testId( entity.getTestId() );
        testQuestionResponse.lessonId( entity.getLessonId() );
        testQuestionResponse.questionText( entity.getQuestionText() );
        testQuestionResponse.questionType( entity.getQuestionType() );
        testQuestionResponse.pointValue( entity.getPointValue() );
        testQuestionResponse.orderIndex( entity.getOrderIndex() );

        return testQuestionResponse.build();
    }

    @Override
    public void updateEntity(TestQuestionUpdateRequest dto, TestQuestionEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getQuestionText() != null ) {
            entity.setQuestionText( dto.getQuestionText() );
        }
        if ( dto.getQuestionType() != null ) {
            entity.setQuestionType( dto.getQuestionType() );
        }
        if ( dto.getPointValue() != null ) {
            entity.setPointValue( dto.getPointValue() );
        }
        if ( dto.getOrderIndex() != null ) {
            entity.setOrderIndex( dto.getOrderIndex() );
        }
    }

    @Override
    public TestQuestionEntity applyTestId(TestQuestionCreateRequest dto, TestQuestionEntity entity) {
        if ( dto == null ) {
            return entity;
        }

        entity.setTestId( dto.getTestId() );
        entity.setLessonId( dto.getLessonId() );
        entity.setQuestionText( dto.getQuestionText() );
        entity.setQuestionType( dto.getQuestionType() );
        entity.setPointValue( dto.getPointValue() );
        entity.setOrderIndex( dto.getOrderIndex() );

        return entity;
    }
}
