package entity;

import java.time.LocalDate;

public class Book {

    private int bookID;
    private int usersID;
    private String bookAuthor;
    private String bookName;
    private LocalDate bookYear;
    private LocalDate bookMustBeReturned;
    private boolean bookStatus;
    private boolean isBook;
    private User user;

    public Book(int bookID, int usersID, String bookAuthor, String bookName, LocalDate bookYear, LocalDate bookMustBeReturned, boolean bookStatus) {
        this.bookID = bookID;
        this.usersID = usersID;
        this.bookAuthor = bookAuthor;
        this.bookName = bookName;
        this.bookYear = bookYear;
        this.bookMustBeReturned = bookMustBeReturned;
        this.bookStatus = bookStatus;
        if(bookMustBeReturned!=null){
        this.isBook = LocalDate.now().isBefore(bookMustBeReturned);
        }else {
            this.isBook=true;
        }
    }

    public int getBookID() {
        return bookID;
    }

    public int getUsersID() {
        return usersID;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public LocalDate getBookYear() {
        return bookYear;
    }

    public LocalDate getBookMustBeReturned() {
        return bookMustBeReturned;
    }

    public boolean isBookStatus() {
        return bookStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsBook() {
        return isBook;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", usersID=" + usersID +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookYear=" + bookYear +
                ", bookMustBeReturned=" + bookMustBeReturned +
                ", bookStatus=" + bookStatus +
                ", user=" + user +
                '}';
    }
}
