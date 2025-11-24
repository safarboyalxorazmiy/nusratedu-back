package uz.nusratedu.test.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.application.mapper.*;
import uz.nusratedu.test.domain.service.ITestService;
import uz.nusratedu.test.infastructure.entity.*;
import uz.nusratedu.test.infastructure.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TestService implements ITestService {

    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final TestQuestionRepository questionRepository;
    private final TestAnswerRepository answerRepository;
    private final TestAnswerResultRepository answerResultRepository;

    private final TestMapper testMapper;
    private final TestResultMapper resultMapper;
    private final TestQuestionMapper questionMapper;

    @Override
    public TestResponse create(TestCreateRequest request) {
        log.info("Creating new test: {}", request.getTitle());

        TestEntity entity = testMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());

        TestEntity saved = testRepository.save(entity);
        log.info("Test created successfully with ID: {}", saved.getId());

        return testMapper.toResponse(saved);
    }

    @Override
    public TestResponse getById(UUID testId) {
        TestEntity entity = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found: " + testId));
        return testMapper.toResponse(entity);
    }

    @Override
    public List<TestResponse> getByLessonId(UUID lessonId) {
        return testRepository.findByLessonId(lessonId).stream()
                .map(testMapper::toResponse)
                .toList();
    }

    @Override
    public TestResultResponse startTest(UUID testId, String userTelegramId) {
        log.info("User {} starting test: {}", userTelegramId, testId);

        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found: " + testId));

        // Count previous attempts
        int attemptNumber = testResultRepository.findByTestIdAndUserId(testId, userTelegramId)
                .size() + 1;

        TestResultEntity resultEntity = TestResultEntity.builder()
                .testId(testId)
                .userId(userTelegramId)
                .startedAt(LocalDateTime.now())
                .attemptNumber(attemptNumber)
                .createdAt(LocalDateTime.now())
                .build();

        TestResultEntity saved = testResultRepository.save(resultEntity);
        log.info("Test started. Result ID: {}, Attempt: {}", saved.getId(), attemptNumber);

        return resultMapper.toResponse(saved);
    }

    @Override
    public TestResultResponse submitTest(TestSubmissionRequest request, String userTelegramId) {
        UUID resultId = request.getResultId();
        Map<UUID, Integer> answers = request.getAnswers();

        log.info("User {} submitting test result: {}", userTelegramId, resultId);

        TestResultEntity resultEntity = testResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Test result not found: " + resultId));

        // Security: ensure user owns this result
        if (!resultEntity.getUserId().equals(userTelegramId)) {
            throw new RuntimeException("You cannot submit someone else's test");
        }

        if (resultEntity.getFinishedAt() != null) {
            throw new RuntimeException("Test already submitted");
        }

        TestEntity test = testRepository.findById(resultEntity.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        int totalScore = 0;
        int maxPossibleScore = 0;

        // Process each answer
        for (Map.Entry<UUID, Integer> entry : answers.entrySet()) {
            UUID questionId = entry.getKey();
            Integer selectedAnswerId = entry.getValue();

            TestQuestionEntity question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            maxPossibleScore += question.getPointValue();

            // Find correct answer
            List<TestAnswerEntity> correctAnswers = answerRepository.findByQuestionId(questionId)
                    .stream()
                    .filter(TestAnswerEntity::getIsCorrect)
                    .toList();

            if (correctAnswers.isEmpty()) {
                log.warn("Question {} has no correct answer!", questionId);
                continue;
            }

            boolean isCorrect = correctAnswers.stream()
                    .anyMatch(a -> a.getId().toString().equals(selectedAnswerId.toString()) || // fallback
                            a.getId().equals(UUID.fromString(selectedAnswerId.toString()))); // safe

            if (isCorrect) {
                totalScore += question.getPointValue();
            }

            // Save individual answer result
            TestAnswerResultEntity answerResult = TestAnswerResultEntity.builder()
                    .resultId(Math.toIntExact(resultEntity.getId().getMostSignificantBits())) // or use UUID → Integer mapping
                    .questionId(Math.toIntExact(questionId.getMostSignificantBits()))
                    .selectedAnswerId(selectedAnswerId)
                    .isCorrect(isCorrect)
                    .build();

            answerResultRepository.save(answerResult);
        }

        boolean isPassed = test.getPassingScore() != null &&
                totalScore >= test.getPassingScore();

        // Finalize result
        resultEntity.setScore(totalScore);
        resultEntity.setIsPassed(isPassed);
        resultEntity.setFinishedAt(LocalDateTime.now());

        TestResultEntity updated = testResultRepository.save(resultEntity);

        log.info("Test submitted successfully. Score: {}/{}, Passed: {}", totalScore, maxPossibleScore, isPassed);

        return resultMapper.toResponse(updated);
    }

    @Override
    public List<TestResultResponse> getUserResults(String userTelegramId) {
        return testResultRepository.findByUserId(userTelegramId).stream()
                .map(resultMapper::toResponse)
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    @Override
    public List<TestResultResponse> getTestResults(UUID testId) {
        return testResultRepository.findByTestId(testId).stream()
                .map(resultMapper::toResponse)
                .sorted((a, b) -> b.getFinishedAt().compareTo(a.getFinishedAt()))
                .toList();
    }
}