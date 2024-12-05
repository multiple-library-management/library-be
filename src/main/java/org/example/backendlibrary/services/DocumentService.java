package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.DocumentCreationRequest;
import org.example.backendlibrary.dtos.requests.DocumentUpdateRequest;
import org.example.backendlibrary.dtos.responses.DocumentResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Document;
import org.example.backendlibrary.entities.Genre;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.DocumentMapper;
import org.example.backendlibrary.repositories.DocumentAuthorsRepository;
import org.example.backendlibrary.repositories.DocumentGenresRepository;
import org.example.backendlibrary.repositories.DocumentRepository;
import org.example.backendlibrary.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentAuthorsRepository documentAuthorsRepository;
    private final DocumentGenresRepository documentGenresRepository;
    private final GenreRepository genreRepository;
    private final DocumentMapper documentMapper;

    public PageResponse<DocumentResponse> getAll(int page, int size) {
        List<Document> documents = documentRepository.findAll(page, size);

        documents.forEach((document) -> {
            List<String> authors = documentAuthorsRepository.getAuthorsByDocumentId(document.getId());
            List<String> genres = documentGenresRepository.getGenresByDocumentId(document.getId());
            document.setAuthors(authors);
            document.setGenres(genres);
        });

        long totalRecords = documentRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<DocumentResponse>builder()
                .items(documents.stream()
                        .map(documentMapper::toDocumentResponse)
                        .toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public DocumentResponse getById(Long id) {
        Optional<Document> optionalDocument = Optional.ofNullable(documentRepository.findById(id));

        if (optionalDocument.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        Document document = optionalDocument.get();
        List<String> authors = documentAuthorsRepository.getAuthorsByDocumentId(document.getId());
        List<String> genres = documentGenresRepository.getGenresByDocumentId(document.getId());
        document.setAuthors(authors);
        document.setGenres(genres);

        return documentMapper.toDocumentResponse(document);
    }

    public DocumentResponse create(DocumentCreationRequest documentCreationRequest) {
        Document document = documentMapper.toDocument(documentCreationRequest);

        long documentId = documentRepository.save(document);
        document.setId(documentId);

        document.getAuthors().forEach(author -> {
            //            log.error("author name: {}", author);
            documentAuthorsRepository.addAuthorToDocument((long) documentId, author);
        });

        document.getGenres().forEach(genre -> {
            Optional<Genre> optionalGenre = Optional.ofNullable(genreRepository.findByName(genre));
            if (optionalGenre.isEmpty()) {
                long genreId = genreRepository.save(Genre.builder().name(genre).build());
                documentGenresRepository.addGenreToDocument(documentId, genreId);
                return;
            }

            documentGenresRepository.addGenreToDocument(
                    documentId, optionalGenre.get().getId());
        });

        return documentMapper.toDocumentResponse(document);
    }

    public DocumentResponse update(long id, DocumentUpdateRequest documentUpdateRequest) {
        Optional<Document> optionalDocument = Optional.ofNullable(documentRepository.findById(id));

        if (optionalDocument.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        Document document = optionalDocument.get();

        documentMapper.updateDocument(document, documentUpdateRequest);

        documentAuthorsRepository.deleteAuthorByDocumentId((int) id);

        document.getAuthors().forEach(author -> {
            documentAuthorsRepository.addAuthorToDocument(id, author);
        });

        documentGenresRepository.deleteGenresByDocumentId((int) id);

        document.getGenres().forEach(genre -> {
            Optional<Genre> optionalGenre = Optional.ofNullable(genreRepository.findByName(genre));
            if (optionalGenre.isEmpty()) {
                long genreId = genreRepository.save(Genre.builder().name(genre).build());
                documentGenresRepository.addGenreToDocument(id, genreId);
                return;
            }

            documentGenresRepository.addGenreToDocument(id, optionalGenre.get().getId());
        });

        return documentMapper.toDocumentResponse(document);
    }

    public void delete(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        documentRepository.deleteById(id);
    }
}
