import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SuperArray extends ArrayList<String> implements Iterable<String> {
    public SuperArray() {super();}
    public Iterator<String> iterator() {
	return new SuperArrayIterator(this);
    }
}

class SuperArrayIterator implements Iterator<String> {
    private SuperArray data;
    private int i;

    public SuperArrayIterator(SuperArray S) {
	data = S;
	i = 0;
    }

    public boolean hasNext() {
	return i<data.size();
    }

    public String next() {
	try {
	    return data.get(i++);
	} catch (IndexOutOfBoundsException e) {
	    throw new NoSuchElementException();
	}
    }

    public void remove() {
	throw new UnsupportedOperationException();
    }
}
	
    