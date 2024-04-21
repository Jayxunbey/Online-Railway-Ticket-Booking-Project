package uz.pdp.online.jayxun.onlinerailwayticket.mapper;

import org.mapstruct.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfirmSentCodeMapper {
    ConfirmSentCode toEntity(ConfirmSentCodeDto confirmSentCodeDto);

    ConfirmSentCodeDto toDto(ConfirmSentCode confirmSentCode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ConfirmSentCode partialUpdate(ConfirmSentCodeDto confirmSentCodeDto, @MappingTarget ConfirmSentCode confirmSentCode);
}