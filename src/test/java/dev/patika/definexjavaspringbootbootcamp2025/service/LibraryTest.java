package dev.patika.definexjavaspringbootbootcamp2025.service;

import dev.patika.definexjavaspringbootbootcamp2025.entity.Book;
import dev.patika.definexjavaspringbootbootcamp2025.entity.Member;
import dev.patika.definexjavaspringbootbootcamp2025.exception.BookDoesNotExistsException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.BookIsNotAvailableException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.ISBNAlreadyExistException;
import dev.patika.definexjavaspringbootbootcamp2025.exception.MemberDoesNotHaveTheBookException;
import junit.framework.TestCase;

public class LibraryTest extends TestCase {
    Library library;

    public void setUp() {


        //Library library = new LibraryServiceImpl();

        this.library = new LibraryServiceImpl();

        /* #region BURADA_DEGISIKLIK_YAPMAYIN */
        Book existingAvailableBook = new Book();
        existingAvailableBook.setAuthor("Robert C. Martin");
        existingAvailableBook.setIsbn("9780132350884");
        existingAvailableBook.setAvailable(true);
        existingAvailableBook.setTitle("Clean Code: A Handbook of Agile Software Craftsmanship");
        library.addBook(existingAvailableBook);

        Book existingAvailableBook2 = new Book();
        existingAvailableBook2.setAuthor("Chris Richardson");
        existingAvailableBook2.setIsbn("1617294543");
        existingAvailableBook2.setAvailable(true);
        existingAvailableBook2.setTitle("Microservices Patterns: With Examples in Java");
        library.addBook(existingAvailableBook2);
        /* #endregion */
    }

    public void testAddBook_Success() {
        Book bmBook = new Book();
        bmBook.setAuthor("Sam Newman");
        bmBook.setIsbn("1492034029");
        bmBook.setAvailable(true);
        bmBook.setTitle("Building Microservices: Designing Fine-Grained Systems");

        boolean expectedResult = true;
        boolean actualResult = library.addBook(bmBook);
        assertEquals(expectedResult, actualResult);
    }

    public void testAddBook_WhenIsbnAlreadyExist_ThrowISBNAlreadyExistException() {
        try {
            Book bmBook = new Book();
            bmBook.setAuthor("Sam Newman");
            bmBook.setIsbn("1492034029");
            bmBook.setAvailable(true);
            bmBook.setTitle("Building Microservices: Designing Fine-Grained Systems");
            boolean expectedResult = true;
            boolean actualResult = library.addBook(bmBook);
            assertEquals(expectedResult, actualResult);

            library.addBook(bmBook);
        } catch (ISBNAlreadyExistException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        fail("did not throw ISBNAlreadyExistException");
    }

    public void testUpdateBook_Success() {
        Book bmBook = new Book();
        bmBook.setAuthor("Sam Newman");
        bmBook.setIsbn("1492034029");
        bmBook.setAvailable(true);
        bmBook.setTitle("Building Microservices: Designing Fine-Grained Systems");

        library.addBook(bmBook);

        boolean expectedResult = true;
        boolean actualResult = library.updateBook(bmBook);
        assertEquals(expectedResult, actualResult);
    }

    public void testUpdateBook_WhenBookDoesNotExist_ThrowBookDoesNotExistsException() {
        try {
            Book notExistingBook = new Book();
            notExistingBook.setAuthor("Ali Veli");
            notExistingBook.setIsbn("0000000000");
            notExistingBook.setAvailable(true);
            notExistingBook.setTitle("Bla Bla");

            this.library.updateBook(notExistingBook);
        } catch (BookDoesNotExistsException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        fail("did not throw BookDoesNotExistsException");
    }

    public void testRemoveBook_Success() {
        String bookISBNToRemove = "1617294543";
        boolean expectedResult = true;
        boolean actualResult = this.library.removeBook(bookISBNToRemove);
        assertEquals(expectedResult, actualResult);
    }

    public void testRemoveBook_WhenBookDoesNotExist_ThrowBookDoesNotExistsException() {
        try {
            String bookISBNToRemove = "0000000000";
            this.library.removeBook(bookISBNToRemove);
        } catch (BookDoesNotExistsException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        fail("did not throw BookDoesNotExistsException");
    }

    public void testLendBookToMember_Success() {
        Member member = new Member();
        member.setId("00000000001");
        member.setName("Jane");
        member.setSurname("Doe");

        String bookISBNToLend = "9780132350884";
        boolean expectedResult = true;
        boolean actualResult = this.library.lendBookToMember(bookISBNToLend, member);
        assertEquals(expectedResult, actualResult);
    }

    public void testLendBookToMember_WhenBookIsNotAvailable_ThrowBookIsNotAvailableException() {
        try {
            Member firstMember = new Member();
            firstMember.setId("00000000001");
            firstMember.setName("Jane");
            firstMember.setSurname("Doe");

            String bookISBNToLend = "9780132350884";
            this.library.lendBookToMember(bookISBNToLend, firstMember);

            Member secondMember = new Member();
            secondMember.setId("00000000002");
            secondMember.setName("John");
            secondMember.setSurname("Doe");

             this.library.lendBookToMember(bookISBNToLend, secondMember);
        } catch (BookIsNotAvailableException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        fail("did not throw BookIsNotAvailableException");
    }

    public void testRetrieveBookFromMember_Success() {
        Member member = new Member();
        member.setId("00000000001");
        member.setName("Jane");
        member.setSurname("Doe");

        String bookISBNToRetrieve = "9780132350884";
        this.library.lendBookToMember(bookISBNToRetrieve, member);

        boolean expectedResult = true;
        boolean actualResult = this.library.retrieveBookFromMember(bookISBNToRetrieve, member);
        assertEquals(expectedResult, actualResult);
    }

    public void testRetrieveBookFromMember_WhenMemberHasNotTheBook_ThrowMemberDoesNotHaveTheBookException() {
        try {
            Member member = new Member();
            member.setId("00000000002");
            member.setName("John");
            member.setSurname("Doe");

            String bookISBNToRetrieve = "9780132350884";
            this.library.retrieveBookFromMember(bookISBNToRetrieve, member);
        } catch (MemberDoesNotHaveTheBookException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        fail("did not throw MemberDoesNotHaveTheBookException");
    }
}