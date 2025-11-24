package uz.nusratedu.course.domain.service;

import uz.nusratedu.course.application.dto.CompletedLessonCreateRequest;

/**
 * ✅ CONVERTED: ICompletedLessonService from reactive to blocking
 *
 * Already has void return (no Mono/Flux).
 * No changes needed - kept as-is for clarity.
 */
public interface ICompletedLessonService {

    // ✅ ALREADY BLOCKING: void create()
    void create(CompletedLessonCreateRequest dto);
}