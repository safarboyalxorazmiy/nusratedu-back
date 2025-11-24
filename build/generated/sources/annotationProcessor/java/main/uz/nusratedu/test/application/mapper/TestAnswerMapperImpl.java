package uz.nusratedu.test.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.test.application.dto.TestAnswerCreateRequest;
import uz.nusratedu.test.application.dto.TestAnswerResponse;
import uz.nusratedu.test.application.dto.TestAnswerUpdateRequest;
import uz.nusratedu.test.infastructure.entity.TestAnswerEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T02:28:35+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class TestAnswerMapperImpl implements TestAnswerMapper {

    @Override
    public TestAnswerEntity toEntity(TestAnswerCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        TestAnswerEntity.TestAnswerEntityBuilder testAnswerEntity = TestAnswerEntity.builder();

        testAnswerEntity.questionId( dto.getQuestionId() );
        testAnswerEntity.answerText( dto.getAnswerText() );

        return testAnswerEntity.build();
    }

    @Override
    public TestAnswerResponse toResponse(TestAnswerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TestAnswerResponse.TestAnswerResponseBuilder testAnswerResponse = TestAnswerResponse.builder();

        testAnswerResponse.id( entity.getId() );
        testAnswerResponse.questionId( entity.getQuestionId() );
        testAnswerResponse.answerText( entity.getAnswerText() );
        if ( entity.getIsCorrect() != null ) {
            testAnswerResponse.isCorrect( entity.getIsCorrect() );
        }

        return testAnswerResponse.build();
    }

    @Override
    public void updateEntity(TestAnswerUpdateRequest dto, TestAnswerEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getAnswerText() != null ) {
            entity.setAnswerText( dto.getAnswerText() );
        }
        if ( dto.getIsCorrect() != null ) {
            entity.setIsCorrect( dto.getIsCorrect() );
        }
    }
}
