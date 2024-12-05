package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.GenreCreationRequest;
import org.example.backendlibrary.dtos.requests.GenreUpdateRequest;
import org.example.backendlibrary.dtos.responses.GenreResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Genre;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.GenreMapper;
import org.example.backendlibrary.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public PageResponse<GenreResponse> getAll(int page, int size) {
        List<Genre> genres = genreRepository.findAll(page, size);

        long totalRecords = genreRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<GenreResponse>builder()
                .items(genres.stream().map(genreMapper::toGenreResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public GenreResponse getById(Long id) {
        Optional<Genre> optionalGenre = Optional.ofNullable(genreRepository.findById(id));

        if (optionalGenre.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        return genreMapper.toGenreResponse(optionalGenre.get());
    }

    public GenreResponse create(GenreCreationRequest genreCreationRequest) {
        Genre genre = genreMapper.toGenre(genreCreationRequest);

        long genreId = genreRepository.save(genre);
        genre.setId(genreId);

        return genreMapper.toGenreResponse(genre);
    }

    public GenreResponse update(long id, GenreUpdateRequest genreUpdateRequest) {
        Optional<Genre> optionalGenre = Optional.ofNullable(genreRepository.findById(id));

        if (optionalGenre.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        Genre genre = optionalGenre.get();

        genreMapper.updateGenre(genre, genreUpdateRequest);

        genreRepository.update(genre);

        return genreMapper.toGenreResponse(genre);
    }

    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        genreRepository.deleteById(id);
    }
}
