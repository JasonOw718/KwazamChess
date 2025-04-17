import java.util.ArrayList;

/**
 * Represents the Xor piece in the chess-like game.
 * Extends the Piece class and defines its unique movement logic (diagonal
 * moves).
 */
// Lau Zi Herng
public class Xor extends Piece {
    /**
     * Constructor to initialize the Xor piece with a position, team, and image
     * path.
     *
     * @param position The initial position of the piece.
     * @param team     The team of the piece ("white" or "black").
     */
    public Xor(Position position, String team) {
        super(position, team, "src/" + team + "_Xor.png");
    }

    /**
     * Copy constructor to create a new Xor piece from an existing one.
     *
     * @param tor The existing piece to copy.
     */
    public Xor(Piece tor) {
        super(tor.position, tor.team, "src/" + tor.team + "_Xor.png");
        this.possibleNextMove = new ArrayList<>();
        // Copy the possible moves from the original piece
        for (int[] pos : tor.possibleNextMove) {
            this.possibleNextMove.add(pos);
        }
    }

    /**
     * Sets the current valid moves for the Xor piece based on the board state.
     *
     * @param board The chessboard represented as a 2D array of pieces.
     */
    public void setCurrentValidMoves(Piece[][] board) {
        // Clear the list of possible moves before calculating new ones
        possibleNextMove.clear();

        // Direction arrays for diagonal movements
        final int[] DIRECTIONS_ROW = { 1, 1, -1, -1 }; // Down-Right, Down-Left, Up-Right, Up-Left
        final int[] DIRECTIONS_COL = { 1, -1, 1, -1 }; // Corresponding column movements

        // Current row and column of the piece
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        // Iterate over all four diagonal directions
        for (int dir = 0; dir < 4; dir++) {
            int newRow = currentRow;
            int newCol = currentCol;

            // Move in the current diagonal direction until hitting a boundary or obstacle
            while (true) {
                // Update row and column for the current direction
                newRow += DIRECTIONS_ROW[dir];
                newCol += DIRECTIONS_COL[dir];

                // Check if the new position is outside the board bounds
                if (newRow < 0 || newRow >= ChessBoard.ROWS ||
                        newCol < 0 || newCol >= ChessBoard.COLUMNS) {
                    break; // Stop moving in this direction
                }

                // Get the piece at the new position
                Piece nextCellPiece = board[newRow][newCol];
                if (nextCellPiece != null) {
                    // If the piece belongs to the opposing team, it can be captured
                    if (!nextCellPiece.team.equals(team)) {
                        possibleNextMove.add(new int[] { newRow, newCol }); // Add capture move
                    }
                    break; // Stop further movement in this direction
                }

                // If the square is unoccupied, it's a valid move
                possibleNextMove.add(new int[] { newRow, newCol });
            }
        }
    }
}
