//Biz extends the Piece class and defines the movement rules for the Biz piece.
// Class Ram(Child Class) inherits Piece(Parent class).
//Harold goh
public class Biz extends Piece {
    /**
     * Constructor for the Biz class.
     * Initializes the Biz piece with its position and team.
     */
    public Biz(Position position, String team) {
        super(position, team, "src/" + team + "_Biz.png");
    }

    // This method overrides the setCurrentValidMoves method in the Piece class.
    @Override
    public void setCurrentValidMoves(Piece[][] board) {
        possibleNextMove.clear();// clear previous move
        int[][] possibleMoves = {
                { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, // Horizontal L-shapes
                { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } // Vertical L-shapes
        };

        // Validate each potential move
        for (int[] moves : possibleMoves) {
            int targetX = position.getColumn() + moves[0];
            int targetY = position.getRow() + moves[1];

            if (isWithinBoard(targetX, targetY) && isEmpty(targetY, targetX, board)) { // Check if within board and
                                                                                       // empty
                possibleNextMove.add(new int[] { targetY, targetX });
            }
        }
    }

    // This method checks if the target position is within the board limits.
    private boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < ChessBoard.COLUMNS && y >= 0 && y < ChessBoard.ROWS; // Ensure within board limits
    }

    // This method checks if the target position is empty or occupied by an
    // opponent's piece.
    private boolean isEmpty(int row, int col, Piece[][] board) {
        Piece targetPiece = board[row][col]; // Get the piece at the target position
        return targetPiece == null || !targetPiece.getTeam().equals(this.team);
    }
}