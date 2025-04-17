
/**
 * The ChessBoard class represents a chessboard for the Kwazam Chess game.
 * It manages the board state, pieces, and their interactions.
 * 
 * This class provides methods to initialize the board, create pieces, move pieces,
 * flip the board for visual symmetry, and update the board after a move.
 * It also includes getters and setters for the board, piece count, and selected piece.
 */

public class ChessBoard {
    // Constants representing the number of rows and columns on the board
    public static final int COLUMNS = 5;
    public static final int ROWS = 8;

    // 2D array to represent the board, storing pieces at their respective positions
    private Piece[][] board;

    // Counter for the number of pieces remaining on the board
    private int pieceCount;

    // The currently selected piece for movement or interaction
    private Piece selectedPiece;

    // Constructor that initializes the board with pieces for both teams
    // Ow Ka Sheng
    public ChessBoard() {
        this.board = new Piece[ROWS][COLUMNS]; // Initializes the 2D array for the board
        this.pieceCount = 20; // Initially 20 pieces per team
        this.selectedPiece = null; // No piece selected initially
        initialize(30, 39, 35, "Blue"); // Initialize the Blue team's pieces
        initialize(5, 0, 4, "Red"); // Initialize the Red team's pieces
    }

    // Method to initialize pieces for a team at specified positions
    // Ow Ka Sheng
    private void initialize(int RamStartingPos, int torPos, int xorPos, String team) {
        // Initializing Tor and Xor pieces at given positions
        Position torPosition = new Position(torPos);
        board[torPosition.getRow()][torPosition.getColumn()] = createPiece("Tor", torPosition, team, "");

        Position xorPosition = new Position(xorPos);
        board[xorPosition.getRow()][xorPosition.getColumn()] = createPiece("Xor", xorPosition, team, "");

        // Calculate the other starting position and use it to place Sau and Biz pieces
        int otherStartingPos = Math.min(torPos, xorPos);

        int sauPos = otherStartingPos + 2;
        Position sauPosition = new Position(sauPos);
        board[sauPosition.getRow()][sauPosition.getColumn()] = createPiece("Sau", sauPosition, team, "");

        int biz1Pos = otherStartingPos + 1;
        Position biz1Position = new Position(biz1Pos);
        board[biz1Position.getRow()][biz1Position.getColumn()] = createPiece("Biz", biz1Position, team, "");

        int biz2Pos = otherStartingPos + 3;
        Position biz2Position = new Position(biz2Pos);
        board[biz2Position.getRow()][biz2Position.getColumn()] = createPiece("Biz", biz2Position, team, "");

        // Initialize Ram pieces at consecutive positions
        for (int i = RamStartingPos; i < RamStartingPos + 5; i++) {
            Position ramPosition = new Position(i);
            board[ramPosition.getRow()][ramPosition.getColumn()] = createPiece("Ram", ramPosition, team, "");
        }
    }

    // Method to create a piece based on its type and position
    // Ow Ka Sheng
    public Piece createPiece(String pieceType, Position position, String team, String currentTurn) {
        // Use switch-case to create different types of pieces (Tor, Xor, Sau, Biz, Ram)
        return switch (pieceType) {
            case "Tor" -> new Tor(position, team);
            case "Xor" -> new Xor(position, team);
            case "Sau" -> new Sau(position, team, currentTurn.equals("Red"));
            case "Biz" -> new Biz(position, team);
            case "Ram" -> new Ram(position, team);
            default -> null; // Return null if no matching piece type
        };
    }

    // Method to move the selected piece to a new position
    // Ow Ka Sheng
    public void moveSelectedPiece(int position) {
        this.selectedPiece.move(position); // Move the selected piece to the given position
    }

    // Method to flip the board based on the current team (for visual symmetry)
    // Ow Ka Sheng
    public void flipBoard(String currentTeam) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                Piece flipPiece = board[i][j];
                if (flipPiece != null) {
                    // If the piece implements IconPathProvider, change its icon based on the team
                    if (flipPiece instanceof IconPathProvider) {
                        IconPathProvider p = (IconPathProvider) flipPiece;
                        if (currentTeam.equals("Red"))
                            flipPiece.setIconPath(p.getFlipIconPath());
                        else
                            flipPiece.setIconPath(p.getInitialIconPath());
                    }
                }
            }
        }
    }

    // Method to update the board after a move, handling piece movement and removal
    // Ow Ka Sheng
    public void updateBoard(int nextMove) {
        // Get the current position of the selected piece
        Position selecPosition = selectedPiece.getPosition();
        int prevRow = selecPosition.getRow();
        int prevCol = selecPosition.getColumn();

        // Move the selected piece to the new position
        Position newPosition = this.selectedPiece.move(nextMove);
        int newRow = newPosition.getRow();
        int newCol = newPosition.getColumn();

        // Handle the piece that is being replaced
        Piece destPiece = board[newRow][newCol];
        board[newRow][newCol] = board[prevRow][prevCol]; // Move the selected piece to the new position
        if (destPiece != null) // If a piece is replaced, decrement the piece count
            pieceCount--;
        board[prevRow][prevCol] = null; // Set the previous position to null (no piece left there)
    }

    // Getters and setters for the board, piece count, and selected piece
    // Ow Ka Sheng
    public Piece[][] getBoard() {
        return board;
    }

    public int getPieceCount() {
        return pieceCount;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }

    public void setPieceCount(int pieceCount) {
        this.pieceCount = pieceCount;
    }

    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
        if (selectedPiece != null) {
            selectedPiece.setCurrentValidMoves(board); // Update valid moves for the selected piece
        }
    }
}
