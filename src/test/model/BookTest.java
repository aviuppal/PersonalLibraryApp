package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookTest {

    private Book testBook1;
    private Book testBook2;
    
    @BeforeEach
    void runBefore() {
        testBook1 = new Book("Harry Potter", "Fantasy", "J.K. Rowling");
        testBook2 = new Book("The Hunger Games", "Dystopian", "Suzanne Collins");

    }

    @Test
    void testConstructor() {
        assertEquals("Harry Potter", testBook1.getTitle());
        assertEquals("Fantasy", testBook1.getGenre());
        assertEquals("J.K. Rowling", testBook1.getAuthor());
        assertFalse(testBook1.getReadStatus());
        assertFalse(testBook1.getWishlistStatus());
        assertNull(testBook1.getRating());
    }

    @Test
    void testMarkAsRead() {
        testBook1.markAsRead();
        assertTrue(testBook1.getReadStatus());
        assertFalse(testBook1.getWishlistStatus());
    }

    @Test
    void testMarkWishlist() {
        testBook2.markWishlist(true);
        assertFalse(testBook2.getReadStatus());
        assertTrue(testBook2.getWishlistStatus());

        testBook2.markWishlist(false);
        assertFalse(testBook2.getReadStatus());
        assertFalse(testBook2.getWishlistStatus());
    }

    @Test
    void testSetRating() {
        testBook1.markAsRead();
        testBook1.setRating(1);
        assertTrue(testBook1.setRating(1));
        assertEquals(1, testBook1.getRating());

        testBook2.setRating(5);
        assertFalse(testBook2.setRating(5));
        assertNull(testBook2.getRating());
    }
}
