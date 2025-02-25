package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LibraryTest {
    private Book testBook1; 
    private Book testBook2;
    private Library testLibrary;

    @BeforeEach
    void runBefore() {
        testLibrary = new Library();
        testBook1 = new Book("testTitle1", "testGenre1", "testAuthor1");
        testBook2 = new Book("testTitle2", "testGenre2", "testAuthor2");
    }

    @Test
    void testConstructor() {
        assertEquals(0, testLibrary.getBookNum());
        assertTrue(testLibrary.emptyLibrary());
    }

    @Test
    void testAddBookToLibrary() {
        testLibrary.addBookToLibrary("testTitle1", "testGenre1", "testAuthor1");
        assertEquals(1, testLibrary.getBookNum());
        assertEquals("testTitle1", testLibrary.getTitle(0));
        assertEquals("testGenre1", testLibrary.getGenre(0));
        assertEquals("testAuthor1", testLibrary.getAuthor(0));
    }

    @Test
    void testEmptyLibrary() {
        assertTrue(testLibrary.emptyLibrary());
    }

    @Test
    void testDeletedBookFromLibrary() {
        testLibrary.addBookToLibrary("title", "genre", "author");
        assertTrue(testLibrary.deleteBookFromLibrary("title"));
        assertEquals(0, testLibrary.getBookNum());
        testLibrary.addBookToLibrary("title2", "genre2", "author2");
        assertFalse(testLibrary.deleteBookFromLibrary("title4"));
        assertEquals(1, testLibrary.getBookNum());
    }

    @Test
    void testMakeWishlist() {
        List<Book> testBookList = new ArrayList<>();
        testLibrary.addBookToLibrary("testTitle", "testGenre", "testAuthor");
        testLibrary.getBook(0).markWishlist(true);
        testLibrary.makeWishlist();
        testBookList.add(testLibrary.getBook(0));
        assertEquals(testBookList, testLibrary.getWishlist());
    }

    @Test
    void testMakeWishlistFalse() {
        List<Book> testBookList = new ArrayList<>();
        testLibrary.addBookToLibrary("testTitle", "testGenre", "testAuthor");
        testLibrary.makeWishlist();
        assertEquals(testBookList, testLibrary.getWishlist());
    }

    @Test
    void testNoneInWishlist() {
        List<Book> testBookList = new ArrayList<>();
        testLibrary.makeWishlist();
        assertEquals(testBookList, testLibrary.getWishlist());
    }

    @Test
    void testMakeReadList() {
        List<Book> testBookList = new ArrayList<>();
        testLibrary.addBookToLibrary("testTitle", "testGenre", "testAuthor");
        testLibrary.getBook(0).markAsRead();
        testLibrary.makeReadList();
        testBookList.add(testLibrary.getBook(0));
        assertEquals(testBookList, testLibrary.getReadBooks());
    }

    @Test
    void testNoneRead() {
        List<Book> testBookList = new ArrayList<>();
        testLibrary.addBookToLibrary("testTitle", "testGenre", "testAuthor");
        testLibrary.makeReadList();
        assertEquals(testBookList, testLibrary.getReadBooks());
    }

    @Test
    void testMarkWishlist() {
        testLibrary.addBookToLibrary("title1", "genre1", "author1");
        testLibrary.addBookToLibrary("title2", "genre2", "author2");
        testLibrary.markWishlist(0, true);
        assertTrue(testLibrary.getWishlistStatus(0));
        assertFalse(testLibrary.getWishlistStatus(1));
    }

    @Test
    void testMarkRead() {
        testLibrary.addBookToLibrary("title1", "genre1", "author1");
        testLibrary.addBookToLibrary("title2", "genre2", "author2");
        testLibrary.markRead(0);
        assertTrue(testLibrary.getReadStatus(0));
        assertFalse(testLibrary.getReadStatus(1));
    }

    @Test
    void testRateBook() {
        testLibrary.addBookToLibrary("title1", "genre1", "author1");
        testLibrary.addBookToLibrary("title2", "genre2", "author2");
        testLibrary.getBook(0).markAsRead();
        testLibrary.getBook(1).markAsRead();
        testLibrary.rateBook(0, 3);
        testLibrary.rateBook(1, 5);
        assertEquals(3, testLibrary.getRating(0));
        assertEquals(5, testLibrary.getRating(1));
    }

    @Test
    void testAddBook() {
        testLibrary.addBook(testBook1);
        assertEquals(1, testLibrary.getBookNum());
        assertEquals(testBook1, testLibrary.getBook(0));
    }

    @Test
    void testFindBookIndex() {
        testLibrary.addBook(testBook1);
        testLibrary.addBook(testBook2);
        assertEquals(0, testLibrary.findBookIndex(testBook1.getTitle()));
        assertEquals(1, testLibrary.findBookIndex(testBook2.getTitle()));
        assertEquals(null, testLibrary.findBookIndex("title"));
    }
}
