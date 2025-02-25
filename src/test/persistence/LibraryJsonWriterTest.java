package persistence;

import model.Library;
import model.Book;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test code modeled from JsonSerializationDemo
class LibraryJsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            LibraryJsonWriter writer = new LibraryJsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLibrary() {
        try {
            Library library = new Library();
            LibraryJsonWriter writer = new LibraryJsonWriter("./data/testWriterEmptyLibrary.json");
            writer.open();
            writer.write(library);
            writer.close();

            LibraryJsonReader reader = new LibraryJsonReader("./data/testWriterEmptyLibrary.json");
            library = reader.read();
            assertEquals(0, library.getBookNum());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralLibrary() {
        try {
            Library library = new Library();
            library.addBookToLibrary("testBook C", "Science Fiction", "testAuthor C");
            library.addBookToLibrary("testBook D", "Fantasy", "testAuthor D");
            library.markWishlist(0, true);
            library.markRead(1);
            library.rateBook(1, 5);
            LibraryJsonWriter writer = new LibraryJsonWriter("./data/testWriterGeneralLibrary.json");
            writer.open();
            writer.write(library);
            writer.close();

            LibraryJsonReader reader = new LibraryJsonReader("./data/testWriterGeneralLibrary.json");
            library = reader.read();
            List<Book> bookList = library.getBookList();
            assertEquals(2, bookList.size());
            checkBook("testBook C", "Science Fiction", "testAuthor C", false, true, null, bookList.get(0));
            checkBook("testBook D", "Fantasy", "testAuthor D", true, false, 5, bookList.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}