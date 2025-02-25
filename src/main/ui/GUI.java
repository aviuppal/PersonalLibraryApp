package ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import java.awt.event.ActionEvent; 

import model.Book;
import model.Event;
import model.EventLog;
import model.Library;
import persistence.LibraryJsonReader;
import persistence.LibraryJsonWriter;


// the graphical user interface that displays the Library App
public class GUI extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/workroom.json";
    LibraryJsonWriter jsonWriter = new LibraryJsonWriter(JSON_STORE);
    LibraryJsonReader jsonReader = new LibraryJsonReader(JSON_STORE); 
    private JLabel titleLabel;
    private JFrame frame;
    private ImageIcon bookImage;
    private JPanel viewBooksPanel;
    JPanel buttonsPanel = new JPanel();
    private DefaultListModel<String> viewBooksList;
    private Library library;
    private JList<String> bookList;
    private JButton addBookButton;
    private JButton removeBookButton;
    private JButton saveLibraryButton;
    private JButton loadLibraryButton;
    

    // EFFECTS: creates a GUI with panels, buttons, and a title
    public GUI() {
        makeTitleLabel();
        makeBookList();
        makeViewPanel();
        makeButtonsPanel();
        makeFrame();
        displayEventLog();
    }

    // EFFECTS: creates a new frame with a title and label
    private void makeFrame() {
        frame = new JFrame();
        frame.setTitle("Personal Library App");
        frame.setSize(800, 500);
        frame.addWindowListener(new WindowAdapter() { 
            @Override
            public void windowClosing(WindowEvent e) {
                printLog();
                frame.dispose();
                System.exit(0); 
            }
        });
        frame.add(titleLabel);
        frame.add(viewBooksPanel);
        frame.add(buttonsPanel);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(0xFBEAEB));
        frame.setVisible(true);
    }

    // EFFECTS: creates the title label for the gui with an image
    private void makeTitleLabel() {
        String sep = System.getProperty("file.separator");
        bookImage = new ImageIcon(System.getProperty("user.dir") + sep + "image" + sep + "Book Image.png");
        Image scaledImage = bookImage.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        bookImage = new ImageIcon(scaledImage);
        titleLabel = new JLabel();
        titleLabel.setText("Welcome to your personal library app!");
        titleLabel.setIcon(bookImage);
        titleLabel.setBounds(0, 100, 400, 250);
    }

    // EFFECTS: creates a panel that displays all books in the library
    private void makeViewPanel() {
        viewBooksPanel = new JPanel();
        viewBooksPanel.setBackground(new Color(0xCD4662));
        viewBooksPanel.setLayout(null);
        viewBooksPanel.setBounds(400, 0, 400, 250);

        Border viewPanelBorder = BorderFactory.createLineBorder(new Color(0x500711));
        viewBooksPanel.setBorder(viewPanelBorder);
    
        // Create the title label and set its bounds
        JLabel bookViewLabel = new JLabel("Your bookshelf:");
        bookViewLabel.setBounds(150, 10, 200, 30);
        viewBooksPanel.add(bookViewLabel);
    
        // Create the JScrollPane for the book list
        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setBounds(10, 50, 370, 190);
        viewBooksPanel.add(scrollPane);
    }

    // adapted from https://cachhoc.net/2014/04/25/java-swing-tuy-bien-jlist-jlist-custom-renderer/?lang=en
    // EFFECTS: creates the booklist that users can view
    private void makeBookList() {
        library = new Library();
        viewBooksList = new DefaultListModel<String>();

        for (Book book : library.getBookList()) {
            viewBooksList.addElement("Title: " + book.getTitle() + "  Genre: " 
                    + book.getGenre() + "  Author: " + book.getAuthor());
        }

        bookList = new JList<>(viewBooksList);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // EFFECTS: creates the buttons to add/remove books and to save/load a library
    private void makeButtons() {
        addBookButton = new JButton("Add");
        addBookButton.setActionCommand("addBook");
        addBookButton.addActionListener(this);
        addBookButton.setBounds(150, 50, 100, 30);

        removeBookButton = new JButton("Remove");
        removeBookButton.setActionCommand("removeBook");
        removeBookButton.addActionListener(this);
        removeBookButton.setBounds(150, 90, 100, 30); 

        saveLibraryButton = new JButton("Save");
        saveLibraryButton.setActionCommand("saveLibrary");
        saveLibraryButton.addActionListener(this);
        saveLibraryButton.setBounds(150, 130, 100, 30);

        loadLibraryButton = new JButton("Load");
        loadLibraryButton.setActionCommand("loadLibrary");
        loadLibraryButton.addActionListener(this);
        loadLibraryButton.setBounds(150, 170, 100, 30);
    }

    // EFFECTS: creates the panel that has all the buttons
    private void makeButtonsPanel() {
        buttonsPanel.setBackground(new Color(0xF4DBDB));
        buttonsPanel.setLayout(null);
        buttonsPanel.setBounds(400, 250, 400, 250);

        Border buttonsPanelBorder = BorderFactory.createLineBorder(new Color(0x500711));
        buttonsPanel.setBorder(buttonsPanelBorder);

        JLabel buttonsLabel = new JLabel("Options:");
        buttonsLabel.setBounds(175, 10, 200, 30);
        buttonsPanel.add(buttonsLabel);

        makeButtons();

        buttonsPanel.add(addBookButton);
        buttonsPanel.add(removeBookButton);
        buttonsPanel.add(saveLibraryButton);
        buttonsPanel.add(loadLibraryButton);
    }

    // EFFECTS: handles the buttons clicks and performs the task for each
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addBook")) {
            promptAddBook();
        } else if (e.getActionCommand().equals("removeBook")) {
            promptRemoveBook();
        } else if (e.getActionCommand().equals("saveLibrary")) {
            saveLibrary();
        } else if (e.getActionCommand().equals("loadLibrary")) {
            loadLibrary();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to add a book to the library by title, genre, and author
    private void promptAddBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        String genre = JOptionPane.showInputDialog(this, "Enter genre:");
        String author = JOptionPane.showInputDialog(this, "Enter author:");

        if (title != null && author != null && genre != null) {
            library.addBookToLibrary(title, genre, author);
            updateBookList();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to remove a book with the given title
    private void promptRemoveBook() {
        String title = JOptionPane.showInputDialog(this, "Enter title of book to remove:");

        if (title != null && library.deleteBookFromLibrary(title)) {
            updateBookList(); 
        } else {
            JOptionPane.showMessageDialog(this, "Book not found in the library.");
        }
    }

    // MODIFIES: this
    // EFFECTS: saves the library to a file
    private void saveLibrary() {
        try {
            jsonWriter.open();
            jsonWriter.write(library);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
        updateBookList();
    }

    // MODIFIES: this
    // EFFECTS: loads the library from file
    private void loadLibrary() {
        try {
            library = jsonReader.read();
            System.out.println("Loaded library from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
        updateBookList();
    } 

    // MODIFIES: this
    // EFFECTS: updates the list of books displayed
    private void updateBookList() {
        viewBooksList.clear();
        for (Book book : library.getBookList()) {
            viewBooksList.addElement("Title: " + book.getTitle() + "  Genre: " 
                    + book.getGenre() + "  Author: " + book.getAuthor());
        }
        bookList.setModel(viewBooksList);
    }

    // MODIFIES: this
    // EFFECTS: adds a window listener to print the event log when the window is closed
    private void displayEventLog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog();
            }
        });
    }

    // EFFECTS: prints the event log to the console
    private void printLog() {
        System.out.println("Event Log:");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event);
        }
    }
}
