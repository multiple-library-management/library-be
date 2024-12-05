package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.MemberCreationRequest;
import org.example.backendlibrary.dtos.requests.MemberUpdateRequest;
import org.example.backendlibrary.dtos.responses.MemberResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.MemberService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public Response<PageResponse<MemberResponse>> getAllEmployees(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<MemberResponse>>builder()
                .success(true)
                .data(memberService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<MemberResponse> getEmployeeById(@PathVariable Long id) {
        return Response.<MemberResponse>builder()
                .success(true)
                .data(memberService.getById(id))
                .build();
    }

    @PostMapping
    public Response<MemberResponse> createEmployee(@RequestBody @Valid MemberCreationRequest memberCreationRequest) {
        return Response.<MemberResponse>builder()
                .success(true)
                .data(memberService.create(memberCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<MemberResponse> updateEmployee(
            @RequestBody @Valid MemberUpdateRequest memberUpdateRequest, @PathVariable Long id) {
        return Response.<MemberResponse>builder()
                .success(true)
                .data(memberService.update(id, memberUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteEmployee(@PathVariable Long id) {
        memberService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
