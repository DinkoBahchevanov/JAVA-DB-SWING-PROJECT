import entities.Author;
import entities.Book;
import entities.Library;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Engine extends JFrame implements Runnable {

    public final EntityManager entityManager;

    //----------------------------------------------------------------------------------
    //Author properties
    private final JPanel authorPanel = new JPanel();
    private final JPanel authorAddButtonPanel = new JPanel();
    private final JLabel firstNameL = new JLabel("First name: ");
    private final JLabel lastNameL = new JLabel("Last name: ");
    private final JLabel birthPlaceLabel = new JLabel("Birth place: ");
    private final JLabel ageL = new JLabel("Age: ");

    private final JTextField fNameField = new JTextField();
    private final JTextField lNameField = new JTextField();
    //JTextField sexField = new JTextField();
    private final JTextField ageField = new JTextField();

    private final String[] birthPlaces =
            {"Sofia", "Plovdiv", "Varna", "Burgas", "Stara Zagora",
                    "Vidin", "Shumen", "Iambol", "Asenovgrad"};

    private final JComboBox<String> locationCombo1 = new JComboBox<>(birthPlaces);
    private final JComboBox<String> locationCombo2 = new JComboBox<>(birthPlaces);
    private final JButton authorAddButton = new JButton("Add author");
    //----------------------------------------------------------------------------------
    // Library properties
    private final JPanel libraryPanel = new JPanel();

    private final JLabel libraryNameLabel = new JLabel("Name of library: ");
    private final JLabel libraryLocationLabel = new JLabel("Location: ");
    private final JLabel libraryRatingLabel = new JLabel("Rating Level: ");

    private final JTextField libraryNameField = new JTextField();
    private final JTextField libraryRatingField = new JTextField();

    private final JPanel libraryAddButtonPanel = new JPanel();
    private final JButton addLibraryButton = new JButton("Add library");
    //----------------------------------------------------------------------------------
    // book properties
    private final JPanel bookPanel = new JPanel();

    private final JLabel bookDescriptionLabel = new JLabel("Book genre: ");
    private final JLabel bookNameLabel = new JLabel("Book name: ");
    private final JLabel bookPagesLabel = new JLabel("Book number of pages: ");
    private final JLabel bookAuthorNameLabel = new JLabel("Book author name: ");
    private final JLabel bookLibraryNameLabel = new JLabel("Book library name: ");

    private final JTextField bookNameField = new JTextField();
    private final JTextField bookLibraryNameField = new JTextField();
    private final JTextField bookPagesField = new JTextField();
    private final JTextField bookDescriptionField = new JTextField();
    private final JTextField bookAuthorNameField = new JTextField();

    private final JPanel addBookPanel = new JPanel();
    private final JButton addBookButton = new JButton("Add Book");
    //----------------------------------------------------------------------------------
    //Tables for entities
    JTable authorTable;
    JTable libraryTable;
    JTable bookTable;
    JScrollPane authorScroll;

    //----------------------------------------------------------------------------------
    // 1 query
    JPanel firstQueryPanel;
    JTable searchAuthorByNameAndMinBookPagesTable;
    JLabel queryAuthorNameLabel;
    JTextField queryAuthorNameTextField;
    JLabel queryBookNameLabel;
    JTextField queryBookNameTextField;
    JButton queryAuthorNameBookNameButton;

    // 2 query

    JPanel secondQueryPanel;
    JTable searchByBookGenreAndAuthorFullNameTable;
    JLabel queryAuthorFullNameLabel;
    JTextField queryAuthorFullNameTextField;
    JLabel queryBookGenreLabel;
    JTextField queryBookGenreTextField;
    JButton queryAuthorNameAndBookGenreButton;

    // 3 query
    JPanel thirdQueryPanel;
    JTable searchLibraryByAuthorFullNameAndBookNameTable;
    JLabel querySecondAuthorFullNameLabel;
    JTextField querySecondAuthorFullNameTextField;
    JLabel querySecondBookNameLabel;
    JTextField querySecondBookNameTextField;
    JButton querySearchLibraryByANameAndBNameButton;

    //----------------------------------------------------------------------------------
//    private JButton deleteButton = new JButton("Delete");
//    private JButton editButton = new JButton("Edit");
    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.setSize(700, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(new GridLayout(9, 1));

        setAuthor();
        setLibrary();
        setBook();
        setTables();
        setQueries();
    }

    private void setQueries() {
        setFirstQuery();
        setSecondQuery();
        setThirdQuery();
    }

    private void setThirdQuery() {
        thirdQueryPanel = new JPanel();
        thirdQueryPanel.setLayout(new GridLayout(3, 2));
        thirdQueryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Search library by Author Name and Book Name",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        querySecondAuthorFullNameLabel = new JLabel("Author Full Name: ");
        querySecondAuthorFullNameTextField = new JTextField();

        querySecondBookNameLabel = new JLabel("Book Name: ");
        querySecondBookNameTextField = new JTextField();

        thirdQueryPanel.add(querySecondAuthorFullNameLabel);
        thirdQueryPanel.add(querySecondAuthorFullNameTextField);
        thirdQueryPanel.add(querySecondBookNameLabel);
        thirdQueryPanel.add(querySecondBookNameTextField);
        querySearchLibraryByANameAndBNameButton = new JButton("Search!");
        thirdQueryPanel.add(new JPanel());
        thirdQueryPanel.add(querySearchLibraryByANameAndBNameButton);
        this.add(thirdQueryPanel);

        //Add table for 3 query
        JPanel panelToAddTableThirdQuery = new JPanel();
        searchLibraryByAuthorFullNameAndBookNameTable = new JTable(0, 5);
        searchLibraryByAuthorFullNameAndBookNameTable.setPreferredSize(new Dimension(300, 200));
        JScrollPane js = new JScrollPane(searchLibraryByAuthorFullNameAndBookNameTable);
        js.setPreferredSize(new Dimension(500, 60));
//        scrollPane3 = new JScrollPane(searchLibraryByAuthorFullNameAndBookNameTable);
//        this.add(scrollPane3);
        panelToAddTableThirdQuery.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Result of Search",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        panelToAddTableThirdQuery.add(js);
        this.add(panelToAddTableThirdQuery);
        querySearchLibraryByANameAndBNameButton.addActionListener(new QuerySearchByAuthorFullNameAndBookName());
        query3SetColumnNames();
    }

    private void setSecondQuery() {
        secondQueryPanel = new JPanel();
        secondQueryPanel.setLayout(new GridLayout(3, 2));
        secondQueryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Search Book by Genre and author Full Name",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        queryAuthorFullNameLabel = new JLabel("Author Full Name: ");
        queryAuthorFullNameTextField = new JTextField();
        queryBookGenreLabel = new JLabel("Genre of book");
        queryBookGenreTextField = new JTextField();
        secondQueryPanel.add(queryAuthorFullNameLabel);
        secondQueryPanel.add(queryAuthorFullNameTextField);
        secondQueryPanel.add(queryBookGenreLabel);
        secondQueryPanel.add(queryBookGenreTextField);
        queryAuthorNameAndBookGenreButton = new JButton("Search!");
        secondQueryPanel.add(new JPanel());
        secondQueryPanel.add(queryAuthorNameAndBookGenreButton);
        this.add(secondQueryPanel);

        //TAble for 2 query
        JPanel panelToAddTableSecondQuery = new JPanel();
        searchByBookGenreAndAuthorFullNameTable = new JTable(0, 4);
        searchByBookGenreAndAuthorFullNameTable.setPreferredSize(new Dimension(300, 200));

        JScrollPane js = new JScrollPane(searchByBookGenreAndAuthorFullNameTable);
        js.setPreferredSize(new Dimension(500, 60));

        panelToAddTableSecondQuery.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Result of Search",
                TitledBorder.CENTER,
                TitledBorder.TOP));
