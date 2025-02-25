package persistence;

import model.Book;
import model.Library;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Code modeled from JsonSerializationDemo
// Represents a reader that reads Library from JSON data stored in file
public class LibraryJsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public LibraryJsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads library from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Library read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLibrary(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses library from JSON object and returns it
    private Library parseLibrary(JSONObject jsonObject) {
        Library library = new Library();
        addBooks(library, jsonObject);
        addWishlistBooks(library, jsonObject);
        addReadBooks(library, jsonObject);
        return library;
    }

    // MODIFIES: library
    // EFFECTS: parses books from JSON object and adds them to library
    private void addBooks(Library library, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("bookList");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addBook(library, nextBook);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses book from JSON object and adds it to library
    private void addBook(Library library, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        String genre = jsonObject.getString("genre");
        String author = jsonObject.getString("author");
        Boolean isRead = jsonObject.getBoolean("isRead");
        Boolean isOnWishlist = jsonObject.getBoolean("isOnWishlist");
        Integer rating = null;
        if (!jsonObject.isNull("rating")) {
            rating = jsonObject.getInt("rating");
        }

        Book book = new Book(title, genre, author);
        if (isRead) {
            book.markAsRead();
        }
        book.markWishlist(isOnWishlist);
        if (rating != null) {
            book.setRating(rating);
        }
        library.addBook(book); // Ensure the book is added to the library's bookList
    }

    // MODIFIES: library
    // EFFECTS: parses wishlist books from JSON object and adds them to library
    private void addWishlistBooks(Library library, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wishlistBooks");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addWishlistBook(library, nextBook);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses book from JSON object and adds it to the wishlist in library
    private void addWishlistBook(Library library, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        library.markWishlist(library.findBookIndex(title), true);
    }

    // MODIFIES: library
    // EFFECTS: parses read books from JSON object and adds them to library
    private void addReadBooks(Library library, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("readBooks");
        for (Object json : jsonArray) {
            JSONObject nextBook = (JSONObject) json;
            addReadBook(library, nextBook);
        }
    }

    // MODIFIES: library
    // EFFECTS: parses book from JSON object and adds it to the read list in library
    private void addReadBook(Library library, JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        library.markRead(library.findBookIndex(title));
    }
}
