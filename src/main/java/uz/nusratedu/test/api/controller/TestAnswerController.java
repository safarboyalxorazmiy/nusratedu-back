package uz.nusratedu.test.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.domain.service.ITestAnswerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/test/answer")
@RequiredArgsConstructor
public class TestAnswerController {

    private final ITestAnswerService answerService;

    @PostMapping("/create")
    public ResponseEntity<TestAnswerResponse> createAnswer(@RequestBody TestAnswerCreateRequest request) {
        log.info("Creating answer for question: {}", request.getQuestionId());
        TestAnswerResponse response = answerService.create(request);
        log.debug("Answer created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<List<TestAnswerResponse>> createAnswers(@RequestBody List<TestAnswerCreateRequest> requests) {
        log.info("Creating {} answers in bulk", requests.size());
        List<TestAnswerResponse> responses = answerService.createBulk(requests);
        log.debug("Successfully created {} answers", responses.size());
        return ResponseEntity.status(201).body(responses);
    }

    @GetMapping("/get/{answerId}")
    public ResponseEntity<TestAnswerResponse> getAnswerById(@PathVariable UUID answerId) {
        log.info("Fetching answer with ID: {}", answerId);
        TestAnswerResponse response = answerService.getById(answerId);
        log.debug("Answer retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<TestAnswerResponse>> getAnswersByQuestion(@PathVariable UUID questionId) {
        log.info("Fetching answers for question: {}", questionId);
        List<TestAnswerResponse> answers = answerService.getByQuestionId(questionId);
        log.debug("Found {} answers for question: {}", answers.size(), questionId);
        return ResponseEntity.ok(answers);
    }

    @PutMapping("/update/{answerId}")
    public ResponseEntity<TestAnswerResponse> updateAnswer(
            @PathVariable UUID answerId,
            @RequestBody TestAnswerUpdateRequest request
    ) {
        log.info("Updating answer: {}", answerId);
        TestAnswerResponse response = answerService.update(answerId, request);
        log.debug("Answer updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable UUID answerId) {
        log.info("Deleting answer: {}", answerId);
        answerService.delete(answerId);
        log.debug("Answer deleted successfully");
        return ResponseEntity.noContent().build();
    }
}