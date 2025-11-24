package uz.nusratedu.test.domain.service;

import uz.nusratedu.test.application.dto.*;

import java.util.List;
import java.util.UUID;

public interface ITestAnswerService {

    TestAnswerResponse create(TestAnswerCreateRequest dto);

    List<TestAnswerResponse> createBulk(List<TestAnswerCreateRequest> dtos);

    TestAnswerResponse getById(UUID answerId);

    List<TestAnswerResponse> getByQuestionId(UUID questionId);

    TestAnswerResponse update(UUID answerId, TestAnswerUpdateRequest dto);

    void delete(UUID answerId);
}