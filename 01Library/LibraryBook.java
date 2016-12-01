abstract class LibraryBook extends Book implements Comparable<LibraryBook> {
    public String callNumber;
    public int compareTo(LibraryBook other) {
	return callNumber.compareTo(other.callNumber);
    }
    abstract void checkout(String patron, String due);
    abstract void returned();
    abstract String circulationStatus();
    public LibraryBook(String author, String title, String isbn) {
	super(author, title, isbn);
    }
}