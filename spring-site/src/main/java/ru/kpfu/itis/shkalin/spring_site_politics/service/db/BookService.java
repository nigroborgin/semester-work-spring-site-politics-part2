package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.trivee.fb2pdf.FB2toPDF;
import org.trivee.fb2pdf.FB2toPDFException;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.FormatBookDto;
import ru.kpfu.itis.shkalin.spring_site_politics.model.BookToFormatBook;
import ru.kpfu.itis.shkalin.spring_site_politics.model.FormatBook;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookToFormatBookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.FormatBookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage.StorageService;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.PathRefactorerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {

    @Value("${upload.url.suffix.books}")
    private String urlSuffixBooks;
    @Value("${upload.url.suffix}")
    private String urlSuffixMain;
    @Value("${upload.realpath}")
    private String realPath;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    FormatBookRepository formatBookRepository;
    @Autowired
    BookToFormatBookRepository bookToFormatBookRepository;
    @Autowired
    StorageService storageService;

    public void showAll(CustomUserDetails userSess, ModelMap map) {

        List<BookViewDto> bookList = bookRepository.findAll().stream()
                .map(this::convertionBookToViewDto)
                .toList();

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

        map.put("bookList", bookList);
    }

    public void showOne(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            Book book = bookRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book"));
            BookViewDto bookViewDto = convertionBookToViewDto(book);
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
                    .orElseThrow(() -> new NotFoundException("book"));

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

    public void create(CustomUserDetails userSess, BookFormDto bookFormDto, MultipartFile bookFile) throws IOException, DocumentException, FB2toPDFException {

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, new Book());
            uploadBookFile(bookFile, book);
            Book savedBook = bookRepository.save(book);
            bookToFormatBookRepository.saveAll(savedBook.getFormats());

        } else {
            throw new CustomAccessDeniedException("No rights to create the book.");
        }
    }

    public void update(CustomUserDetails userSess, BookFormDto bookFormDto, MultipartFile bookFile, Optional<Integer> id)
            throws IOException, DocumentException, FB2toPDFException {

        if (id.isPresent()) {
            Book bookById = bookRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book"));

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, bookById);
                uploadBookFile(bookFile, book);
                Book savedBook = bookRepository.save(book);
                bookToFormatBookRepository.saveAll(savedBook.getFormats());

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

    private void uploadBookFile(MultipartFile bookFile, Book book)
            throws DocumentException, IOException, FB2toPDFException {
        if (bookFile != null && !bookFile.isEmpty()) {
            String filename = book.getTitle();

            String newFullFileName = storageService.saveResource(bookFile, "books", filename);
            String newFileNameWithoutExtension = PathRefactorerUtil.getFileNameWithoutExtension(newFullFileName);
            String fileExtension = PathRefactorerUtil.getExtension(bookFile);
            String pathPattern = urlSuffixMain + urlSuffixBooks + "/";

            List<BookToFormatBook> formats = book.getFormats();
            if (formats == null) {
                formats = new ArrayList<>();
            }

            FormatBook extensionFormat = formatBookRepository.findByName(fileExtension).get(0);
            if (extensionFormat == null) {
                extensionFormat = formatBookRepository.save(new FormatBook(fileExtension));
            }

            formats.add(new BookToFormatBook(
                    book, extensionFormat, pathPattern + newFullFileName));

            if (Objects.equals(fileExtension, "fb2")) {
                FB2toPDF.translate(
                        realPath + File.separator + "books" + File.separator + newFullFileName,
                        realPath + File.separator + "books" + File.separator + newFileNameWithoutExtension + ".pdf");
                FormatBook pdfFormat = formatBookRepository.findByName("pdf").get(0);
                formats.add(new BookToFormatBook(
                        book, pdfFormat, pathPattern + newFileNameWithoutExtension + ".pdf"));
            }

            book.setFormats(formats);
        }
    }

    private BookViewDto convertionBookToViewDto(Book book) {
        BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());
        bookViewDto.setFormatsOfBook(
                book.getFormats().stream()
                        .map(f -> new FormatBookDto(f.getFormat().getName(), f.getUrl()))
                        .toList());
        return bookViewDto;
    }

}
