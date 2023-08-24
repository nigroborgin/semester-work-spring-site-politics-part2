package ru.kpfu.itis.shkalin.spring_site_politics.service.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewForSelectionDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookFormDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookViewAllDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book.SelectionBookViewOneDto;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.CustomAccessDeniedException;
import ru.kpfu.itis.shkalin.spring_site_politics.exception.NotFoundException;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Post;
import ru.kpfu.itis.shkalin.spring_site_politics.model.SelectionBook;
import ru.kpfu.itis.shkalin.spring_site_politics.model.User;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.BookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.repository.SelectionBookRepository;
import ru.kpfu.itis.shkalin.spring_site_politics.security.CustomUserDetails;
import ru.kpfu.itis.shkalin.spring_site_politics.util.ConverterUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SelectionBookService {

    @Autowired
    SelectionBookRepository selectionRepository;

    @Autowired
    BookRepository bookRepository;

    public void showAll(CustomUserDetails userSess, ModelMap map) {

        List<SelectionBookViewAllDto> bookSelectionsList = selectionRepository.findAll().stream()
                .map(bs -> (SelectionBookViewAllDto) ConverterUtil.updateAndReturn(bs, new SelectionBookViewAllDto()))
                .toList();

        boolean showNew = false;
        boolean showEdit = false;
        boolean showDelete = false;

        if (userSess != null) {
            boolean isAccess = Objects.equals(userSess.getUser().getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                showNew = true;
                showEdit = true;
                showDelete = true;
            }
        }

        map.put("showNew", showNew);
        map.put("showEdit", showEdit);
        map.put("showDelete", showDelete);

        map.put("bookSelections", bookSelectionsList);
    }

    public void showOne(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            SelectionBook selectionBook = selectionRepository.findById(id.get())
                    .orElseThrow(() -> new NotFoundException("book-selection"));
            SelectionBookViewOneDto selectionBookViewOneDto = (SelectionBookViewOneDto) ConverterUtil.updateAndReturn(selectionBook, new SelectionBookViewOneDto());
            List<BookViewForSelectionDto> bookViewDtoList = selectionBook.getBooks().stream()
                    .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                    .toList();
            if (bookViewDtoList.size() > 0) {
                selectionBookViewOneDto.setBookList(bookViewDtoList);
            } else {
                selectionBookViewOneDto.setBookList(null);
            }


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

            map.put("selectionBookView", selectionBookViewOneDto);

        }
    }

    public void showNewForm(CustomUserDetails userSess, ModelMap map) {

        User userFromSession = userSess.getUser();
        boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

        if (isAccess) {
            map.put("showNew", false);
            map.put("showEdit", false);
            map.put("showDelete", false);

            SelectionBookViewOneDto selectionBookViewOneDto = new SelectionBookViewOneDto();
            List<BookViewForSelectionDto> bookViewDtoList = bookRepository.findAll().stream()
                    .map(b -> (BookViewForSelectionDto) ConverterUtil.updateAndReturn(b, new BookViewForSelectionDto()))
                    .toList();
            selectionBookViewOneDto.setBookList(bookViewDtoList);

            map.put("selectionBookView", selectionBookViewOneDto);
            map.put("selectionBookEdit", new SelectionBookFormDto());

        } else {
            throw new CustomAccessDeniedException("No rights to create the book-selection.");
        }
    }

    public void showEditForm(CustomUserDetails userSess, Optional<Integer> id, ModelMap map) {

        if (id.isPresent()) {

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {

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

                map.put("showNew", true);
                map.put("showEdit", true);
                map.put("showDelete", true);

                map.put("selectionBookView", selectionBookViewOneDto);
                map.put("selectionBookEdit", new SelectionBookFormDto());

            } else {
                throw new CustomAccessDeniedException("No rights to edit the book-selection.");
            }
        }
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

        if (id.isPresent()) {
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

    }

    public void delete(CustomUserDetails userSess, Optional<Integer> id) {
        if (id.isPresent()) {

            User userFromSession = userSess.getUser();
            boolean isAccess = Objects.equals(userFromSession.getRole().getName(), "ROLE_ADMIN");

            if (isAccess) {
                selectionRepository.deleteById(id.get());

            } else {
                throw new CustomAccessDeniedException("No rights to delete the selection-book.");
            }
        }
    }

}
