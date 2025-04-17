import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// This class follows the MVC (Model-View-Controller) and state design pattern.
// MVC:
// - Model: Handles the game logic, state, and data (ChessModel).
// - View: Displays the UI (handled by ChessView).
// - Controller: Interacts with the model and updates the view (handled by ChessController).
// This ChessModel class represents Model class to handle the game logic, state, and data.
// State design pattern:
// - ChessModel serves as a context class for the State design pattern.
// - It maintains the current game state, which can be in one of several states (RedState or BlueState).
// - The State design pattern allows the behavior of the ChessModel to change dynamically based on the current game state
// The ChessModel interacts with the chess board and updates the game state based on player actions.

public class ChessModel {

    private ChessBoard chessBoard; // Chess board object
    private PlayerState state; // Current game state (RedState or BlueState)
    private int round; // Round number
    private ArrayList<String> moveHistory; // History of moves made during the game

    // Constructor to initialize a new game
    public ChessModel() {
        restartChessGame();
    }

    // Resets the game state to start a new game
    // Ow Ka Sheng
    public void restartChessGame() {
        this.round = 0;
        this.state = new BlueState(); // Blue starts by default
        this.chessBoard = new ChessBoard(); // Initializes a new chess board
        this.moveHistory = new ArrayList<String>(); // Initializes move history
    }

    // Tracks the round number, incrementing it
    // Ow Ka Sheng
    public void trackRound() {
        round++;
    }

