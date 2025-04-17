// Class Ram(Child Class) inherits Piece(Parent class) and IconPathProvider(interface)
// Class Ram defines the movement rules of piece Ram
// Ow Ka Sheng
public class Ram extends Piece implements IconPathProvider {
    // The operator used to determine the next row's direction (+ or -).
    private String nextPosOperator;
    // Path to the icon used when the Ram is flipped.
    private String flipIconPath;
    // Path to the initial icon of the Ram.
    private String initialIconPath;

    /**
     * Constructor for the Ram class.
     * 
     * @param position The initial position of the Ram on the chessboard.
     * @param team     The team to which the Ram belongs ("Red" or another team).
     */
    public Ram(Position position, String team) {
        // Call the parent class constructor and set the initial icon path.
        super(position, team, "src/" + team + "_Ram.png");
        // Set the direction operator based on the team (Red moves "+" direction, others
        // "-").
        this.nextPosOperator = team.equals("Red") ? "+" : "-";
        // Set the paths for the flipped and initial icons.
        this.flipIconPath = "src/" + team + "_Ram_Rotated.png";
        this.initialIconPath = "src/" + team + "_Ram.png";
        int row = position.getRow();
        // Update the movement direction and icon based on the initial position's row.
        updateDirection(row);
        updateIconDirection(row);
    }

    /**
     * Updates the direction operator based on the current row.
     * If the Ram is at the top or bottom of the board, it flips its direction.
     * 
     * @param row The current row of the Ram.
     */
    private void updateDirection(int row) {
        if (row == 0 || row == ChessBoard.ROWS - 1) {
            nextPosOperator = (row == 0) ? "+" : "-";
        }
    }

    /**
     * Updates the icon path based on the current row.
     * If the Ram is at the top or bottom, it swaps the initial and flipped icons.
     * 
     * @param row The current row of the Ram.
     */
    private void updateIconDirection(int row) {
        if (row == 0 || row == ChessBoard.ROWS - 1) {
            String temp = initialIconPath;
            initialIconPath = flipIconPath;
            flipIconPath = temp;
        }
    }

    /**
     * Sets the valid moves for the Ram based on its current position and the board
     * state.
     * 
     * @param board The chessboard represented as a 2D array of Piece objects.
     */
    @Override
    public void setCurrentValidMoves(Piece[][] board) {
        // Clear the list of possible moves before calculating new ones.
        possibleNextMove.clear();

        int row = position.getRow();
        int column = position.getColumn();
        // Determine the next row based on the direction operator.
        int nextRow = nextPosOperator.equals("-") ? row - 1 : row + 1;

        // Check if the next position is within the board boundaries.
        if (isWithinBounds(nextRow, column, board.length, board[0].length))
            // Add to next move list
            possibleNextMove.add(new int[] { nextRow, column });
    }

    // Setter methods for updating specific properties.
    public void setNextPosOperator(String nextPosOperator) {
        this.nextPosOperator = nextPosOperator;
    }

    public void setFlipIconPath(String flipIconPath) {
        this.flipIconPath = flipIconPath;
    }

    public void setInitialIconPath(String initialIconPath) {
        this.initialIconPath = initialIconPath;
    }

    /**
     * Checks if a given position is within the board boundaries.
     * 
     * @param row    The row index to check.
     * @param column The column index to check.
     * @param maxRow The maximum number of rows on the board.
     * @param maxCol The maximum number of columns on the board.
     * @return True if the position is within bounds, false otherwise.
     */
    private boolean isWithinBounds(int row, int column, int maxRow, int maxCol) {
        return row >= 0 && row < maxRow && column >= 0 && column < maxCol;
    }

    /**
     * Moves the Ram to a new position and updates its direction and icon paths
     * accordingly.
     * 
     * @param nextMove The index of the next move.
     * @return The updated position of the Ram.
     */
    @Override
    public Position move(int nextMove) {
        super.move(nextMove);
        updateDirection(position.getRow());
        updateIconDirection(position.getRow());
        return position;
    }

    // Getter methods for retrieving specific properties.
    @Override
    public String getFlipIconPath() {
        return flipIconPath;
    }

    @Override
    public String getInitialIconPath() {
        return initialIconPath;
    }

    public String getNextPosOperator() {
        return nextPosOperator;
    }
}
