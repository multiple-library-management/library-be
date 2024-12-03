package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.EmployeeCreationRequest;
import org.example.backendlibrary.dtos.requests.EmployeeUpdateRequest;
import org.example.backendlibrary.dtos.responses.EmployeeResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public Response<PageResponse<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<EmployeeResponse>>builder()
                .success(true)
                .data(employeeService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return Response.<EmployeeResponse>builder()
                .success(true)
                .data(employeeService.getById(id))
                .build();
    }

    @PostMapping
    public Response<Void> createEmployee(@RequestBody @Valid EmployeeCreationRequest employeeCreationRequest) {
        employeeService.create(employeeCreationRequest);

        return Response.<Void>builder().success(true).build();
    }

    @PutMapping("/{id}")
    public Response<Void> updateEmployee(@RequestBody EmployeeUpdateRequest employeeUpdateRequest, @PathVariable Long id) {
        employeeService.update(id, employeeUpdateRequest);

        return Response.<Void>builder().success(true).build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
