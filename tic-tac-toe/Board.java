public class Board {
    int oBoard;
    int xBoard;
    static final int[] WIN = {448,56,7,292,146,73,273,273};
    static final Board NULL = new Board(0,0);

    public Board(int oBoard, int xBoard) {
	this.oBoard = oBoard;
	this.xBoard = xBoard;
    }

    public Board(Board b) {
	oBoard = b.oBoard;
	xBoard = b.xBoard;
    }

    public Board() {
	this(0,0);
    }

    public void set(Board b) {
	this.oBoard = b.oBoard;
	this.xBoard = b.xBoard;
    }

    public int isTerminal(int color) {
	int v = 0;
	for (int win: WIN) {
	    if ((oBoard & win) == win) return color;
	    if ((xBoard & win) == win) return color * -1;
	}
	return 0;
    }

    public String toString() {
	String out = "";
	for (int i=0; i<9; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O";
	    else if (((1 << i) & xBoard) != 0) out+="X";
	    else out+="_";
	    if (i%3==2) out +="\n";
	}
	return out;
    }

    public String toStringVisible() {
	String out = "";
	for (int i=0; i<9; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O";
	    else if (((1 << i) & xBoard) != 0) out+="X";
	    else out+=i;
	    if (i%3==2) out +="\n";
	}
	return out;
    }

    public Board place(int color, int i) {
	if (((1 << i) & (oBoard | xBoard)) == 0) {
	    if (color == 1)
		return new Board(oBoard | (1 << i), xBoard);
	    else return new Board(oBoard, xBoard | (1 << i));
	}
	return NULL;
    }

    public Board[] children(int color) {
	int m = ~(oBoard | xBoard) & 0b111111111;
	Board[] children = new Board[Integer.bitCount(m)];
	int i=0;
	for (int j=0; j<9; j++) {
	    if (((1 << j) & m) != 0) {
		if (color == 1) {
		    children[i++] = new Board(oBoard | (1 << j), xBoard);
		} else {
		    children[i++] = new Board(oBoard, xBoard | (1 << j));
		}
	    }
	}
	return children;
    }

    public static int value(Board b, int color, int depth) {
	int t = b.isTerminal(color);
	if (t!=0) return 10*t-depth;
	if (depth==0 || ((b.oBoard | b.xBoard) == 0b111111111)) return 0;
	int max = Integer.MIN_VALUE;
	for (Board c: b.children(color)) {
	    //pr(c + "with value " + value(c, color * -1, depth-1) + " for " + (color * -1) + " at depth " + depth);
	    int v = value(c, color * -1, depth-1);
	    max = Math.max(max, -v);
	}
	return max;
    }

    public void compMove(int color, int depth) {
	int i = 0;
	int max = Integer.MIN_VALUE;
	for (int j=0; j<9; j++) {
	    Board c = place(color, j);
	    //pr("Testing board with placement at " + j);
	    if (c!=NULL) {
		int v = -value(c, color * -1, depth);
		//pr(c + "with value " + -v + " for " + color * -1);
		if (v>max) {
		    i = j;
		    max = v;
		}
	    }// else pr("Failure due to overlap");
	}
	//pr("choosing place " + i + " with value " + max);
	set(place(color, i));
    }

    public void playerMove(int color) {
	Board p = NULL;
	System.out.print("Choose player move\n"+toStringVisible()+"Choose number: ");
	while (p==NULL) {
	    int move = 0;
	    try {
		move = Integer.parseInt(System.console().readLine());
		if (move<0 || move>=9) throw new NumberFormatException();
	    } catch (NumberFormatException nbfme) {
		System.out.print("Choose an integer present on board: ");
		continue;
	    } 
	    p = place(color, move);
	}
	set(p);
    }
     
    public boolean makeMove(boolean real, int color, int depth) {
	if (real) {
	    playerMove(color);
	    System.out.println("Player's move:\n");
	} else {
	    compMove(color, depth);
	    System.out.println("Computer's move:\n");
	}
	System.out.println(this);

	if ((oBoard | xBoard) == 0b111111111) {
	    System.out.println("Tie");
	    return false;
	}
	if (isTerminal(color) == 0) return true;
	if ((isTerminal(color) == 1) == real)
	    System.out.println("Win for player");
	else 
	    System.out.println("Win for computer");
	return false;
    }
	   
    public static void playGame(boolean real, int depth) {
	Board b = new Board();
	int color = 1;
	System.out.println(b);
	while(b.makeMove(real, color, depth)) {
	    real = !real;
	    color = -1 * color;
	}
    }

    public static void main(String[] args) {
	playGame(true, 10);	
    }  

    static void pr(Object s) {
	System.out.println(s);
    }
}
		
	