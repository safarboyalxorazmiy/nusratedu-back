package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;

public interface ICompletedLessonService {
    void create(CompletedLessonCreateRequest dto);
}