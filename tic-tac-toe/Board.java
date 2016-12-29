import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


/**
 * <h1> Interactive game of tic tac toe <h1>
 * Upon execution of the main method a call to playGame begins an interactive game of tic tac toe
 * This class also contains several methods useful for creating and manipulating tic tac toe boards, and evaluating moves.
 */

public class Board {
    /** A bitboard containing the positions of X tokens */
    long oBoard;

    /** A bitboard containing the positins of ) tokens */
    long xBoard;

    /** ints representing the height, width, and win condition length of the board */    
    public final int m, n, k;

    /** An array of bit masks representing k length runs */
    Set<Long> WIN_RUNS = new HashSet<Long>();

    /** A symbolic instance of Board used to reference the result of illegal moves*/
    private static final Board NULL = new Board();


    /** Construcsts a new mxn board with an n-in-a-row win requirement and generates with win runs
     * @param m the height of the board
     * @param n the width of the board
     * @param k the number of consecutive tokens needed to win
     */
    public Board(int m, int n, int k) {
	this.m = m;
	this.n = n;
	this.k = k;
	genRuns();
    }

    /** Constucts a board with the same dimensions as another, but different O and X bit boards */
    public Board(Board b, long oBoard, long xBoard) {
	this.m = b.m;
	this.n = b.n;
	this.k = b.k;
	this.WIN_RUNS = b.WIN_RUNS;
	this.oBoard = oBoard;
	this.xBoard = xBoard;
    }

    /** Constructs an empty board */
    public Board() {
	this(0,0,0);
    }

    private void genRuns() {
	if (m<=0 || n<=0 || k<=0) return;

	//This is slightly ineffecient, as the same vertical and horizontal runs will be added multiple times
	//but it doesn't make that much of a difference in performce because it runs only one time
	List<Long> bases = new ArrayList<Long>();
	long[] horizontal = new long[k];
	long[] vertical = new long[k];
	for (int i=0; i<k; i++) {
	    for (int j=0; j<k; j++) {
		horizontal[i]+= (1<<j)*(1<<(i*n));
		vertical[j]+= (1<<j)*(1<<(i*n));
	    }
	}
	for (long x: horizontal) bases.add(x);
	for (long x: vertical) bases.add(x);
	
	long forward = 0;
	long backward = 0;
	for (int i=0; i<k; i++) {
	    forward+= 1<<((n+1)*i);
	    backward+= 1<<((n-1)*i+(k-1));
	}
	bases.add(forward);
	bases.add(backward);

	for (int i=0;  i<=n-k; i++) {
	    for (long j=0; j<=m-k; j++) {
		for (long x: bases) {
		    x *= 1<<i;
		    x *= 1<<(j*n);
		    WIN_RUNS.add(x);
		}
	    }
	}
    }

    /** Checks if the m,n,k values of this and another specificed board match */
    public boolean matches(Board b) {
	return m==b.m && n==b.n && k==b.k;
    }

    /**
     * Sets the contents of the O and X bitboards to those of specified board
     * @param b the board to copy
     */
    public void set(Board b) {
	this.oBoard = b.oBoard;
	this.xBoard = b.xBoard;
    }

    /**
     * Checks the terminal status of this board for a specified color
     * @return 1 if the board is a win, -1 if the board is a loss, and 0 otherwise
     * @param color the color from whose perspective to test
     */
    public int isTerminal(int color) {
	for (long win: WIN_RUNS) {
	    if ((oBoard & win) == win) return color; //checks if masking the oBoard by the given win mask produces a corresponding win board
	    if ((xBoard & win) == win) return color * -1; //does the same for xBoard, but returns the opposite color's win
	}
	return 0;
    }

    /** 
     * Returns a simple representation of the Board with X, O, and _ for empty slots.
     * @return the string representation
     */
    public String toString() {
	String out = "";
	for (int i=0; i<m*n; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O"; //tests if there is an O at position i of the board
	    else if (((1 << i) & xBoard) != 0) out+="X"; //does the same for X
	    else out+="_";
	    if (i%n==n-1) out +="\n"; //inserts newlines after every three places
	}
	return out;
    }

    /**
     * Returns a representation of the Board with X, O, and empty slots labelled with their position.
     * @return the string representation
     */
    public String toStringVisible() {
	String out = "";
	for (int i=0; i<m*n; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O";
	    else if (((1 << i) & xBoard) != 0) out+="X";
	    else out+=i; //only difference from previous method
	    if (i%n==n-1) out +="\n";
	}
	return out;
    }

    /**
     * Returns a copy of the current Board with the specified slot of the Board to contain a token of the specified color
     * @return a modified copy of the Board if the specified slot is empty, else the NULL board
     */
    public Board place(int color, int i) {
	if (((1 << i) & (oBoard | xBoard)) == 0) { //tests if the board is empty at position i
	    if (color == 1) return new Board(this, oBoard | (1 << i), xBoard); //adds an X at position i
	    else return new Board(this, oBoard, xBoard | (1 << i)); //adds an O at position i
	}
	return NULL; //represents invalid placement
    }

    /**
     * Returns an array of Board instances representing all possible children of the current Board after placement of a token of the specified color
     * @return an array of Board instances, each of which differs by exactly one token placement from this Board
     * @param color the color of the tokens to add
     */
    public Board[] children(int color) {
	long spaces = ~(oBoard | xBoard) & ((1<<(m*n))-1); //bitboard representing all slots that are empty in this board
	Board[] children = new Board[Long.bitCount(spaces)]; //array with size = the number of empty slots in this board
	int i=0;
	for (int j=0; j<m*n; j++) {
	    if (((1 << j) & spaces) != 0) children[i++] = place(color, j); //if the board is nonempty at position j, place a token at j
	}
	return children;
    }

