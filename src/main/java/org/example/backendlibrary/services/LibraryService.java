package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.LibraryCreationRequest;
import org.example.backendlibrary.dtos.requests.LibraryUpdateRequest;
import org.example.backendlibrary.dtos.responses.LibraryResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Library;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.LibraryMapper;
import org.example.backendlibrary.repositories.LibraryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final LibraryRepository libraryRepository;

    private final LibraryMapper libraryMapper;

    public PageResponse<LibraryResponse> getAll(int page, int size) {
        List<Library> libraries = libraryRepository.findAll(page, size);

        long totalRecords = libraryRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<LibraryResponse>builder()
                .items(libraries.stream().map(libraryMapper::toLibraryResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public LibraryResponse getById(Long id) {
        Optional<Library> optionalLibrary = Optional.ofNullable(libraryRepository.findById(id));

        if (optionalLibrary.isEmpty()) {
            throw new AppException(ErrorCode.LIBRARY_NOTFOUND);
        }

        return libraryMapper.toLibraryResponse(optionalLibrary.get());
    }

    public LibraryResponse create(LibraryCreationRequest libraryCreationRequest) {
        Library library = libraryMapper.toLibrary(libraryCreationRequest);

        long libraryId = libraryRepository.save(library);
        library.setId(libraryId);

        return libraryMapper.toLibraryResponse(library);
    }

    public LibraryResponse update(long id, LibraryUpdateRequest libraryUpdateRequest) {
        Optional<Library> optionalLibrary = Optional.ofNullable(libraryRepository.findById(id));

        if (optionalLibrary.isEmpty()) {
            throw new AppException(ErrorCode.LIBRARY_NOTFOUND);
        }

        Library library = optionalLibrary.get();

        libraryMapper.updateLibrary(library, libraryUpdateRequest);

        libraryRepository.update(library);

        return libraryMapper.toLibraryResponse(library);
    }

    public void delete(Long id) {
        if (!libraryRepository.existsById(id)) {
            throw new AppException(ErrorCode.LIBRARY_NOTFOUND);
        }

        libraryRepository.deleteById(id);
    }
}
