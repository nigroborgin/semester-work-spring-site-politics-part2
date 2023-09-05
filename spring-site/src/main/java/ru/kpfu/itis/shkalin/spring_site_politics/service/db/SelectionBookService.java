package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookViewAllDto;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;
import ru.kpfu.itis.shkalin.spring_site_politics.model.BookToFormatBook;
import ru.kpfu.itis.shkalin.spring_site_politics.model.SelectionBook;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.service.file_storage.StorageService;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.util.PathRefactorerUtil;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewForSelectionDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookViewOneDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.SelectionBookRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SelectionBookService {

    @Value("${upload.realpath}")
    private String uploadPath;

    @Autowired
    SelectionBookRepository selectionRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Autowired
    StorageService storageService;

    public void showAll(ModelMap map) {

        List<SelectionBookViewAllDto> bookSelectionsList = selectionRepository.findAllWithoutUser().stream()
                .map(bs -> (SelectionBookViewAllDto) ConverterUtil.updateAndReturn(bs, new SelectionBookViewAllDto()))
                .toList();

        map.put("bookSelections", bookSelectionsList);
    }

    public void showOne(Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        SelectionBook selectionBook = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));
        selectionBook.setBooks(
                selectionBook.getBooks().stream().distinct().toList()
        );

        SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto)
                ConverterUtil.updateAndReturn(selectionBook, new SelectionBookViewOneDto());

        List<BookViewForSelectionDto> bookViewDtoList = new ArrayList<>();
        for (Book book : selectionBook.getBooks()) {
            BookViewDto bookViewDto = bookService.conversionBookToViewDto(book);
            BookViewForSelectionDto bookViewForSelectionDto = (BookViewForSelectionDto)
                    ConverterUtil.updateAndReturn(bookViewDto, new BookViewForSelectionDto());
            bookViewForSelectionDto.setIsSelected(true);
            bookViewDtoList.add(bookViewForSelectionDto);
        }