    /**
     * Returns the integer value of the specified Board from the perspective of the specified color, recursively evaluating to the specified depth with an implementation of the negamax algorithm.
     * @param b the Board to evaluate
     * @param color the color from whose perspective to evaluate the value
     * @param depth the depth to which the algotithm will be run
     * @return the value of the board
     */
    public static int value(Board b, int color, int depth, int lower, int upper) {
	if (b==null) return 0; //not sure if i need this...
	int t = b.isTerminal(color);
	if (t!=0) return 10*t-depth; //if the board is a win for either color, return a positive or negative number whose magnitude is inversely proportional to its depth from the root node
	if (depth==0 || ((b.oBoard | b.xBoard) == ((1<<(b.m*b.n))-1))) return 0; //bottom of recursion or full board condition, returns neutral value
	int max = Integer.MIN_VALUE;
	for (Board c: b.children(color)) {
	    //pr(c + "with value " + value(c, color * -1, depth-1) + " for " + (color * -1) + " at depth " + depth);
	    int v = -1 * value(c, color * -1, depth-1, upper * -1, lower * -1);
	    max = Math.max(max, v);
	    lower = Math.max(lower, v);
	    upper = Math.min(lower, v);
	    if (lower >= upper) break;
	}
	return max; //returns the negative of the worst-valued child board of this board for the enemy, i.e. the negamax value of this node
    }

    /** Modifies the current board to contain the placement of a token of the specified color that results in the minimum value of the resulting board for the opponent. Computed by attempting each placement and for all those non-NULL valued children finding that with the maximum negative value of the call to value with the specified depth
     * @param color the color of the token to be placed, and the player whose position to optimize
     * @param depth the depth to which the algorithm will be run
     */
    public void compMove(int color, int depth) {
	int i = 0;
	int max = Integer.MIN_VALUE;
	for (int j=0; j<m*n; j++) { //difference between this method and previous, this one keep track of actual positions of token placement so that it can be later replicated
	    Board c = place(color, j);
	    //pr("Testing board with placement at " + j);
	    if (c!=NULL) {
		int v = -1 * value(c, color * -1, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
		//pr(c + "with value " + -v + " for " + color * -1);
		if (v>max) {
		    i = j;
		    max = v;
		}
	    }// else pr("Failure due to overlap");
	}
	//pr("choosing place " + i + " with value " + max);
	set(place(color, i)); //replication of best case token placement (child with highest negamax value)
    }

    /** Modifies the current board to contain the placement of a token of the specified color at a position specified by player input. Illegal input result in a reissue of the prompt until proper input is given
     * @param color the color of the token to be placed
     */
    public void playerMove(int color) {
	Board p = NULL;
	System.out.print("Choose player move\n"+toStringVisible()+"Choose number: ");
	while (p==NULL) {
	    int move = 0;
	    try {
		move = Integer.parseInt(System.console().readLine());
		if (move<0 || move>=m*n) throw new NumberFormatException();
	    } catch (NumberFormatException nbfme) {
		System.out.print("Choose an integer present on board: ");
		continue;
	    } 
	    p = place(color, move);
	}
	set(p);
    }
    
    /**
     * Places a token of the specified color by either the player or computer, printing associated information to the terminal and testing for endgame conditions if applicable
     * @param real true if the move will be made by player input
     * @param color the color of the token to be placed
     * @param depth the depth to which the algorithm will be run
     * @return true if the game should continue to the next round
     */
    public boolean makeMove(boolean real, int color, int depth) {
	if (real) {
	    playerMove(color);
	    System.out.println("Player's move:\n");
	} else {
	    compMove(color, depth);
	    System.out.println("Computer's move:\n");
	}
	System.out.println(this);

	if ((oBoard | xBoard) == (1<<(m*n))-1) { //tests if board is full, returns false if so to end game with tie
	    System.out.println("Tie");
	    return false;
	}
	if (isTerminal(color) == 0) return true; //tests if board is non-full, but inconclusive, returns true to continue game
	if ((isTerminal(color) == 1) == real) //tests if board is a win and the active is player, or board is loss and the active is computer, i.e. win for player
	    System.out.println("Win for player");
	else //otherwise win for computer
	    System.out.println("Win for computer");
	return false;
    }
    
    /** Runs an interactive game of tic tac toe by repeatedly calling makeMove with alternting values of real and color
     * @param real true if the game will be began by player input
     * @param depth the depth to which the algorithm will be run
     */
    public static void playGame(int m, int n, int k, boolean real, int depth) {
	Board b = new Board(m, n, k);
	int color = 1;
	System.out.println(b);
	while (b.makeMove(real, color, depth)) { //continues to make moves until returns false
	    real = !real; //switches player
	    color = -1 * color; //switches color
	}
    }

    public static void main(String[] args) {
	int m = Integer.parseInt(args[0]);
	int n = Integer.parseInt(args[1]);
	int k = Integer.parseInt(args[2]);
	playGame(m, n, k, false, 5*(m+n)/k);
    }  

    static void pr(Object s) {
	System.out.println(s);
    }
}
		
	