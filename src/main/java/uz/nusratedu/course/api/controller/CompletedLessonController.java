package uz.nusratedu.course.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;
import uz.nusratedu.course.domain.service.ICompletedLessonService;

@RestController("/completed/lesson")
@RequiredArgsConstructor
public class CompletedLessonController {
    private final ICompletedLessonService service;

    @PostMapping("/create")
    public ResponseEntity<Void> create(
            @RequestBody CompletedLessonCreateRequest dto
    ) {
        service.create(dto);
        return ResponseEntity.status(201).build();
    }
}