//        List<BookViewForSelectionDto> bookViewDtoList = selectionBook.getBooks().stream()
//                .map(book -> {
//                    BookViewDto bookViewDto = bookService.conversionBookToViewDto(book);
//                    BookViewForSelectionDto bookViewForSelectionDto = (BookViewForSelectionDto)
//                            ConverterUtil.updateAndReturn(bookViewDto, new BookViewForSelectionDto());
//                    bookViewForSelectionDto.setIsSelected(true);
//                    return bookViewForSelectionDto;})
//                .toList();
        if (!bookViewDtoList.isEmpty()) {
            selectionBookViewOneDto.setBookList(bookViewDtoList);
        } else {
            selectionBookViewOneDto.setBookList(null);
        }

        map.addAttribute("selectionBookView", selectionBookViewOneDto);
    }

    public void showNewForm(ModelMap map) {

        SelectionBookViewOneDto selectionBookViewOneDto = new SelectionBookViewOneDto();
        List<BookViewForSelectionDto> bookViewDtoList = bookRepository.findAll().stream()
                .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                .toList();
        selectionBookViewOneDto.setBookList(bookViewDtoList);

        map.put("selectionBookView", selectionBookViewOneDto);
        map.put("selectionBookEdit", new SelectionBookFormDto());
    }

    public void showEditForm(Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        SelectionBook selectionBook = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));
        SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto)
                ConverterUtil.updateAndReturn(selectionBook, new SelectionBookViewOneDto());

        List<BookViewForSelectionDto> allBooks = bookRepository.findAll().stream()
                .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                .toList();

        for (BookViewForSelectionDto bookDto : allBooks) {
            for (Book bookFromSelection : selectionBook.getBooks()) {
                if (bookDto.getId().equals(bookFromSelection.getId())) {
                    bookDto.setIsSelected(true);
                }
            }
        }

        selectionBookViewOneDto.setBookList(allBooks);

        map.put("selectionBookView", selectionBookViewOneDto);
        map.put("selectionBookEdit", new SelectionBookFormDto());

    }

    public void create(CustomUserDetails userSess, SelectionBookFormDto selectionBookFormDto) {

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            SelectionBook newSelectionBook = (SelectionBook)
                    ConverterUtil.updateAndReturn(selectionBookFormDto, new SelectionBook());

            selectionBookFormDto.getBookIdList().stream()
                    .forEach(bookId -> {
                        Book bookForSelection = Book.builder().id(bookId).build();
                        newSelectionBook.addBook(bookForSelection);
                    });

            selectionRepository.save(newSelectionBook);

        } else {
            throw new CustomAccessDeniedException("No rights to create the book.");
        }
    }

    public void update(CustomUserDetails userSess, SelectionBookFormDto newData, Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            SelectionBook selectionBookById = selectionRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("selection-book"));
            selectionBookById.getBooks().clear();

            ConverterUtil.update(newData, selectionBookById);
            List<Book> booksByIds = bookRepository.findAllById(newData.getBookIdList());
            selectionBookById.setBooks(booksByIds);

            selectionRepository.save(selectionBookById);

        } else {
            throw new CustomAccessDeniedException("No rights to update the post.");
        }
    }

    public void delete(CustomUserDetails userSess, Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            selectionRepository.deleteById(id.get());

        } else {
            throw new CustomAccessDeniedException("No rights to delete the selection-book.");
        }
    }

    public String download(Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        Optional<SelectionBook> selectionById = selectionRepository.findById(id.get());
        List<Book> books = selectionById.get().getBooks();
        List<String> bookFilesystemNames = books.stream()
                .map(b -> PathRefactorerUtil.getFileNameByUrl("/",
                        /*todo: сделать выбор формата при скачивании подборки*/
                        b.getFormats().get(0).getUrl()))
                .toList();

        String archiveName;
        try {
            archiveName = storageService.createArchive(bookFilesystemNames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return archiveName;
    }

    public void showAllMy(CustomUserDetails userSess, ModelMap map) {

        List<SelectionBookViewAllDto> bookSelectionsList = null;
        if (userSess != null) {
            bookSelectionsList = selectionRepository.findAllByUser(userSess.getUser().getId()).stream()
                    .map(bs -> (SelectionBookViewAllDto) ConverterUtil.updateAndReturn(bs, new SelectionBookViewAllDto()))
                    .toList();
        }

        map.addAttribute("bookSelections", bookSelectionsList);
    }

    public void showOneMy(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        SelectionBook selectionBook = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));
        if (!selectionBook.getUser().equals(userSess.getUser())) {
            throw new NotFoundException("book-selection");
        }

        SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto)
                ConverterUtil.updateAndReturn(selectionBook, new SelectionBookViewOneDto());

        List<BookViewForSelectionDto> bookViewDtoList = selectionBook.getBooks().stream()
                .map(book -> {
                    BookViewDto bookViewDto = bookService.conversionBookToViewDto(book);
                    BookViewForSelectionDto bookViewForSelectionDto = (BookViewForSelectionDto)
                            ConverterUtil.updateAndReturn(bookViewDto, new BookViewForSelectionDto());
                    bookViewForSelectionDto.setIsSelected(true);
                    return bookViewForSelectionDto;})
                .toList();

        if (!bookViewDtoList.isEmpty()) {
            selectionBookViewOneDto.setBookList(bookViewDtoList);
        } else {
            selectionBookViewOneDto.setBookList(null);
        }

        map.addAttribute("selectionBookView", selectionBookViewOneDto);
    }

    public void showNewFormMy(ModelMap map) {

        SelectionBookViewOneDto selectionBookViewOneDto = new SelectionBookViewOneDto();
        List<BookViewForSelectionDto> bookViewDtoList = bookRepository.findAll().stream()
                .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                .toList();
        selectionBookViewOneDto.setBookList(bookViewDtoList);

        map.put("selectionBookView", selectionBookViewOneDto);
        map.put("selectionBookEdit", new SelectionBookFormDto());
    }

    public void showEditFormMy(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        User userFromSession = userSess.getUser();
        SelectionBook selectionBook = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));

        User userFromSelection = selectionBook.getUser();
        boolean isAccess = false;
        if (userFromSelection != null) {
            isAccess = Objects.equals(userFromSession.getId(), userFromSelection.getId());
        }

        if (isAccess) {

            SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto)
                    ConverterUtil.updateAndReturn(selectionBook, new SelectionBookViewOneDto());

            List<BookViewForSelectionDto> allBooks = bookRepository.findAll().stream()
                    .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                    .toList();

            for (BookViewForSelectionDto bookDto : allBooks) {
                for (Book bookFromSelection : selectionBook.getBooks()) {

                    if (bookDto.getId().equals(bookFromSelection.getId())) {
                        bookDto.setIsSelected(true);
                    }
                }
            }

            selectionBookViewOneDto.setBookList(allBooks);

            map.put("selectionBookView", selectionBookViewOneDto);
            map.put("selectionBookEdit", new SelectionBookFormDto());

        } else {
            throw new CustomAccessDeniedException("No rights to edit the book-selection.");
        }
    }

    public void createMy(CustomUserDetails userSess, SelectionBookFormDto selectionBookFormDto) {

        User userFromSession = userSess.getUser();

        SelectionBook newSelectionBook = (SelectionBook)
                ConverterUtil.updateAndReturn(selectionBookFormDto, new SelectionBook());
        List<Integer> bookIdList = selectionBookFormDto.getBookIdList();
        if (bookIdList != null) {
            bookIdList.stream()
                    .forEach(bookId -> {
                        Book bookForSelection = Book.builder().id(bookId).build();
                        newSelectionBook.addBook(bookForSelection);
                    });
        }
        newSelectionBook.setUser(userFromSession);
        selectionRepository.save(newSelectionBook);
    }

    public void updateMy(CustomUserDetails userSess, SelectionBookFormDto selectionBookFormDto, Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        User userFromSession = userSess.getUser();
        SelectionBook selectionBookById = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));

        User userFromSelection = selectionBookById.getUser();
        boolean isAccess = false;
        if (userFromSelection != null) {
            isAccess = Objects.equals(userFromSession.getId(), userFromSelection.getId());
        }

        if (isAccess) {

            selectionBookById.getBooks().clear();

            ConverterUtil.update(selectionBookFormDto, selectionBookById);
            List<Integer> bookIdList = selectionBookFormDto.getBookIdList();
            if (bookIdList != null && !bookIdList.isEmpty()) {
                List<Book> booksByIds = bookRepository.findAllById(bookIdList);
                selectionBookById.setBooks(booksByIds);
            }


            selectionRepository.save(selectionBookById);

        } else {
            throw new CustomAccessDeniedException("No rights to update the post.");
        }
    }

    public void deleteMy(CustomUserDetails userSess, Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        User userFromSession = userSess.getUser();
        SelectionBook selectionBookById = selectionRepository.findById(id.get())
                .orElseThrow(() -> new NotFoundException("book-selection"));

        User userFromSelection = selectionBookById.getUser();
        boolean isAccess = false;
        if (userFromSelection != null) {
            isAccess = Objects.equals(userFromSession.getId(), userFromSelection.getId());
        }

        if (isAccess) {
            selectionRepository.deleteById(id.get());

        } else {
            throw new CustomAccessDeniedException("No rights to delete the selection-book.");
        }
    }

    public String downloadMy(CustomUserDetails userSess, Optional<Integer> id) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        String result = null;
        if (userSess != null) {

            User userFromSession = userSess.getUser();
            SelectionBook selectionBookById = selectionRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book-selection"));
            User userFromSelection = selectionBookById.getUser();

            boolean isAccess = false;
            if (userFromSelection != null) {
                isAccess = Objects.equals(userFromSession.getId(), userFromSelection.getId());
            }

            if (isAccess) {
                result = download(id);
            }
        }
        return result;
    }

    public void showNewFormWithNewData(SelectionBookFormDto selectionBookFormDto, ModelMap modelMap) {
        SelectionBookViewOneDto selectionBookViewOneDto = getDtoWithNewData(selectionBookFormDto);
        modelMap.addAttribute("selectionBookView", selectionBookViewOneDto);
        modelMap.addAttribute("selectionBookEdit", new SelectionBookFormDto());
    }

    private SelectionBookViewOneDto getDtoWithNewData(SelectionBookFormDto selectionBookFormDto) {
        SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto)
                ConverterUtil.updateAndReturn(selectionBookFormDto, new SelectionBookViewOneDto());
        List<BookViewForSelectionDto> allBooks = bookRepository.findAll().stream()
                .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                .toList();

        List<Integer> bookIdList = selectionBookFormDto.getBookIdList();
        if (bookIdList != null && !bookIdList.isEmpty()) {
            for (BookViewForSelectionDto bookDto : allBooks) {
                for (Integer idBook : bookIdList) {
                    if (bookDto.getId().equals(idBook)) {
                        bookDto.setIsSelected(true);
                    }
                }
            }
        }
        selectionBookViewOneDto.setBookList(allBooks);
        return selectionBookViewOneDto;
    }

    public void showEditFormWithNewData(Optional<Integer> id, SelectionBookFormDto selectionBookFormDto, ModelMap modelMap) {

        id.orElseThrow(() -> new IllegalArgumentException("ID of book-selection is not present"));

        SelectionBookViewOneDto selectionBookViewOneDto = getDtoWithNewData(selectionBookFormDto);
        selectionBookViewOneDto.setId(id.get());
        modelMap.addAttribute("selectionBookView", selectionBookViewOneDto);
        modelMap.addAttribute("selectionBookEdit", new SelectionBookFormDto());

    }


}
