package uz.pdp.online.jayxun.onlinerailwayticket.mapper;

import org.mapstruct.*;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.request.SignUpReqDto;
import uz.pdp.online.jayxun.onlinerailwayticket.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(SignUpReqDto signUpReqDto);

    SignUpReqDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(SignUpReqDto signUpReqDto, @MappingTarget User user);
}