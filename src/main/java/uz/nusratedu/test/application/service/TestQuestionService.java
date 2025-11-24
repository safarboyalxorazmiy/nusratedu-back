package uz.nusratedu.test.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.application.mapper.TestQuestionMapper;
import uz.nusratedu.test.domain.service.ITestQuestionService;
import uz.nusratedu.test.infastructure.entity.TestQuestionEntity;
import uz.nusratedu.test.infastructure.repository.TestQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestQuestionService implements ITestQuestionService {

    private final TestQuestionRepository questionRepository;
    private final TestQuestionMapper questionMapper;

    @Override
    public TestQuestionResponse create(TestQuestionCreateRequest dto) {
        log.debug("Creating question for test: {}", dto.getTestId());
        TestQuestionEntity entity = questionMapper.toEntity(dto);
        TestQuestionEntity saved = questionRepository.save(entity);
        log.info("Question created with ID: {} for test: {}", saved.getId(), saved.getTestId());
        return questionMapper.toResponse(saved);
    }

    @Override
    public List<TestQuestionResponse> createBulk(List<TestQuestionCreateRequest> dtos) {
        log.info("Processing bulk creation of {} questions", dtos.size());
        List<TestQuestionResponse> responses = new ArrayList<>();

        for (TestQuestionCreateRequest dto : dtos) {
            try {
                TestQuestionEntity entity = questionMapper.toEntity(dto);
                TestQuestionEntity saved = questionRepository.save(entity);
                responses.add(questionMapper.toResponse(saved));
                log.trace("Question created with ID: {}", saved.getId());
            } catch (Exception e) {
                log.error("Failed to create question: {}", e.getMessage());
            }
        }

        log.info("Successfully created {} out of {} questions", responses.size(), dtos.size());
        return responses;
    }

    @Override
    public TestQuestionResponse getById(UUID questionId) {
        log.debug("Fetching question with ID: {}", questionId);
        TestQuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> {
                    log.error("Question not found with ID: {}", questionId);
                    return new RuntimeException("Question not found: " + questionId);
                });
        log.info("Question retrieved successfully");
        return questionMapper.toResponse(question);
    }

    @Override
    public List<TestQuestionResponse> getByTestId(UUID testId) {
        log.debug("Fetching questions for test: {}", testId);
        List<TestQuestionResponse> questions = questionRepository.findByTestId(testId)
                .stream()
                .sorted((q1, q2) -> Integer.compare(q1.getOrderIndex(), q2.getOrderIndex()))
                .map(questionMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} questions for test: {}", questions.size(), testId);
        return questions;
    }

    @Override
    public TestQuestionResponse update(UUID questionId, TestQuestionUpdateRequest dto) {
        log.info("Updating question: {}", questionId);
        TestQuestionEntity question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

        if (dto.getQuestionText() != null) {
            question.setQuestionText(dto.getQuestionText());
        }
        if (dto.getQuestionType() != null) {
            question.setQuestionType(dto.getQuestionType());
        }
        if (dto.getPointValue() != null) {
            question.setPointValue(dto.getPointValue());
        }
        if (dto.getOrderIndex() != null) {
            question.setOrderIndex(dto.getOrderIndex());
        }

        TestQuestionEntity updated = questionRepository.save(question);
        log.debug("Question updated successfully");
        return questionMapper.toResponse(updated);
    }

    @Override
    public void delete(UUID questionId) {
        log.info("Deleting question: {}", questionId);
        if (!questionRepository.existsById(questionId)) {
            log.error("Question not found for deletion: {}", questionId);
            throw new RuntimeException("Question not found: " + questionId);
        }
        questionRepository.deleteById(questionId);
        log.debug("Question deleted successfully");
    }
}