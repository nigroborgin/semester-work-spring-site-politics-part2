package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.FileUploaderUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.PathRefactorerUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    @Value("${upload.realpath}")
    private String uploadPath;
    @Value("${upload.url.suffix.books}")
    private String urlSuffixBooks;
    @Autowired
    BookRepository bookRepository;

    public void showAll(CustomUserDetails userSess, ModelMap map) {

        boolean showNew = false;
        boolean showEdit = false;
        boolean showDelete = false;

        List<BookViewDto> bookList = bookRepository.findAll().stream()
                .map(b -> (BookViewDto) ConverterUtil.updateAndReturn(b, new BookViewDto()))
                .toList();

        if (userSess != null) {
            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                showNew = true;
                showEdit = true;
                showDelete = true;
            }
        }

        map.put("showNew", showNew);
        map.put("showEdit", showEdit);
        map.put("showDelete", showDelete);

        map.put("bookList", bookList);
    }

    public void showOne(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            Book book = bookRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book"));
            BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());
            boolean showNew = false;
            boolean showEdit = false;
            boolean showDelete = false;

            if (userSess != null) {
                User userFromSession = userSess.getUser();
                boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

                if (isAccess) {
                    showNew = true;
                    showEdit = true;
                    showDelete = true;
                }
            }

            map.put("showNew", showNew);
            map.put("showEdit", showEdit);
            map.put("showDelete", showDelete);

            map.put("bookView", bookViewDto);

        }
    }

    public void showNewForm(CustomUserDetails userSess, ModelMap map) {

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            map.put("showNew", false);
            map.put("showEdit", false);
            map.put("showDelete", false);

            map.put("bookView", new BookViewDto());
            map.put("bookEdit", new BookFormDto());

        } else {
            throw new CustomAccessDeniedException("No rights to create the book.");
        }
    }

    public void showEditForm(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {
            Book book = bookRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("post"));

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());

                map.put("showNew", true);
                map.put("showEdit", true);
                map.put("showDelete", true);

                map.put("bookView", bookViewDto);
                map.put("bookEdit", new BookFormDto());

            } else {
                throw new CustomAccessDeniedException("No rights to edit the book.");
            }
        }
    }

    public void create(CustomUserDetails userSess, BookFormDto bookFormDto, MultipartFile bookFile) throws IOException {

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, new Book());
            uploadBook(bookFile, book);

        } else {
            throw new CustomAccessDeniedException("No rights to create the book.");
        }
    }

    public void update(CustomUserDetails userSess, BookFormDto bookFormDto, MultipartFile bookFile, Optional<Integer> id) throws IOException {

        if (id.isPresent()) {
            Book bookById = bookRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book"));

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, bookById);
                uploadBook(bookFile, book);

            } else {
                throw new CustomAccessDeniedException("No rights to update the book.");
            }
        }
    }

    public void delete(CustomUserDetails userSess, Optional<Integer> id) {

        if (id.isPresent()) {

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                bookRepository.deleteById(id.get());

            } else {
                throw new CustomAccessDeniedException("No rights to delete the book.");
            }
        }
    }

    private void uploadBook(MultipartFile bookFile, Book book) throws IOException {

        if (bookFile != null && !bookFile.isEmpty()) {
            String newUploadPath = uploadPath + PathRefactorerUtil.changeSeparator(urlSuffixBooks, "/", File.separator);
            String mainFileName = book.getTitle();
            String newFileName = FileUploaderUtil.uploadFile(bookFile, newUploadPath, mainFileName);
            book.setFileUrl(urlSuffixBooks + "/" + newFileName);
        }

        bookRepository.save(book);
    }

}
