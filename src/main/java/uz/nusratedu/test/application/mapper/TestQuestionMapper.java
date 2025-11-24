package uz.nusratedu.test.application.mapper;

import org.mapstruct.*;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.infastructure.entity.TestQuestionEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestQuestionMapper {

    TestQuestionEntity toEntity(TestQuestionCreateRequest dto);

    TestQuestionResponse toResponse(TestQuestionEntity entity);

    // Correct: Update request has NO testId → don't try to map it
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TestQuestionUpdateRequest dto, @MappingTarget TestQuestionEntity entity);

    // Optional: if you want a full update method with testId
    @Mapping(target = "testId", source = "testId")
    TestQuestionEntity applyTestId(TestQuestionCreateRequest dto, @MappingTarget TestQuestionEntity entity);
}