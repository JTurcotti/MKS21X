import java.util.Arrays;

public class SlidingPuzzle {
    private int side;
    private int[][] values;

    SlidingPuzzle(int[][] values) {
	side = values.length;
	this.values = values;
    }

    boolean isDone() {
	int then = 0;
	for (int i=0; i<side; i++) {
	    for (int j=0; j<side; j++) {
		if (then > values[i][j] && values[i][j] != 0) return false;
		else {
			then = Math.max(then, values[i][j]);
		    }
	    }
	}
	return true;
    }

    boolean isDone2() {
        int[] flat = Arrays.stream(values).flatMapToInt(Arrays::stream).filter(e -> e>0).toArray();
	for (int i=0; i+1<flat.length; i++) if (flat[i]>=flat[i+1]) return false;
	return true;
    }
	

    public static void main(String[] a) {
	SlidingPuzzle p = new SlidingPuzzle(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 0, 8}});
	System.out.println(p.isDone());
	System.out.println(p.isDone2());
    } 
}