    // Switches Tor pieces to Xor pieces and vice versa, and updates the pieces on
    // the board
    // Ow Ka Sheng
    public Map<String, ArrayList<Piece>> switchTorXor() {
        round = 0;
        Map<String, ArrayList<Piece>> teamPieces = new HashMap<>();
        teamPieces.put("Red", new ArrayList<>());
        teamPieces.put("Blue", new ArrayList<>());

        Piece[][] board = chessBoard.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Piece piece = board[i][j];
                if (piece instanceof Tor || piece instanceof Xor) {
                    // Convert piece to opposite type (Tor <=> Xor)
                    Piece newPiece = (piece instanceof Tor) ? new Xor(piece) : new Tor(piece);
                    board[i][j] = newPiece;

                    // Add to appropriate team's list
                    teamPieces.get(newPiece.getTeam()).add(newPiece);
                }
            }
        }

        return teamPieces;
    }

    // Saves the current game state (board, moves, round, etc.) to a file
    // Lau Zi Herng
    public void saveGame(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Save metadata
            writer.write("---METADATA---\n");
            writer.write("Timestamp: " + java.time.LocalDateTime.now() + "\n");

            // Save state information (RedState or BlueState)
            writer.write("---STATE---\n");
            writer.write(state.getClass().getSimpleName() + "\n");

            // Save board state
            writer.write("---BOARD---\n");
            for (int i = 0; i < ChessBoard.ROWS; i++) {
                for (int j = 0; j < ChessBoard.COLUMNS; j++) {
                    Piece piece = chessBoard.getBoard()[i][j];
                    if (piece != null) {
                        if (piece instanceof Ram) {
                            Ram ram = (Ram) piece;
                            writer.write(String.format("%d,%d,%s,%s,%s,%s,%s,%s%n",
                                    i, j,
                                    piece.getClass().getSimpleName(),
                                    piece.getTeam(),
                                    ram.getFlipIconPath(),
                                    ram.getInitialIconPath(),
                                    ram.getNextPosOperator(),
                                    ram.getIconPath()));
                        } else {
                            writer.write(String.format("%d,%d,%s,%s%n",
                                    i, j,
                                    piece.getClass().getSimpleName(),
                                    piece.getTeam()));
                        }
                    }
                }
            }

            // Save piece count
            writer.write("---PIECE COUNT---\n");
            writer.write("Piece Count: " + chessBoard.getPieceCount() + "\n");

            // Save move history
            writer.write("---MOVES---\n");
            for (String move : moveHistory) {
                writer.write(move + "\n");
            }

            // Save current round
            writer.write("---ROUND---\n");
            writer.write(String.valueOf(round));

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    // Loads a saved game from a file
    // Lau Zi Herng
    public boolean loadGame(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            Piece[][] board = new Piece[ChessBoard.ROWS][ChessBoard.COLUMNS];

            String line;
            boolean readingState = false;
            boolean readingBoard = false;
            boolean readingMoves = false;
            boolean readingRound = false;
            boolean readingPieceCount = false;

            moveHistory.clear(); // Clear previous history before loading new game
            while ((line = reader.readLine()) != null) {
                if (line.equals("---METADATA---")) {
                    // Read and skip metadata section
                    reader.readLine();
                    continue;
                }
                if (line.equals("---STATE---")) {
                    readingState = true;
                    continue;
                }
                if (line.equals("---BOARD---")) {
                    readingState = false;
                    readingBoard = true;
                    continue;
                }
                if (line.equals("---MOVES---")) {
                    readingBoard = false;
                    readingMoves = true;
                    continue;
                }
                if (line.equals("---ROUND---")) {
                    readingMoves = false;
                    readingRound = true;
                    continue;
                }
                if (line.equals("---PIECE COUNT---")) {
                    readingPieceCount = true;
                    continue;
                }

                if (readingState) {
                    // Handle state (either RedState or BlueState)
                    switch (line) {
                        case "RedState":
                            this.state = new RedState();
                            break;
                        case "BlueState":
                            this.state = new BlueState();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid state in save file: " + line);
                    }
                } else if (readingRound) {
                    // Read and set the round number
                    round = Integer.parseInt(line);
                } else if (readingMoves) {
                    // Add the moves to the move history
                    moveHistory.add(line);
                } else if (readingPieceCount) {
                    // Read and set the piece count
                    int pieceCount = Integer.parseInt(line.split(":")[1].trim());
                    chessBoard.setPieceCount(pieceCount);
                    readingPieceCount = false;
                } else if (readingBoard) {
                    // Read and parse the board setup
                    String[] parts = line.split(",");
                    int row = Integer.parseInt(parts[0]);
                    int column = Integer.parseInt(parts[1]);
                    String pieceType = parts[2];
                    String team = parts[3];
                    Position position = new Position(row, column);

                    // Create the piece and handle Ram-specific fields
                    Piece piece = chessBoard.createPiece(pieceType, position, team, state.getTeam());

                    // Additional handling for Ram pieces
                    if (piece instanceof Ram && parts.length >= 7) {
                        Ram ram = (Ram) piece;
                        ram.setFlipIconPath(parts[4]);
                        ram.setInitialIconPath(parts[5]);
                        ram.setNextPosOperator(parts[6]);
                        ram.iconPath = parts[7];
                    }

                    if (piece != null) {
                        board[row][column] = piece;
                    }
                }
            }
            chessBoard.setBoard(board); // Set the loaded board state
            chessBoard.setSelectedPiece(null); // Clear selected piece

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return false;
        }
        return true;
    }

    // Determines the winner of the game based on the presence of Sau pieces
    // Lau Zi Herng
    public String determineWinner() {
        boolean isBlueSauFound = false;
        boolean isRedSauFound = false;
        Piece[][] board = chessBoard.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] instanceof Sau) {
                    if (board[i][j].getTeam().equals("Blue"))
                        isBlueSauFound = true;
                    else
                        isRedSauFound = true;
                }
            }
        }
        return isRedSauFound ? (isBlueSauFound ? "None" : "Red") : "Blue";
    }

    // Adds a move to the move history
    // Harold Goh
    public void addMoveToHistory(int nextMove) {
        String pos = Position.convertPositionToChessNotation(nextMove);
        moveHistory.add(pos);
    }

    // Clears the move history
    // Harold Goh
    public void clearMoveHistory() {
        moveHistory.clear();
    }

    // Sets the current player state (either RedState or BlueState)
    // Ow Ka Sheng
    public void setState(PlayerState state) {
        this.state = state;
    }

    // Returns the move history
    // Harold Goh
    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }

    // Returns the current chess board
    // Harold Goh
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    // Returns the current team's turn
    // Ow Ka Sheng
    public String getCurrentTurnTeam() {
        return state.getTeam();
    }

    // Returns the current player state
    // Ow Ka Sheng
    public PlayerState getPlayerState() {
        return state;
    }

    // Returns the current round number
    // Ow Ka Sheng
    public int getRound() {
        return round;
    }

}
