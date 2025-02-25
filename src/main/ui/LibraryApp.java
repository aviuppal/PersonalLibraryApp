package ui;


import persistence.LibraryJsonReader;
import persistence.LibraryJsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import model.Library;

// An application that allows a user to organize and rate books in their library
public class LibraryApp {
    private static final String JSON_STORE = "./data/workroom.json";
    private LibraryJsonWriter jsonWriter;
    private LibraryJsonReader jsonReader;
    private Library library;
    private Scanner scanner;
    private boolean exitProgram;

    // creates an instance of the library application ui
    public LibraryApp() {
        init();
        System.out.println("Welcome to your Personal Library app!");
        separator();

        while (!exitProgram) {
            mainMenu();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes application with starting values
    private void init() {
        library = new Library();
        scanner = new Scanner(System.in);
        jsonWriter = new LibraryJsonWriter(JSON_STORE);
        jsonReader = new LibraryJsonReader(JSON_STORE);
        exitProgram = false;
    }

    // EFFECTS: displays options and handles inputs from main menu of app
    private void mainMenu() {
        menuOptions();
        String input = this.scanner.nextLine();
        processCommands(input);
        
    }

    // EFFECTS: displays the commands the user can select
    private void menuOptions() {
        System.out.println("What would you like to do? \n");
        System.out.println("a: add new book to library");
        System.out.println("v: view all books in library app");
        System.out.println("i: add library book to wishlist");
        System.out.println("f: add library book to read list");
        System.out.println("d: delete a book from library");
        System.out.println("b: rate a read book");
        System.out.println("w: view wishlist books");
        System.out.println("r: view read books");
        System.out.println("e: exit Library application");
        System.out.println("s: save library to file");
        System.out.println("l: load library from file");
        separator();
    }

    // EFFECTS: processes the user's commands from menu
    @SuppressWarnings("methodlength")
    private void processCommands(String input) {
        separator();
        switch (input) {
            case "a":
                addBook();
                break;
            case "v":
                viewAllBooks();
                break;
            case "i":
                addToWishList();
                break;
            case "f":
                addToReadList();
                break;
            case "d":
                deleteBook();
                break;
            case "b":
                rateReadBook();
                break;
            case "w":
                viewWishlist();
                break;
            case "r":
                viewReadlist();
                break;
            case "e":
                endLibrary();
                break;
            case "s":
                saveLibrary();
                break;
            case "l":
                loadLibrary();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new book to the library with given title, genre, and author
    private void addBook() {
        System.out.println("Enter book title:");
        String title = this.scanner.nextLine();

        System.out.println("Enter book genre:");
        String genre = this.scanner.nextLine();

        System.out.println("Enter book author:");
        String author = this.scanner.nextLine();

        library.addBookToLibrary(title, genre, author);
        System.out.println("\nSuccessfully added book: " + title);

    }

    // MODIFIES: this
    // EFFECTS: removes book with given title from library
    private void deleteBook() {
        if (library.emptyLibrary()) {
            System.out.println("Error: No books in library to delete!");
            return;
        }
        System.out.println("What is the title of the book you would like to delete:");
        String deletedBook = this.scanner.nextLine();

        
        if (library.deleteBookFromLibrary(deletedBook)) {
            System.out.println("Book has been deleted from library.");
        } else {
            System.out.println("Book not found in library"); 
        }

    }

    //  EFFECTS: displays books that are on user's wishlist
    void viewWishlist() {
        Integer totalBooks = library.getBookNum();
        Integer numOfWishBooks = 0;
        for (Integer i = 0; i < totalBooks; i++) {
            if (library.getWishlistStatus(i)) {
                System.out.println(library.getTitle(i));
                numOfWishBooks++;
            }
        }
        if (numOfWishBooks == 0) {
            System.out.println("No books found in wishlist library.");
        }
    }

    // EFFECTS: displays books that the user has read
    void viewReadlist() {
        Integer totalBooks = library.getBookNum();
        Integer numOfReadBooks = 0;
        for (Integer i = 0; i < totalBooks; i++) {
            if (library.getReadStatus(i)) {
                System.out.println(library.getTitle(i));
                numOfReadBooks++;
            }
        }
        if (numOfReadBooks == 0) {
            System.out.println("No books found in read library.");
        }
    }

    // MODIFIES: this
    // EFFECTS: tells the user they have quit and exits the program
    void endLibrary() {
        System.out.println("You have quit the Library app.");
        exitProgram = true;
    }

    // EFFECTS: displays and numbers all books in the library, with their title, genre, author
    //          rating, and read/wishlist status
    void viewAllBooks() {
        Integer num = library.getBookNum();
        for (int i = 0; i < num; i++) {
            String readStatus = "Not read";
            String wishlistStatus = "Not on wishlist";
            if (library.getReadStatus(i)) {
                readStatus = "Read";
            }
            if (library.getWishlistStatus(i)) {
                wishlistStatus = "On wishlist";
            }
            System.out.println(Integer.toString(i + 1) + ": " + library.getTitle(i) + "  "   
                        + library.getGenre(i) + "  " + library.getAuthor(i) + "  " + library.getRating(i)
                        + "  " + readStatus + "  " + wishlistStatus);
        }
        separator();
    }

    // MODIFIES: book
    // EFFECTS: adds the selected book to the wishlist
    void addToWishList() {
        viewAllBooks();
        System.out.println("Enter the number of the book you want to add to wishlist");
        Integer input = Integer.valueOf(this.scanner.nextLine());
        library.markWishlist(input - 1, true);
    }

    // MODIFIES: book
    // EFFECTS: adds the selected book to the read list
    void addToReadList() {
        viewAllBooks();
        System.out.println("Enter the number of the book you have read");
        Integer input = Integer.valueOf(this.scanner.nextLine());
        library.markRead(input - 1);
    }

    // MODIFIES: book
    // EFFECTS: allows user to rate a book they have read from one to five
    void rateReadBook() {
        viewAllBooks();
        System.out.println("Enter the number of the read book you would like to rate from 1-5");
        Integer input = Integer.valueOf(this.scanner.nextLine());
        System.out.println("What do you rate this book from 1 to 5?");
        Integer rate = Integer.valueOf(this.scanner.nextLine());
        library.rateBook(input - 1, rate);
    }

    void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
            System.out.println("Saved library to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    void loadLibrary() {
        try {
            library = jsonReader.read();
            System.out.println("Loaded library from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


    // EFFECTS: prints a line to divide lines
    private void separator() {
        System.out.println("_____________________________");
    }

}

