// Class Sau(Child Class) inherits Piece(Parent class) and IconPathProvider(interface)
// Sau extends the Piece class and defines the movement rules for the Sau piece.
// Harold goh
public class Sau extends Piece implements IconPathProvider {
    private String flipIconPath;
    private String initialIconPath;

    /**
     * Constructor for the Sau class.
     * Initializes the Sau piece with its position and team.
     */
    public Sau(Position position, String team, boolean isFlipped) {
        super(position, team, "src/" + team + "_Sau.png");
        this.flipIconPath = "src/" + team + "_Sau_Rotated.png";
        this.initialIconPath = "src/" + team + "_Sau.png";
        if (isFlipped)
            this.iconPath = flipIconPath;
    }

    // This method overrides the setCurrentValidMoves method in the Piece class.
    @Override
    public void setCurrentValidMoves(Piece[][] board) {
        possibleNextMove.clear(); // clear previous move
        int[][] possibleMoves = {
                { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, // Horizontal and vertical moves
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } // Diagonal moves
        };

        // Validate each potential move
        for (int[] moves : possibleMoves) {
            int targetX = position.getColumn() + moves[0];
            int targetY = position.getRow() + moves[1];

            if (isWithinBoard(targetX, targetY) && isEmpty(targetY, targetX, board)) {
                possibleNextMove.add(new int[] { targetY, targetX });

            }
        }
    }

    // This method checks if the target position is within the board limits.
    private boolean isWithinBoard(int x, int y) {
        return x >= 0 && x < ChessBoard.COLUMNS && y >= 0 && y < ChessBoard.ROWS;
    }

    // This method checks if the target position is empty or occupied by an
    // opponent's piece.
    private boolean isEmpty(int row, int col, Piece[][] board) {
        Piece targetPiece = board[row][col];
        return targetPiece == null || !targetPiece.getTeam().equals(this.team);
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

}
