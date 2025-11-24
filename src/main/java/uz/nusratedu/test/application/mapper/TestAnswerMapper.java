package uz.nusratedu.test.application.mapper;

import org.mapstruct.*;
import uz.nusratedu.test.application.dto.*;
import uz.nusratedu.test.infastructure.entity.TestAnswerEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TestAnswerMapper {

    TestAnswerEntity toEntity(TestAnswerCreateRequest dto);

    TestAnswerResponse toResponse(TestAnswerEntity entity);

    // Correct: Update DTO has no questionId → don't map it
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(TestAnswerUpdateRequest dto, @MappingTarget TestAnswerEntity entity);
}