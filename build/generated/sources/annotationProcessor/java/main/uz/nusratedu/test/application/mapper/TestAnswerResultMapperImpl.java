package uz.nusratedu.test.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.test.infastructure.entity.TestAnswerResultEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T20:03:00+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class TestAnswerResultMapperImpl implements TestAnswerResultMapper {

    @Override
    public TestAnswerResultEntity toEntity(Integer resultId, Integer questionId, Integer selectedAnswerId, Boolean isCorrect) {
        if ( resultId == null && questionId == null && selectedAnswerId == null && isCorrect == null ) {
            return null;
        }

        TestAnswerResultEntity.TestAnswerResultEntityBuilder testAnswerResultEntity = TestAnswerResultEntity.builder();

        testAnswerResultEntity.resultId( resultId );
        testAnswerResultEntity.questionId( questionId );
        testAnswerResultEntity.selectedAnswerId( selectedAnswerId );
        testAnswerResultEntity.isCorrect( isCorrect );

        return testAnswerResultEntity.build();
    }
}
