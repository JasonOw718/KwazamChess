import java.util.ArrayList;

// Class Tor(Child Class) inherits Piece(Parent class)
// Class Ram defines the movement rules of piece Ram
// Ow Ka Sheng
public class Tor extends Piece {
    /**
     * Constructor for the Tor piece.
     *
     * @param position The initial position of the Tor.
     * @param team     The team to which the Tor belongs ("Red" or "Blue").
     */
    public Tor(Position position, String team) {
        super(position, team, "src/" + team + "_Tor.png");
    }

    /**
     * Copy constructor for creating a new Tor object from an existing
     * piece.(swapping with xor)
     *
     * @param xor The piece to copy (must have the same team and position).
     */
    public Tor(Piece xor) {
        super(xor.position, xor.team, "src/" + xor.team + "_Tor.png");
        this.possibleNextMove = new ArrayList<>();
        for (int[] pos : xor.possibleNextMove) {
            this.possibleNextMove.add(pos); // Copy the list of possible moves.
        }
    }

    /**
     * Calculates and sets all the valid moves for the Tor based on the current
     * board state.
     *
     * @param board The 2D array representing the game board with all pieces.
     */
    public void setCurrentValidMoves(Piece[][] board) {
        // Clear the current list of possible moves before recalculating.
        possibleNextMove.clear();

        // Direction arrays for row and column movements:
        // Down, Right, Up, Left (in that order).
        final int[] DIRECTIONS_ROW = { 1, 0, -1, 0 };
        final int[] DIRECTIONS_COL = { 0, 1, 0, -1 };

        // Get the current row and column of the Tor piece.
        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        // Iterate through all four movement directions.
        for (int dir = 0; dir < 4; dir++) {
            // Initialize the new row and column variables for movement in a direction.
            int newRow = currentRow;
            int newCol = currentCol;

            // Continue moving in the current direction until blocked.
            while (true) {
                // Update the row and column according to the current direction.
                newRow += DIRECTIONS_ROW[dir];
                newCol += DIRECTIONS_COL[dir];

                // Check if the new position is out of bounds.
                if (newRow < 0 || newRow >= ChessBoard.ROWS ||
                        newCol < 0 || newCol >= ChessBoard.COLUMNS) {
                    break; // Stop if the position is outside the board.
                }

                // Get the piece at the new position.
                Piece nextCellPiece = board[newRow][newCol];
                if (nextCellPiece != null) {
                    // If the cell contains an opponent piece, it's a valid capture move.
                    if (!nextCellPiece.team.equals(team)) {
                        possibleNextMove.add(new int[] { newRow, newCol });
                    }
                    break; // Stop further movement in this direction after an obstacle.
                }

                // If the cell is empty, add the move as valid.
                possibleNextMove.add(new int[] { newRow, newCol });
            }
        }
    }
}
