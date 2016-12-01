public class CirculatingBook extends LibraryBook {
    String currentHolder;
    String dueDate;
    public CirculatingBook(String author, String title, String isbn, String callNumber) {
	super(author, title, isbn);
	this.callNumber = callNumber;
	currentHolder = null;
	dueDate = null;
    }
    public void checkout(String name, String date) {
	currentHolder = name;
	dueDate = date;
    }
    public void returned() {
	currentHolder = null;
	dueDate = null;
    }
    public String circulationStatus() {
	if (currentHolder == null) {
	    return "book available on shelves";
	} else {
	    return currentHolder + " holds; due on " + dueDate;
	}
    }
    public String toString() {
	return super.toString() + " ; " + circulationStatus();
    }
}
	