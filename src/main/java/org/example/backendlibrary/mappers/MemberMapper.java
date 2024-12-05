package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.MemberCreationRequest;
import org.example.backendlibrary.dtos.requests.MemberUpdateRequest;
import org.example.backendlibrary.dtos.responses.MemberResponse;
import org.example.backendlibrary.entities.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    MemberResponse toMemberResponse(Member member);

    Member toMember(MemberCreationRequest memberCreationRequest);

    void updateMember(@MappingTarget Member member, MemberUpdateRequest memberUpdateRequest);
}
