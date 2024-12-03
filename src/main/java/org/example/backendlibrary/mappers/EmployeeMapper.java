package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.EmployeeCreationRequest;
import org.example.backendlibrary.dtos.requests.EmployeeUpdateRequest;
import org.example.backendlibrary.dtos.responses.EmployeeResponse;
import org.example.backendlibrary.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    EmployeeResponse toEmployeeResponse(Employee employee);

    Employee toEmployee(EmployeeCreationRequest employeeCreationRequest);

    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest employeeUpdateRequest);
}
