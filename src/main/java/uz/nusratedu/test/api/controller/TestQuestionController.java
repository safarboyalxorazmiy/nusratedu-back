package uz.nusratedu.test.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.domain.service.ITestQuestionService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/test/question")
@RequiredArgsConstructor
public class TestQuestionController {

    private final ITestQuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<TestQuestionResponse> createQuestion(@RequestBody TestQuestionCreateRequest request) {
        log.info("Creating question for test: {}", request.getTestId());
        TestQuestionResponse response = questionService.create(request);
        log.debug("Question created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<List<TestQuestionResponse>> createQuestions(@RequestBody List<TestQuestionCreateRequest> requests) {
        log.info("Creating {} questions in bulk", requests.size());
        List<TestQuestionResponse> responses = questionService.createBulk(requests);
        log.debug("Successfully created {} questions", responses.size());
        return ResponseEntity.status(201).body(responses);
    }

    @GetMapping("/get/{questionId}")
    public ResponseEntity<TestQuestionResponse> getQuestionById(@PathVariable UUID questionId) {
        log.info("Fetching question with ID: {}", questionId);
        TestQuestionResponse response = questionService.getById(questionId);
        log.debug("Question retrieved: {}", response.getQuestionText());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<List<TestQuestionResponse>> getQuestionsByTest(@PathVariable UUID testId) {
        log.info("Fetching questions for test: {}", testId);
        List<TestQuestionResponse> questions = questionService.getByTestId(testId);
        log.debug("Found {} questions for test: {}", questions.size(), testId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/update/{questionId}")
    public ResponseEntity<TestQuestionResponse> updateQuestion(
            @PathVariable UUID questionId,
            @RequestBody TestQuestionUpdateRequest request
    ) {
        log.info("Updating question: {}", questionId);
        TestQuestionResponse response = questionService.update(questionId, request);
        log.debug("Question updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID questionId) {
        log.info("Deleting question: {}", questionId);
        questionService.delete(questionId);
        log.debug("Question deleted successfully");
        return ResponseEntity.noContent().build();
    }
}