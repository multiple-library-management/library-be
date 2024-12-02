package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.GenreCreationRequest;
import org.example.backendlibrary.dtos.requests.GenreUpdateRequest;
import org.example.backendlibrary.dtos.responses.GenreResponse;
import org.example.backendlibrary.entities.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenreMapper {
    GenreResponse toGenreResponse(Genre genre);

    Genre toGenre(GenreCreationRequest genreCreationRequest);

    void updateGenre(@MappingTarget Genre genre, GenreUpdateRequest genreUpdateRequest);
}
