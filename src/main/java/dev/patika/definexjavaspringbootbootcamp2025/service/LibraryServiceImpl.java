package dev.patika.definexjavaspringbootbootcamp2025.service;

import dev.patika.definexjavaspringbootbootcamp2025.entity.Book;
import dev.patika.definexjavaspringbootbootcamp2025.entity.Member;
import dev.patika.definexjavaspringbootbootcamp2025.exception.BookDoesNotExistsException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.BookIsNotAvailableException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.ISBNAlreadyExistException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.MemberDoesNotHaveTheBookException;

import java.util.ArrayList;
import java.util.List;

public class LibraryServiceImpl extends Library {

    private final List<Book> books;

    public LibraryServiceImpl() {
        this.books = new ArrayList<>();
    }




    @Override
    public boolean addBook(Book book) throws ISBNAlreadyExistException {
        if( book == null || book.getTitle() == null || book.getAuthor() == null || book.getIsbn() == null ){
            return false;
        }

        for ( Book b: books){
            if(b.getIsbn().equals(book.getIsbn())){
                throw new ISBNAlreadyExistException();
            }
        }
        return books.add(book);

    }

    @Override
    public boolean updateBook(Book book) throws BookDoesNotExistsException {
        if( book == null || book.getTitle() == null || book.getAuthor() == null || book.getIsbn() == null ){
            return false;
        }

        return books.stream()
                .filter(b -> book.getIsbn().equals(b.getIsbn()))
                .findFirst()
                .map(b -> {
                    b.setIsbn(book.getIsbn());
                    b.setTitle(book.getTitle());
                    b.setAuthor(book.getAuthor());
                    b.setAvailable(book.isAvailable());
                    return true;
                })
                .orElseThrow(BookDoesNotExistsException::new);

    }

    @Override
    public boolean removeBook(String isbn) throws BookDoesNotExistsException {
        if(isbn == null) {
            return false;
        }
        return books.stream()
                .filter(b -> isbn.equals(b.getIsbn()))
                .findFirst()
                .map(books::remove)
                .orElseThrow(BookDoesNotExistsException::new);

    }

    @Override
    public boolean lendBookToMember(String isbn, Member member) throws BookIsNotAvailableException {
        if(isbn == null || member == null || member.getId() == null) {
            return false;
        }
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .map(book -> {
                    if (!book.isAvailable()) {
                        throw new BookIsNotAvailableException();
                    }
                    book.setAvailable(false);
                    return true;
                })
                .orElseThrow(BookDoesNotExistsException::new);

    }

    @Override
    public boolean retrieveBookFromMember(String isbn, Member member) throws MemberDoesNotHaveTheBookException {
        if(isbn == null || member == null || member.getId() == null) {
            return false;
        }
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .map(book -> {
                    if (book.isAvailable()) {
                        throw new MemberDoesNotHaveTheBookException();
                    }
                    book.setAvailable(true);
                    return true;
                })
                .orElseThrow(BookDoesNotExistsException::new);

    }

    @Override
    public Book getBook(String isbn) throws BookDoesNotExistsException {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElseThrow(BookDoesNotExistsException::new);

    }
}
