package uz.pdp.online.jayxun.onlinerailwayticket.mapper;

import org.mapstruct.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.entityDtoWithoutId.ConfirmSentCodeResDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.ConfirmSentCode;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfirmSentCodeMapper {
    ConfirmSentCode toEntity(ConfirmSentCodeResDto confirmSentCodeResDto);

    ConfirmSentCodeResDto toDto(ConfirmSentCode confirmSentCode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ConfirmSentCode partialUpdate(ConfirmSentCodeResDto confirmSentCodeResDto, @MappingTarget ConfirmSentCode confirmSentCode);
}