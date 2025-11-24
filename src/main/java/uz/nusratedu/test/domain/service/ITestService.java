package uz.nusratedu.test.domain.service;

import uz.nusratedu.test.application.dto.*;

import java.util.List;
import java.util.UUID;

public interface ITestService {

    TestResponse create(TestCreateRequest request);

    TestResponse getById(UUID testId);

    List<TestResponse> getByLessonId(UUID lessonId);

    TestResultResponse startTest(UUID testId, String userTelegramId);

    TestResultResponse submitTest(TestSubmissionRequest request, String userTelegramId);

    List<TestResultResponse> getUserResults(String userTelegramId);

    List<TestResultResponse> getTestResults(UUID testId);
}