package model;

import org.json.JSONObject;

import persistence.Writable;

// A class representing a book with a title, genre, author, and status on shelf
public class Book implements Writable {
    private String title;
    private String genre;
    private String author;
    private Boolean isRead;
    private Boolean isOnWishlist;
    private Integer rating;

    // EFFECTS: creates a book with a title, genre, and author that has not been read
    //          or added to wishlist
    public Book(String bookTitle, String bookGenre, String bookAuthor) {
        title = bookTitle;
        genre = bookGenre;
        author = bookAuthor;
        isRead = false;
        isOnWishlist = false;
        rating = null;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    // MODIFIES: this
    // EFFECTS: marks a book as read and takes it off the wishlist
    public void markAsRead() {
        isRead = true;
        isOnWishlist = false;

    }

    // REQUIRES: book is not already marked as read
    // MODIFIES: this
    // EFFECTS: adds/removes the book from the wishlist
    public void markWishlist(Boolean wishlistStatus) {
        isOnWishlist = wishlistStatus;
    }

    public Boolean getReadStatus() {
        return isRead;
    }

    public Boolean getWishlistStatus() {
        return isOnWishlist;
    }

    // REQUIRES: rating is between [0,5]
    // MODIFIES: this
    // EFFECTS: returns true and changes the rating of the book if it is read, returns false if
    //          the book has not been read.
    public Boolean setRating(int rating) {
        if (isRead) {
            this.rating = rating;
            return true;
        }
        return false;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("genre", genre);
        json.put("author", author);
        json.put("isRead", isRead);
        json.put("isOnWishlist", isOnWishlist);
        if (rating != null) {
            json.put("rating", rating);
        }
        return json;
    }
}
