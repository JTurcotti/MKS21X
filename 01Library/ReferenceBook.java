public class ReferenceBook extends LibraryBook {
    public String collection;
    public ReferenceBook(String author, String title, String isbn, String callNumber, String collection) {
	super(author, title, isbn);
	this.callNumber = callNumber;
	this.collection = collection;
    }
    void checkout(String s, String ss) {
	System.out.println("cannot check out a reference book");
    }
    void returned() {
	System.out.println("reference book could not have been checked out -- return impossible");
    }
    public String circulationStatus() {
	return "cannot check out a reference book";
    }
    public String toString() {
	return super.toString() + " ; " + callNumber + " ; " + collection;
    }
}    