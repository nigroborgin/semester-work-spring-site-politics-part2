package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.trivee.fb2pdf.FB2toPDF;
import org.trivee.fb2pdf.FB2toPDFException;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.FormatBookDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;
import ru.kpfu.itis.shkalin.spring_site_politics.model.BookToFormatBook;
import ru.kpfu.itis.shkalin.spring_site_politics.model.FormatBook;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookToFormatBookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.FormatBookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage.StorageService;
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

    public void showAll(ModelMap map) {

        List<BookViewDto> bookList = bookRepository.findAll().stream()
                .map(this::conversionBookToViewDto)
                .toList();

        map.put("bookList", bookList);
    }

    public void showOne(Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book is not present"));

        Book book = bookRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book"));
        BookViewDto bookViewDto = conversionBookToViewDto(book);

        map.put("bookView", bookViewDto);
    }

    public void showNewForm(ModelMap modelMap) {
        modelMap.put("bookView", new BookViewDto());
        modelMap.put("bookEdit", new BookFormDto());
    }

    public void showEditForm(Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book is not present"));

        Book book = bookRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book"));
        BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());

        map.put("bookView", bookViewDto);
        map.put("bookEdit", new BookFormDto());

    }

    public void showEditFormWithNewData(Optional<Integer> id, BookFormDto bookFormDto, ModelMap map) {
        id.orElseThrow(() -> new IllegalArgumentException("ID of book is not present"));

        Book book = bookRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book"));
        BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());
        ConverterUtil.update(bookFormDto, bookViewDto);

        map.put("bookView", bookViewDto);
        map.put("bookEdit", new BookFormDto());
    }

    public void create(BookFormDto bookFormDto, MultipartFile bookFile) throws IOException, DocumentException, FB2toPDFException {

        Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, new Book());
        uploadBookFile(bookFile, book);
        Book savedBook = bookRepository.save(book);
        bookToFormatBookRepository.saveAll(savedBook.getFormats());
    }

    public void update(BookFormDto bookFormDto, MultipartFile bookFile, Optional<Integer> id)
            throws IOException, DocumentException, FB2toPDFException {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book is not present"));

        Book bookById = bookRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book"));

        Book book = (Book) ConverterUtil.updateAndReturn(bookFormDto, bookById);
        uploadBookFile(bookFile, book);
        Book savedBook = bookRepository.save(book);
        bookToFormatBookRepository.saveAll(savedBook.getFormats());
    }

    public void delete(Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book is not present"));

        bookRepository.deleteById(id.get());
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

    private BookViewDto conversionBookToViewDto(Book book) {
        BookViewDto bookViewDto = (BookViewDto) ConverterUtil.updateAndReturn(book, new BookViewDto());
        bookViewDto.setFormatsOfBook(
                book.getFormats().stream()
                        .map(f -> new FormatBookDto(f.getFormat().getName(), f.getUrl()))
                        .toList());
        return bookViewDto;
    }

}
