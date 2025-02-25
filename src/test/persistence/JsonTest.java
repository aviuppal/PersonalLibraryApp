package persistence;

import model.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Test code modeled from JsonSerializationDemo
public class JsonTest {
    protected void checkBook(String title, String genre, String author, 
            boolean isRead, boolean isOnWishlist, Integer rating, Book book) {
        assertEquals(title, book.getTitle());
        assertEquals(genre, book.getGenre());
        assertEquals(author, book.getAuthor());
        assertEquals(isRead, book.getReadStatus());
        assertEquals(isOnWishlist, book.getWishlistStatus());
        assertEquals(rating, book.getRating());
    }
}