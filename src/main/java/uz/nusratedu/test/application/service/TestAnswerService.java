package uz.nusratedu.test.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.application.mapper.TestAnswerMapper;
import uz.nusratedu.test.domain.service.ITestAnswerService;
import uz.nusratedu.test.infastructure.entity.TestAnswerEntity;
import uz.nusratedu.test.infastructure.repository.TestAnswerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestAnswerService implements ITestAnswerService {

    private final TestAnswerRepository answerRepository;
    private final TestAnswerMapper answerMapper;

    @Override
    public TestAnswerResponse create(TestAnswerCreateRequest dto) {
        log.debug("Creating answer for question: {}", dto.getQuestionId());
        TestAnswerEntity entity = answerMapper.toEntity(dto);
        TestAnswerEntity saved = answerRepository.save(entity);
        log.info("Answer created with ID: {} for question: {}", saved.getId(), saved.getQuestionId());
        return answerMapper.toResponse(saved);
    }

    @Override
    public List<TestAnswerResponse> createBulk(List<TestAnswerCreateRequest> dtos) {
        log.info("Processing bulk creation of {} answers", dtos.size());
        List<TestAnswerResponse> responses = new ArrayList<>();

        for (TestAnswerCreateRequest dto : dtos) {
            try {
                TestAnswerEntity entity = answerMapper.toEntity(dto);
                TestAnswerEntity saved = answerRepository.save(entity);
                responses.add(answerMapper.toResponse(saved));
                log.trace("Answer created with ID: {}", saved.getId());
            } catch (Exception e) {
                log.error("Failed to create answer: {}", e.getMessage());
            }
        }

        log.info("Successfully created {} out of {} answers", responses.size(), dtos.size());
        return responses;
    }

    @Override
    public TestAnswerResponse getById(UUID answerId) {
        log.debug("Fetching answer with ID: {}", answerId);
        TestAnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> {
                    log.error("Answer not found with ID: {}", answerId);
                    return new RuntimeException("Answer not found: " + answerId);
                });
        log.info("Answer retrieved successfully");
        return answerMapper.toResponse(answer);
    }

    @Override
    public List<TestAnswerResponse> getByQuestionId(UUID questionId) {
        log.debug("Fetching answers for question: {}", questionId);
        List<TestAnswerResponse> answers = answerRepository.findByQuestionId(questionId)
                .stream()
                .map(answerMapper::toResponse)
                .collect(Collectors.toList());
        log.info("Found {} answers for question: {}", answers.size(), questionId);
        return answers;
    }

    @Override
    public TestAnswerResponse update(UUID answerId, TestAnswerUpdateRequest dto) {
        log.info("Updating answer: {}", answerId);
        TestAnswerEntity answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found: " + answerId));

        if (dto.getAnswerText() != null) {
            answer.setAnswerText(dto.getAnswerText());
        }
        if (dto.getIsCorrect() != null) {
            answer.setIsCorrect(dto.getIsCorrect());
        }

        TestAnswerEntity updated = answerRepository.save(answer);
        log.debug("Answer updated successfully");
        return answerMapper.toResponse(updated);
    }

    @Override
    public void delete(UUID answerId) {
        log.info("Deleting answer: {}", answerId);
        if (!answerRepository.existsById(answerId)) {
            log.error("Answer not found for deletion: {}", answerId);
            throw new RuntimeException("Answer not found: " + answerId);
        }
        answerRepository.deleteById(answerId);
        log.debug("Answer deleted successfully");
    }
}