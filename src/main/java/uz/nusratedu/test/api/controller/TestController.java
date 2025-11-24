package uz.nusratedu.test.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.domain.service.ITestService;
import uz.nusratedu.user.SecurityUser;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final ITestService testService;

    @PostMapping("/create")
    public ResponseEntity<TestResponse> createTest(@RequestBody TestCreateRequest request) {
        log.info("Creating test: {} for lesson: {}", request.getTitle(), request.getLessonId());
        TestResponse response = testService.create(request);
        log.debug("Test created with ID: {}", response.getId());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/get/{testId}")
    public ResponseEntity<TestResponse> getTestById(@PathVariable UUID testId) {
        log.info("Fetching test with ID: {}", testId);
        TestResponse response = testService.getById(testId);
        log.debug("Test retrieved: {}", response.getTitle());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<TestResponse>> getTestsByLesson(@PathVariable UUID lessonId) {
        log.info("Fetching tests for lesson: {}", lessonId);
        List<TestResponse> tests = testService.getByLessonId(lessonId);
        log.debug("Found {} tests for lesson: {}", tests.size(), lessonId);
        return ResponseEntity.ok(tests);
    }

    @PostMapping("/start/{testId}")
    public ResponseEntity<TestResultResponse> startTest(
            @PathVariable UUID testId,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("User {} starting test: {}", user.getTelegramId(), testId);
        TestResultResponse response = testService.startTest(testId, user.getTelegramId());
        log.debug("Test session started with result ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<TestResultResponse> submitTest(
            @RequestBody TestSubmissionRequest request,
            Authentication authentication
    ) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("User {} submitting test result: {}", user.getTelegramId(), request.getResultId());
        TestResultResponse response = testService.submitTest(request, user.getTelegramId());
        log.debug("Test submitted. Score: {}, Passed: {}", response.getScore(), response.getIsPassed());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/results/user")
    public ResponseEntity<List<TestResultResponse>> getUserTestResults(Authentication authentication) {
        var securityUser = (SecurityUser) authentication.getPrincipal();
        var user = securityUser.getUser();

        log.info("Fetching test results for user: {}", user.getTelegramId());
        List<TestResultResponse> results = testService.getUserResults(user.getTelegramId());
        log.debug("Found {} test results for user", results.size());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/{testId}")
    public ResponseEntity<List<TestResultResponse>> getTestResults(@PathVariable UUID testId) {
        log.info("Fetching all results for test: {}", testId);
        List<TestResultResponse> results = testService.getTestResults(testId);
        log.debug("Found {} results for test: {}", results.size(), testId);
        return ResponseEntity.ok(results);
    }
}