//        panelToAddTableSecondQuery.add(searchByBookGenreAndAuthorFullNameTable);
        panelToAddTableSecondQuery.add(js);
        this.add(panelToAddTableSecondQuery);
        queryAuthorNameAndBookGenreButton.addActionListener(new QuerySearchByAuthorFullNameAndBookGenre());
        query2SetColumnNames();
    }

    private void setFirstQuery() {
        firstQueryPanel = new JPanel();
        firstQueryPanel.setLayout(new GridLayout(3, 2));
        firstQueryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Search Author by Full Name and minimum Book pages",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        queryAuthorNameLabel = new JLabel("Author Full name: ");
        queryAuthorNameTextField = new JTextField();
        queryBookNameLabel = new JLabel("Book minimum pages: ");
        queryBookNameTextField = new JTextField();

        firstQueryPanel.add(queryAuthorNameLabel);
        firstQueryPanel.add(queryAuthorNameTextField);
        firstQueryPanel.add(queryBookNameLabel);
        firstQueryPanel.add(queryBookNameTextField);

        queryAuthorNameBookNameButton = new JButton("Search!");

        firstQueryPanel.add(new JPanel());
        firstQueryPanel.add(queryAuthorNameBookNameButton);
        this.add(firstQueryPanel);

        //Table of first query
        JPanel panelToAddTableFirstQuery = new JPanel();
        searchAuthorByNameAndMinBookPagesTable = new JTable(0, 5);
        searchAuthorByNameAndMinBookPagesTable.setPreferredSize(new Dimension(300, 200));
        JScrollPane js = new JScrollPane(searchAuthorByNameAndMinBookPagesTable);
        js.setPreferredSize(new Dimension(500, 60));

        panelToAddTableFirstQuery.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Result of Search",
                TitledBorder.CENTER,
                TitledBorder.TOP));

        panelToAddTableFirstQuery.add(js);
        this.add(panelToAddTableFirstQuery);
        queryAuthorNameBookNameButton.addActionListener(new QuerySearchByAuthorNameAndBookPages());
        query1SetColumnNames();
    }

    private void setTables() {
//        JPanel tablesPanel = new JPanel(new GridLayout(3, 1));
        authorTable = new JTable(0, 5);
        JScrollPane authorScrollPane = new JScrollPane(authorTable);
        authorScrollPane.setVisible(true);
        authorScrollPane.setPreferredSize(new Dimension(500,60));
//        authorTable.setFillsViewportHeight(true);
        JPanel authorPanel = new JPanel();
        authorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Authors",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        authorPanel.add(authorScrollPane);
        authorTable.getColumnModel().getColumn(0).setHeaderValue("Author ID");
        authorTable.getColumnModel().getColumn(1).setHeaderValue("First Name");
        authorTable.getColumnModel().getColumn(2).setHeaderValue("Last Name");
        authorTable.getColumnModel().getColumn(3).setHeaderValue("Birth Place");
        authorTable.getColumnModel().getColumn(4).setHeaderValue("Age");

//        JTableHeader authorHeader= authorTable.getTableHeader();
//        TableColumnModel colMod = authorHeader.getColumnModel();
//        TableColumn tabCol = colMod.getColumn(0);
//        tabCol.setHeaderValue("name");
//        authorHeader.repaint();
//        JScrollPane myScroll=new JScrollPane(authorTable);
//        myScroll.setPreferredSize(new Dimension(650, 150));
//        authorPanel.add(myScroll);
        this.add(authorPanel);
        authorTable.setGridColor(Color.red);


        //library table
        JPanel libraryPanel = new JPanel();
        libraryTable = new JTable(0, 4);
        JScrollPane libraryScrollPane = new JScrollPane(libraryTable);
        libraryScrollPane.setVisible(true);
        libraryScrollPane.setPreferredSize(new Dimension(500, 60));
        libraryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Library Table",
                TitledBorder.CENTER,
                TitledBorder.TOP));

        libraryTable.getColumnModel().getColumn(0).setHeaderValue("Library ID");
        libraryTable.getColumnModel().getColumn(1).setHeaderValue("Library Name");
        libraryTable.getColumnModel().getColumn(2).setHeaderValue("Library Location");
        libraryTable.getColumnModel().getColumn(3).setHeaderValue("Library Rating");

        libraryPanel.add(libraryScrollPane);
        this.add(libraryPanel);
        libraryTable.setGridColor(new Color(213, 0, 255));

        //book table
        JPanel bookPanel = new JPanel();
        bookTable = new JTable(0, 5);

        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        bookScrollPane.setVisible(true);
        bookScrollPane.setPreferredSize(new Dimension(500, 60));

        bookPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "Book Table",
                TitledBorder.CENTER,
                TitledBorder.TOP));
        bookPanel.add(bookScrollPane);

        bookTable.getColumnModel().getColumn(0).setHeaderValue("Book ID");
        bookTable.getColumnModel().getColumn(1).setHeaderValue("Book Name");
        bookTable.getColumnModel().getColumn(2).setHeaderValue("Book Genre");
        bookTable.getColumnModel().getColumn(3).setHeaderValue("Book Pages");
        bookTable.getColumnModel().getColumn(4).setHeaderValue("Book Library");

        bookTable.setGridColor(new Color(0, 72, 255));
        this.add(bookPanel);
        this.add(new JPanel());
    }

    private void setBook() {
        bookPanel.setLayout(new GridLayout(7, 2));
        bookPanel.add(bookNameLabel);
        bookPanel.add(bookNameField);

        bookPanel.add(bookPagesLabel);
        bookPanel.add(bookPagesField);

        bookPanel.add(bookDescriptionLabel);
        bookPanel.add(bookDescriptionField);

        bookPanel.add(bookAuthorNameLabel);
        bookPanel.add(bookAuthorNameField);

        bookPanel.add(bookLibraryNameLabel);
        bookPanel.add(bookLibraryNameField);

        addBookPanel.add(addBookButton);
        addBookButton.addActionListener(new AddBookAction());
        this.add(bookPanel);
        this.add(addBookPanel);
    }

    private void setLibrary() {
        libraryPanel.setLayout(new GridLayout(5, 2));

        libraryPanel.add(libraryNameLabel);
        libraryPanel.add(libraryNameField);

        libraryPanel.add(libraryLocationLabel);
        libraryPanel.add(locationCombo2);

        libraryPanel.add(libraryRatingLabel);
        libraryPanel.add(libraryRatingField);

        libraryAddButtonPanel.add(addLibraryButton);
        addLibraryButton.addActionListener((new AddLibraryAction()));

        this.add(libraryPanel);
        this.add(libraryAddButtonPanel);
    }

    private void setAuthor() {
        authorPanel.setLayout(new GridLayout(5, 2));
        authorAddButton.setLayout(new GridLayout(1, 1));
        authorPanel.add(firstNameL);
        authorPanel.add(fNameField);

        authorPanel.add(lastNameL);
        authorPanel.add(lNameField);

        authorPanel.add(birthPlaceLabel);
        authorPanel.add(locationCombo1);

        authorPanel.add(ageL);
        authorPanel.add(ageField);

        this.add(authorPanel);

        authorAddButtonPanel.add(authorAddButton);
//        midPanel.add(deleteButton);
//        midPanel.add(editButton);

        authorAddButton.addActionListener(new AddAuthorAction());
        this.add(authorAddButtonPanel);

    }

    @Override
    //jpql
    public void run() {
//        entityManager.getTransaction().begin();
//
//        Author author = new Author("", "", "Plovdiv");
////        entityManager.persist(author);
//        entityManager.getTransaction().commit();
    }

    class QuerySearchByAuthorFullNameAndBookName implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String authorFullName = querySecondAuthorFullNameTextField.getText().toLowerCase();
            String bookName = querySecondBookNameTextField.getText();

            List libraries = entityManager.createQuery("SELECT l FROM Library l " +
                    "join l.books b WHERE b.name = :bookName " +
                    "and (CONCAT( b.author.firstName, ' ', b.author.lastName)) = :authorFullName")
                    .setParameter("bookName", bookName)
                    .setParameter("authorFullName", authorFullName)
                    .getResultList();
            System.out.println();

            DefaultTableModel model = (DefaultTableModel) searchLibraryByAuthorFullNameAndBookNameTable.getModel();

            for (int i = 0; i < libraries.size(); i++) {
                Library library = (Library) libraries.get(i);
                Set<Book> books = library.getBooks();
                for (Book book : books) {
                    if (book.getName().equals(bookName)) {
                        model.addRow(new Object[]{
                                library.getId(), book.getId(),
                                library.getName(), book.getName(),
                                book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName()
                        });

//                        JTableHeader th = searchLibraryByAuthorFullNameAndBookNameTable.getTableHeader();
//                        TableColumnModel columnModel = th.getColumnModel();
//                        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(0).setHeaderValue("library ID");
//
                        for (int j = 0; j < 5; j++) {
                            searchLibraryByAuthorFullNameAndBookNameTable.getTableHeader().getColumnModel().getColumn(j).setPreferredWidth(130);
                            searchLibraryByAuthorFullNameAndBookNameTable.getTableHeader().getColumnModel().getColumn(j).setMaxWidth(130);
                        }
                        searchLibraryByAuthorFullNameAndBookNameTable.getTableHeader().repaint();
                        return;
                    }
                }
            }
        }
    }

    private void query3SetColumnNames() {
        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(0).setHeaderValue("Library ID");
        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(1).setHeaderValue("Book ID");
        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(2).setHeaderValue("Library Name");
        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(3).setHeaderValue("Book Name");
        searchLibraryByAuthorFullNameAndBookNameTable.getColumnModel().getColumn(4).setHeaderValue("Full Author Name");
    }

    class QuerySearchByAuthorFullNameAndBookGenre implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String authorFullName = queryAuthorFullNameTextField.getText().toLowerCase();
            String bookGenre = queryBookGenreTextField.getText();

            List books = entityManager.createQuery("SELECT b from Book b " +
                    "where lower(concat(b.author.firstName, ' ',b.author.lastName)) = :authorFullName " +
                    "and b.genre = :bookGenre")
                    .setParameter("authorFullName", authorFullName)
                    .setParameter("bookGenre", bookGenre)
                    .getResultList();

            DefaultTableModel model = (DefaultTableModel) searchByBookGenreAndAuthorFullNameTable.getModel();
            for (int i = 0; i < books.size(); i++) {
                Book book = (Book) books.get(i);
                Author author = book.getAuthor();

                model.addRow(new Object[]{
                        book.getId(), book.getName(), book.getGenre(),
                        book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName()
                });
            }

        }
    }

    private void query2SetColumnNames() {
        searchByBookGenreAndAuthorFullNameTable.getColumnModel().getColumn(0).setHeaderValue("Book ID");
        searchByBookGenreAndAuthorFullNameTable.getColumnModel().getColumn(1).setHeaderValue("Book name");
        searchByBookGenreAndAuthorFullNameTable.getColumnModel().getColumn(2).setHeaderValue("Book genre");
        searchByBookGenreAndAuthorFullNameTable.getColumnModel().getColumn(3).setHeaderValue("Author FullName");
    }

    class QuerySearchByAuthorNameAndBookPages implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String authorFullName = queryAuthorNameTextField.getText();
            int bookPages = Integer.parseInt(queryBookNameTextField.getText());

            List authors = entityManager.createQuery("SELECT a FROM Author a " +
                    "JOIN a.books b where b.pages >= :bookPages and CONCAT(a.firstName, ' ', a.lastName) = :authorFullName")
                    .setParameter("bookPages", bookPages)
                    .setParameter("authorFullName", authorFullName)
                    .getResultList();

            DefaultTableModel model = (DefaultTableModel) searchAuthorByNameAndMinBookPagesTable.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
            }

            for (int i = 0; i < authors.size(); i++) {
                Author author = (Author) authors.get(i);
                Set<Book> books = author.getBooks();

                for (Book book : books) {
                    if (book.getPages() >= bookPages) {
                        model.addRow(new Object[]{
                                author.getId(), author.getFirstName(), author.getLastName(),
                                author.getAge(), author.getBirthTown()});
                        return;
                    }
                }
            }
        }
    }

    private void query1SetColumnNames() {
        searchAuthorByNameAndMinBookPagesTable.getColumnModel().getColumn(0).setHeaderValue("Author ID");
        searchAuthorByNameAndMinBookPagesTable.getColumnModel().getColumn(1).setHeaderValue("Author firstName");
        searchAuthorByNameAndMinBookPagesTable.getColumnModel().getColumn(2).setHeaderValue("Author lastName");
        searchAuthorByNameAndMinBookPagesTable.getColumnModel().getColumn(3).setHeaderValue("Author age");
        searchAuthorByNameAndMinBookPagesTable.getColumnModel().getColumn(4).setHeaderValue("Author birth town");
    }


    class AddBookAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //       entityManager.getTransaction().begin();

            String name = bookNameField.getText();
            int pages = Integer.parseInt(bookPagesField.getText());
            String description = bookDescriptionField.getText();
            String libraryName = bookLibraryNameField.getText();
            String authorName = bookAuthorNameField.getText();

            ArrayList<Library> library = (ArrayList<Library>) entityManager.createQuery("SELECT l FROM Library l " +
                    "where l.name = :name").setParameter("name", libraryName).getResultList();
            ArrayList<Author> author = (ArrayList<Author>) entityManager.createQuery("SELECT a FROM Author a " +
                    "where CONCAT(a.firstName, ' ', a.lastName) = :name").setParameter("name", authorName).getResultList();


            List foundBooks =entityManager.createQuery("SELECT b FROM Book b JOIN b.libraries l where l.name = :libraryName and b.name = :name ")
                    .setParameter("libraryName", libraryName)
                    .setParameter("name", name).getResultList();
            if (foundBooks.size() > 0) {
                entityManager.getTransaction().commit();
                return;
            }

            if (library.size() > 0 && author.size() > 0) {
                entityManager.getTransaction().begin();
                Book book = new Book(name, pages, description);
                book.setAuthor(author.get(0));
                Library currentLibrary = library.get(0);
                book.addLibrary(currentLibrary);
                DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
                entityManager.persist(book);
                entityManager.getTransaction().commit();

                model.addRow(new Object[]{book.getId(), book.getName(), book.getGenre(), book.getPages()
                        ,library.get(0).getName()});
            }
        }
    }

    class AddLibraryAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            entityManager.getTransaction().begin();
            Library library = new Library(libraryNameField.getText(), (String) locationCombo2.getSelectedItem()
                    , Integer.parseInt(libraryRatingField.getText()));
            List<Library> libraryList = (ArrayList<Library>) entityManager.createQuery("SELECT l FROM Library l " +
                    "where l.name = :name").setParameter("name", library.getName()).getResultList();

            if (libraryList.size() > 0) {
                entityManager.getTransaction().commit();
                return;
            }
            DefaultTableModel model = (DefaultTableModel) libraryTable.getModel();
            entityManager.persist(library);
            entityManager.getTransaction().commit();
            model.addRow(new Object[]{library.getId(), library.getName(), library.getLocation(), library.getRating()});
        }
    }


    class AddAuthorAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            entityManager.getTransaction().begin();

            Author author = new Author(Integer.parseInt(ageField.getText()), fNameField.getText(), lNameField.getText(),
                    locationCombo1.getSelectedItem().toString());

            ArrayList<Author> authors = (ArrayList<Author>) entityManager.createQuery("SELECT a FROM Author a " +
                    "where (a.firstName = :fname AND a.lastName = :lname)", Author.class)
                    .setParameter("fname", author.getFirstName())
                    .setParameter("lname", author.getLastName()).getResultList();

            DefaultTableModel model = (DefaultTableModel) authorTable.getModel();

            if (authors.size() > 0) {
                entityManager.getTransaction().commit();
                return;
            }

            entityManager.persist(author);
            entityManager.getTransaction().commit();
            model.addRow(new Object[]{author.getId(), author.getFirstName(), author.getLastName(), author.getBirthTown(), author.getAge()});
        }
    }
}
