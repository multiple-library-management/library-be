package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.MemberCreationRequest;
import org.example.backendlibrary.dtos.requests.MemberUpdateRequest;
import org.example.backendlibrary.dtos.responses.MemberResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Member;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.MemberMapper;
import org.example.backendlibrary.repositories.MemberRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public PageResponse<MemberResponse> getAll(int page, int size) {
        List<Member> members = memberRepository.findAll(page, size);

        long totalRecords = memberRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<MemberResponse>builder()
                .items(members.stream().map(memberMapper::toMemberResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public MemberResponse getById(Long id) {
        Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findById(id));

        if (optionalMember.isEmpty()) {
            throw new AppException(ErrorCode.MEMBER_NOTFOUND);
        }

        return memberMapper.toMemberResponse(optionalMember.get());
    }

    public MemberResponse create(MemberCreationRequest memberCreationRequest) {
        Member member = memberMapper.toMember(memberCreationRequest);

        Long employeeId = memberRepository.save(member);
        member.setId(employeeId);

        return memberMapper.toMemberResponse(member);
    }

    public MemberResponse update(long id, MemberUpdateRequest memberUpdateRequest) {
        Optional<Member> optionalMember = Optional.ofNullable(memberRepository.findById(id));

        if (optionalMember.isEmpty()) {
            throw new AppException(ErrorCode.MEMBER_NOTFOUND);
        }

        // update attribute of employees
        Member member = optionalMember.get();

        memberMapper.updateMember(member, memberUpdateRequest);

        memberRepository.update(member);

        return memberMapper.toMemberResponse(member);
    }

    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new AppException(ErrorCode.MEMBER_NOTFOUND);
        }

        memberRepository.deleteById(id);
    }
}
