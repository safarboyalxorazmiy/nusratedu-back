package uz.nusratedu.test.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.nusratedu.test.infastructure.entity.TestAnswerResultEntity;

@Mapper(componentModel = "spring")
public interface TestAnswerResultMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resultId", source = "resultId")
    @Mapping(target = "questionId", source = "questionId")
    @Mapping(target = "selectedAnswerId", source = "selectedAnswerId")
    @Mapping(target = "isCorrect", source = "isCorrect")
    TestAnswerResultEntity toEntity(Integer resultId, Integer questionId, Integer selectedAnswerId, Boolean isCorrect);
}