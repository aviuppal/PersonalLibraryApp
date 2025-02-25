package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

// a class representing a library with a list of books, wishlist books, and read books
public class Library implements Writable {
    private List<Book> bookList;
    private List<Book> wishlistBooks;
    private List<Book> readBooks;

    // EFFECTS: creates a library with an empty list of books
    public Library() {
        bookList = new ArrayList<>();
        wishlistBooks = new ArrayList<>();
        readBooks = new ArrayList<>();
    }

    // MODIFIES: this, bookList
    // EFFECTS: adds book with given title, genre, and author to book list
    public void addBookToLibrary(String title, String genre, String author) {
        bookList.add(new Book(title, genre, author));
        EventLog.getInstance().logEvent(new Event("Book added to library."));
    }

    // MODIFIES: this, bookLis
    // EFFECTS: adds given book object to the library
    public void addBook(Book book) {
        bookList.add(book);
    }

    // EFFECTS: returns true if the book list is empty
    public Boolean emptyLibrary() {
        return bookList.isEmpty();
    }

    // MODIFIES: this, bookList
    // EFFECTS: reomves given book from library and returns true if successfully deleted
    public Boolean deleteBookFromLibrary(String deleteBook) {
        boolean bookDeleted = false;   
        for (Book book : bookList) {
            if (book.getTitle().equals(deleteBook)) {
                bookList.remove(book);
                EventLog.getInstance().logEvent(new Event("Book deleted from library."));
                bookDeleted = true;
                break;
            }
        }
        return bookDeleted;
    }

    public Book getBook(Integer index) {
        return bookList.get(index);
    }

    // EFFECTS: adds books from book list to wishlist if the wishlist status is true
    public void makeWishlist() {
        for (Book book : bookList) {
            if (book.getWishlistStatus()) {
                wishlistBooks.add(book);
            }
        }
    }

    // EFFECTS: adds books from book list to read list if read status is true
    public void makeReadList() {
        for (Book book : bookList) {
            if (book.getReadStatus()) {
                readBooks.add(book);
            }
        }
    }

    // EFFECTS: returns the list of wishlistBooks
    public List<Book> getWishlist() {
        return wishlistBooks;
    }

    // returns the list of read books
    public List<Book> getReadBooks() {
        return readBooks;
    }

    // returns the complete book list
    public List<Book> getBookList() {
        return bookList;
    }

    // EFFECTS: returns the number of books in the list
    public Integer getBookNum() {
        return bookList.size();
    }

    // EFFECTS: returns the title of the book at the given index in the booklist
    public String getTitle(Integer index) {
        return bookList.get(index).getTitle();
    }

    // EFFECTS: returns the genre of the book at the given index in the booklist
    public String getGenre(Integer index) {
        return bookList.get(index).getGenre();
    }

    // EFFECTS: returns the title of the book at the given index in the booklist
    public String getAuthor(Integer index) {
        return bookList.get(index).getAuthor();
    }

    // EFFECTS: returns the read status of the book at the given index in the booklist
    public Boolean getReadStatus(Integer index) {
        return bookList.get(index).getReadStatus();
    }

    // EFFECTS: returns the wishlist status of the book at the given index in the booklist
    public Boolean getWishlistStatus(Integer index) {
        return bookList.get(index).getWishlistStatus();
    }

    // EFFECTS: returns the rating of the book at the given index in the booklist
    public Integer getRating(Integer index) {
        return bookList.get(index).getRating();
    }

    // EFFECTS: returns a book's index given its title and 
    //          returns null if no book with the given title is found
    public Integer findBookIndex(String title) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getTitle().equals(title)) {
                return i;
            }
        }
        return null;
    }

    // EFFECTS: marks the wishlist status of book at the given index as given status
    public void markWishlist(Integer index, Boolean status) {
        bookList.get(index).markWishlist(status);
    }

    // EFFECTS: marks the book at given index in booklist as read
    public void markRead(Integer index) {
        bookList.get(index).markAsRead();
    }

    // EFFECTS: rates book at given index in booklist with given rating
    public void rateBook(Integer index, Integer rating) {
        bookList.get(index).setRating(rating);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bookList", booksToJson(bookList));
        json.put("wishlistBooks", booksToJson(wishlistBooks));
        json.put("readBooks", booksToJson(readBooks));
        return json;
    }

    private JSONArray booksToJson(List<Book> books) {
        JSONArray jsonArray = new JSONArray();
        for (Book book : books) {
            jsonArray.put(book.toJson());
        }
        return jsonArray;
    }
}
