package uz.nusratedu.test.domain.service;

import uz.nusratedu.test.application.dto.*;

import java.util.List;
import java.util.UUID;

public interface ITestQuestionService {

    TestQuestionResponse create(TestQuestionCreateRequest dto);

    List<TestQuestionResponse> createBulk(List<TestQuestionCreateRequest> dtos);

    TestQuestionResponse getById(UUID questionId);

    List<TestQuestionResponse> getByTestId(UUID testId);

    TestQuestionResponse update(UUID questionId, TestQuestionUpdateRequest dto);

    void delete(UUID questionId);
}