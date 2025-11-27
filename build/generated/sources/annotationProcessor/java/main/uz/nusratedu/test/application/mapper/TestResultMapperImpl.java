package uz.nusratedu.test.application.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.nusratedu.test.application.dto.TestResultResponse;
import uz.nusratedu.test.infastructure.entity.TestResultEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T20:03:00+0500",
    comments = "version: 1.6.2, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.3.jar, environment: Java 21.0.9 (Debian)"
)
@Component
public class TestResultMapperImpl implements TestResultMapper {

    @Override
    public TestResultResponse toResponse(TestResultEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TestResultResponse.TestResultResponseBuilder testResultResponse = TestResultResponse.builder();

        testResultResponse.isPassed( entity.getIsPassed() );
        testResultResponse.id( entity.getId() );
        testResultResponse.testId( entity.getTestId() );
        testResultResponse.userId( entity.getUserId() );
        testResultResponse.score( entity.getScore() );
        testResultResponse.startedAt( entity.getStartedAt() );
        testResultResponse.finishedAt( entity.getFinishedAt() );
        testResultResponse.attemptNumber( entity.getAttemptNumber() );
        testResultResponse.createdAt( entity.getCreatedAt() );

        return testResultResponse.build();
    }
}
