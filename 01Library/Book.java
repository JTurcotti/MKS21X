public class Book {
    protected String author, title, isbn;
    public Book(){
	author = "";
	title = "";
	isbn = "";
    }
    public Book(String author, String title, String isbn) {
	this.author = author;
	this.title = title;
	this.isbn = isbn;
    }
    public String toString() {
	return String.format("%s by %s, %s", title, author, isbn);
    }
}
