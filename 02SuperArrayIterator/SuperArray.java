import java.util.ArrayList;

public class SuperArray extends ArrayList implements Iterable<String> {
    public SuperArray() {super();}
    public Iterator<String> iterator() {
	return new SuperArrayIterator(this);
    }
}

class SuperArrayIterator implements Iterator<String> {
    private SuperArray data;
    private int i;

    public SupperArrayIterator(SuperArray S) {
	data = S;
	i = 0;
    }

    public boolean hasNext() {
	return i<data.size();
    }

    public String next() {
	try {
	    return data.get(i);
	} catch (IndexOutOfBoundsException e) {
	    throw new NoSuchElementException();
	}
    }

    public void remove() {
	throw new UnsupportedOperationException();
    }
}
	
    