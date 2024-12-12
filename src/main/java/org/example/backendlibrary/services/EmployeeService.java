package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.EmployeeCreationRequest;
import org.example.backendlibrary.dtos.requests.EmployeeUpdateRequest;
import org.example.backendlibrary.dtos.responses.EmployeeResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Employee;
import org.example.backendlibrary.entities.Librarian;
import org.example.backendlibrary.entities.WarehouseStaff;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.EmployeeMapper;
import org.example.backendlibrary.repositories.*;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final LibraryRepository libraryRepository;
    private final LibrarianRepository librarianRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseStaffRepository warehouseStaffRepository;

    private final EmployeeMapper employeeMapper;

    public PageResponse<EmployeeResponse> getAll(int page, int size) {
        List<Employee> employees = employeeRepository.findAll(page, size);

        List<EmployeeResponse> employeesResponse =
                employees.stream().map(employeeMapper::toEmployeeResponse).toList();

        employeesResponse.forEach(employeeResponse -> {
            Optional<Librarian> optionalLibrarian =
                    Optional.ofNullable(librarianRepository.findByEmployeeId(employeeResponse.getId()));

            if (optionalLibrarian.isPresent()) {
                employeeResponse.setType("librarian");
                employeeResponse.setLibraryId(optionalLibrarian.get().getLibraryId());
                return;
            }

            Optional<WarehouseStaff> optionalWarehouseStaff =
                    Optional.ofNullable(warehouseStaffRepository.findByEmployeeId(employeeResponse.getId()));

            if (optionalWarehouseStaff.isPresent()) {
                employeeResponse.setType("warehouse_staff");
                employeeResponse.setWarehouseId(optionalWarehouseStaff.get().getWarehouseId());
                return;
            }
        });

        long totalRecords = employeeRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<EmployeeResponse>builder()
                .items(employeesResponse)
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public EmployeeResponse getById(Long id) {
        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeRepository.findById(id));

        if (optionalEmployee.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_NOTFOUND);
        }

        EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(optionalEmployee.get());

        Optional<Librarian> optionalLibrarian =
                Optional.ofNullable(librarianRepository.findByEmployeeId(employeeResponse.getId()));

        if (optionalLibrarian.isPresent()) {
            employeeResponse.setType("librarian");
            employeeResponse.setLibraryId(optionalLibrarian.get().getLibraryId());
        }

        Optional<WarehouseStaff> optionalWarehouseStaff =
                Optional.ofNullable(warehouseStaffRepository.findByEmployeeId(employeeResponse.getId()));

        if (optionalWarehouseStaff.isPresent()) {
            employeeResponse.setType("warehouse_staff");
            employeeResponse.setLibraryId(optionalWarehouseStaff.get().getWarehouseId());
        }

        return employeeResponse;
    }

    public EmployeeResponse create(EmployeeCreationRequest employeeCreationRequest) {
        Employee employee = employeeMapper.toEmployee(employeeCreationRequest);

        Long employeeId = employeeRepository.save(employee);
        employee.setId(employeeId);

        EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);

        if (employeeCreationRequest.getType().equalsIgnoreCase("librarian")) {
            // Check if the library is present
            if (!libraryRepository.existsById(employeeCreationRequest.getLibraryId())) {
                throw new AppException(ErrorCode.LIBRARY_NOTFOUND);
            }

            // if the library is present
            librarianRepository.save(Librarian.builder()
                    .employeeId(employeeId)
                    .libraryId(employeeCreationRequest.getLibraryId())
                    .build());

            employeeResponse.setType("librarian");
            employeeResponse.setLibraryId(employeeCreationRequest.getLibraryId());
            employeeResponse.setWarehouseId(null);
        } else if (employeeCreationRequest.getType().equalsIgnoreCase("warehouse_staff")) {
            // Check if the warehouse is present
            if (!warehouseRepository.existsById(employeeCreationRequest.getWarehouseId())) {
                throw new AppException(ErrorCode.WAREHOUSE_NOTFOUND);
            }

            // if the warehouse is present
            warehouseStaffRepository.save(WarehouseStaff.builder()
                    .employeeId(employeeId)
                    .warehouseId(employeeCreationRequest.getWarehouseId())
                    .build());

            employeeResponse.setType("warehouse_staff");
            employeeResponse.setLibraryId(null);
            employeeResponse.setWarehouseId(employeeCreationRequest.getWarehouseId());
        }

        return employeeResponse;
    }

    public EmployeeResponse update(long id, EmployeeUpdateRequest employeeUpdateRequest) {
        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeRepository.findById(id));

        if (optionalEmployee.isEmpty()) {
            throw new AppException(ErrorCode.EMPLOYEE_NOTFOUND);
        }

        // update attribute of employees
        Employee employee = optionalEmployee.get();

        employeeMapper.updateEmployee(employee, employeeUpdateRequest);

        employeeRepository.update(employee);

        EmployeeResponse employeeResponse = employeeMapper.toEmployeeResponse(employee);

        // update role of employee
        if (employeeUpdateRequest.getType().equalsIgnoreCase("librarian")) {
            // Check if the library is present
            if (!libraryRepository.existsById(employeeUpdateRequest.getLibraryId())) {
                throw new AppException(ErrorCode.LIBRARY_NOTFOUND);
            }

            // delete librarian
            Librarian librarian = librarianRepository.findByEmployeeId(id);
            librarianRepository.deleteByEmployeeId(librarian.getEmployeeId());

            // delete warehouse staff
            WarehouseStaff warehouseStaff = warehouseStaffRepository.findByEmployeeId(id);
            warehouseStaffRepository.deleteByEmployeeId(warehouseStaff.getEmployeeId());

            // recreate librarian
            librarian = Librarian.builder()
                    .employeeId(id)
                    .libraryId(employeeUpdateRequest.getLibraryId())
                    .build();

            librarianRepository.save(librarian);

            employeeResponse.setType("librarian");
            employeeResponse.setLibraryId(librarian.getLibraryId());
            employeeResponse.setWarehouseId(null);
        } else if (employeeUpdateRequest.getType().equalsIgnoreCase("warehouse_staff")) {
            // Check if the warehouse is present
            if (!warehouseRepository.existsById(employeeUpdateRequest.getWarehouseId())) {
                throw new AppException(ErrorCode.WAREHOUSE_NOTFOUND);
            }

            // delete librarian
            Librarian librarian = librarianRepository.findByEmployeeId(id);
            librarianRepository.deleteByEmployeeId(librarian.getEmployeeId());

            // delete warehouse staff
            WarehouseStaff warehouseStaff = warehouseStaffRepository.findByEmployeeId(id);
            warehouseStaffRepository.deleteByEmployeeId(warehouseStaff.getEmployeeId());

            // recreate warehouse staff
            warehouseStaff = WarehouseStaff.builder()
                    .employeeId(id)
                    .warehouseId(employeeUpdateRequest.getWarehouseId())
                    .build();

            warehouseStaffRepository.save(warehouseStaff);
            employeeResponse.setType("warehouse_staff");
            employeeResponse.setLibraryId(null);
            employeeResponse.setWarehouseId(warehouseStaff.getWarehouseId());
        }

        return employeeResponse;
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new AppException(ErrorCode.EMPLOYEE_NOTFOUND);
        }

        employeeRepository.deleteById(id);
    }
}
