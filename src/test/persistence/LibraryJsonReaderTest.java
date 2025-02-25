package persistence;

import model.Library;
import model.Book;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test code modeled from JsonSerializationDemo
class LibraryJsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        LibraryJsonReader reader = new LibraryJsonReader("./data/noSuchFile.json");
        try {
            Library library = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyLibrary() {
        LibraryJsonReader reader = new LibraryJsonReader("./data/testReaderEmptyLibrary.json");
        try {
            Library library = reader.read();
            assertEquals(0, library.getBookNum());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralLibrary() {
        LibraryJsonReader reader = new LibraryJsonReader("./data/testReaderGeneralLibrary.json");
        try {
            Library library = reader.read();
            List<Book> bookList = library.getBookList();
            assertEquals(2, bookList.size());
            checkBook("testBook A", "Fiction", "testAuthor A", false, true, null, bookList.get(0));
            checkBook("testBook B", "Non-Fiction", "testAuthor B", true, false, 4, bookList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
