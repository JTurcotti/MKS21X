/**
 * <h1> Interactive game of tic tac toe <h1>
 * Upon execution of the main method a call to playGame begins an interactive game of tic tac toe
 * This class also contains several methods useful for creating and manipulating tic tac toe boards, and evaluating moves.
 */

public class Board {
    /** A bitboard containing the positions of X tokens */
    int oBoard;

    /** A bitboard containing the positins of ) tokens */
    int xBoard;

    /** An array of bit masks representing 3-in-a-rows */
    static final int[] WIN = {448,56,7,292,146,73,273,273};

    /** A symbolic instance of Board used to reference the result of illegal moves*/
    private static final Board NULL = new Board(0,0);

    /** Constucts a board with the specified O and X bitboards */
    public Board(int oBoard, int xBoard) {
	this.oBoard = oBoard;
	this.xBoard = xBoard;
    }

    /** Constructs a board as copy of specifiec board */
    public Board(Board b) {
	oBoard = b.oBoard;
	xBoard = b.xBoard;
    }

    /** Constructs an empty board */
    public Board() {
	this(0,0);
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
	int v = 0;
	for (int win: WIN) {
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
	for (int i=0; i<9; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O"; //tests if there is an O at position i of the board
	    else if (((1 << i) & xBoard) != 0) out+="X"; //does the same for X
	    else out+="_";
	    if (i%3==2) out +="\n"; //inserts newlines after every three places
	}
	return out;
    }

    /**
     * Returns a representation of the Board with X, O, and empty slots labelled with their position.
     * @return the string representation
     */
    public String toStringVisible() {
	String out = "";
	for (int i=0; i<9; i++) {
	    if (((1 << i) & oBoard) != 0) out+="O";
	    else if (((1 << i) & xBoard) != 0) out+="X";
	    else out+=i; //only difference from previous method
	    if (i%3==2) out +="\n";
	}
	return out;
    }

    /**
     * Returns a copy of the current Board with the specified slot of the Board to contain a token of the specified color
     * @return a modified copy of the Board if the specified slot is empty, else the NULL board
     */
    public Board place(int color, int i) {
	if (((1 << i) & (oBoard | xBoard)) == 0) { //tests if the board is empty at position i
	    if (color == 1)
		return new Board(oBoard | (1 << i), xBoard); //adds an X at position i
	    else return new Board(oBoard, xBoard | (1 << i)); //adds an O at position i
	}
	return NULL; //represents invalid placement
    }

    /**
     * Returns an array of Board instances representing all possible children of the current Board after placement of a token of the specified color
     * @return an array of Board instances, each of which differs by exactly one token placement from this Board
     * @param color the color of the tokens to add
     */
    public Board[] children(int color) {
	int m = ~(oBoard | xBoard) & 0b111111111; //bitboard representing all slots that are empty in this board
	Board[] children = new Board[Integer.bitCount(m)]; //array with size = the number of empty slots in this board
	int i=0;
	for (int j=0; j<9; j++) {
	    if (((1 << j) & m) != 0) { //tests if the board is empty at position j
		if (color == 1) {
		    children[i++] = new Board(oBoard | (1 << j), xBoard); //adds a 1 at j in the oBoard
		} else {
		    children[i++] = new Board(oBoard, xBoard | (1 << j)); //adds a 1 at j in the xBoard
		}
	    }
	}
	return children;
    }

    /**
     * Returns the integer value of the specified Board from the perspective of the specified color, recursively evaluating to the specified depth with an implementation of the negamax algorithm.
     * @param b the Board to evaluate
     * @param color the color from whose perspective to evaluate the value
     * @param depth the depth to which the algorithm will be run
     * @return the value of the board
     */
    public static int value(Board b, int color, int depth) {
	int t = b.isTerminal(color);
	if (t!=0) return 10*t-depth; //if the board is a win for either color, return a positive or negative number whose magnitude is inversely proportional to its depth from the root node
	if (depth==0 || ((b.oBoard | b.xBoard) == 0b111111111)) return 0; //bottom of recursion or full board condition, returns neutral value
	int max = Integer.MIN_VALUE;
	for (Board c: b.children(color)) {
	    //pr(c + "with value " + value(c, color * -1, depth-1) + " for " + (color * -1) + " at depth " + depth);
	    int v = value(c, color * -1, depth-1);
	    max = Math.max(max, -v);
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
	for (int j=0; j<9; j++) { //difference between this method and previous, this one keep track of actual positions of token placement so that it can be later replicated
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
		if (move<0 || move>=9) throw new NumberFormatException();
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

	if ((oBoard | xBoard) == 0b111111111) { //tests if board is full, returns false if so to end game with tie
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
    public static void playGame(boolean real, int depth) {
	Board b = new Board();
	int color = 1;
	System.out.println(b);
	while (b.makeMove(real, color, depth)) { //continues to make moves until returns false
	    real = !real; //switches player
	    color = -1 * color; //switches color
	}
    }

    public static void main(String[] args) {
	playGame(true, 10); // player starts, negamax at 10 ply
    }  

    static void pr(Object s) {
	System.out.println(s);
    }
}
		
	