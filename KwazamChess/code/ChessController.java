import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// This class follows the MVC (Model-View-Controller) and State design pattern.
// MVC: 
// - Model: Handles the game logic and state (ChessModel).
// - View: Manages the UI and user interaction (ChessView).
// - Controller: Acts as an intermediary between the Model and the View, updating the model based on user actions and updating the view based on model changes.
// This ChessController class represents controller class to handle the interaction between the ChessModel (logic) and ChessView (UI)
// State design pattern:
// - ChessController class serves as a client that interacts with the ChessModel context and handles the state-specific behaviors.
// - It delegates the decision-making and state transitions to the ChessModel, based on player actions and the current game state.

public class ChessController {

    private ChessModel model; // Chess game logic and data
    private ChessView view; // Chess game UI

    // Ow Ka Sheng
    public ChessController(ChessModel model) {
        // Initializes the model and view, setting up the view's chessboard
        this.model = model;
        this.view = new ChessView(model.getChessBoard().getBoard());

        // Toggles the visibility of the pieces depending on the current team
        view.togglePlayerPieceState(model.getCurrentTurnTeam());

        // Sets up various actions associated with buttons and menu items
        view.getClearButton().addActionListener(new ClearAction());
        view.getSaveGameItem().addActionListener(new SaveGameAction());
        view.getLoadGameItem().addActionListener(new LoadGameAction());
        view.getNewGameItem().addActionListener(new RestartAction());
        view.getRestartGameItem().addActionListener(new RestartAction());
        view.getExitItem().addActionListener(e -> System.exit(0)); // Exits the application
        view.getLightThemeItem().addActionListener(e -> view.changeTheme(true)); // Switches to light theme
        view.getDarkThemeItem().addActionListener(e -> view.changeTheme(false)); // Switches to dark theme
        view.getOnSoundItem().addActionListener(e -> view.setAudioEnabled(true)); // Turns audio on
        view.getOffSoundItem().addActionListener(e -> view.setAudioEnabled(false)); // Turns audio off
        view.getRulesItem().addActionListener(e -> view.displayRules()); // Displays the game rules
        view.getAboutItem().addActionListener(e -> view.displayAbout()); // Displays about information

        // Listens for window resize events to adjust the board view
        view.getLayeredPane().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                view.resizeWindow(model.getChessBoard().getBoard());
            }
        });

        // Sets up listeners for the cells (buttons) in the chessboard
        setUpCellActionListener();
    }

    // Ow Ka Sheng
    public void setUpCellActionListener() {
        // Loops through all the chessboard cells (buttons) and adds listeners to them
        JButton[][] pieces = view.getChessPieces();
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[0].length; col++) {
                JButton cell = pieces[row][col];
                cell.addActionListener(new CellAction()); // Action listener for cell clicks
                cell.addMouseListener(new MouseAdapter() { // Mouse listener for hover effects
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        view.showHoverEffect(cell); // Highlights the cell on hover
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        view.hideHoverEffect(); // Removes the hover effect
                    }
                });
            }
        }
    }

    // Ow Ka Sheng
    private class CellAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handlePieceAction(e); // Handles the action when a cell is clicked
        }
    }

    // Ow Ka Sheng
    private class ClearAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.clearMoveHistory(); // Clears the move history in the model
            view.clearMoves(); // Clears the displayed move history in the view
        }
    }

    // Ow Ka Sheng
    private class RestartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            restartGame(); // Restarts the game
        }
    }

    // Ow Ka Sheng
    private class SaveGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            model.saveGame("savegame.txt"); // Saves the current game state to a file
            view.displaySaveSuccessfulDialog(); // Notifies the user that the game was saved
        }
    }

    // Ow Ka Sheng
    private class LoadGameAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Attempts to load a previously saved game
            boolean loadSuccessful = model.loadGame("savegame.txt");
            if (loadSuccessful) {
                ChessBoard board = model.getChessBoard();
                view.setUpChessBoard(board.getBoard()); // Sets up the chessboard
                setUpCellActionListener(); // Sets up the action listeners for the board cells
                view.refreshMoveHistory(model.getMoveHistory()); // Refreshes the move history view
                String currentTeam = model.getCurrentTurnTeam();
                boolean isBlueTurn = currentTeam.equals("Blue");

                // Flips the board view if the team is not blue
                if (!isBlueTurn) {
                    board.flipBoard(currentTeam);
                    view.setIsFlipped(false);
                    view.flipBoardView(board.getBoard());
                } else {
                    view.setIsFlipped(false);
                }

                // Updates the turn indicator and other visual elements
                view.toggleBoardLabel(currentTeam);
                view.updateTurnIndicator(currentTeam);
                view.togglePlayerPieceState(currentTeam);
                board.setSelectedPiece(null); // Deselects any previously selected piece
                view.displayLoadDialog("Success"); // Displays success message
            } else {
                view.displayLoadDialog("Failed"); // Displays failure message
            }
        }
    }

    // Ow Ka Sheng
    public void handlePieceAction(ActionEvent e) {
        JButton clickedBtn = (JButton) e.getSource();
        String clickedTeam = (String) clickedBtn.getClientProperty("team");
        ChessBoard chessBoard = model.getChessBoard();
        Piece lastSelectedPiece = chessBoard.getSelectedPiece();
        String currentTeam = model.getCurrentTurnTeam();

        // Check if the clicked piece belongs to the current player or not
        if (clickedTeam == null || (lastSelectedPiece != null && !clickedTeam.equals(currentTeam)))
            updateGame(e); // Updates the game state if the piece is valid for the current turn
        else {
            // If a piece is selected, highlights its valid moves
            ArrayList<int[]> validMoves = (lastSelectedPiece == null) ? new ArrayList<>()
                    : lastSelectedPiece.getValidMoves();
            view.setHighlightPath(validMoves, false);
            int piecePos = Integer.parseInt(e.getActionCommand());
            int[] rowCol = Position.convertPositionToRowColumn(piecePos);
            Piece piece = chessBoard.getBoard()[rowCol[0]][rowCol[1]];
            chessBoard.setSelectedPiece(piece); // Sets the clicked piece as selected
            view.pieceSelectSound(); // Plays a sound for piece selection
            view.setHighlightPath(chessBoard.getSelectedPiece().getValidMoves(), true); // Highlights the valid moves
                                                                                        // for the selected piece
        }
    }

    // Ow Ka Sheng
    public void updateGame(ActionEvent e) {
        ChessBoard chessBoard = model.getChessBoard();
        int prevPieceCount = chessBoard.getPieceCount();
        int position = Integer.parseInt(e.getActionCommand());
        Piece piece = chessBoard.getSelectedPiece();
        Position piecePos = piece.getPosition();
        int prevRow = piecePos.getRow();
        int prevCol = piecePos.getColumn();
        int[] rowCol = Position.convertPositionToRowColumn(position);

        // Removes highlights of the previous valid moves
        view.setHighlightPath(piece.getValidMoves(), false);

        // Updates the board state and plays the move (State design pattern)
        model.getPlayerState().playMove(model, position);
        view.updateBoardView(prevRow, prevCol, rowCol[0], rowCol[1]);

        // Adds the move to the history and updates the move list view
        model.addMoveToHistory(position);
        view.refreshMoveHistory(model.getMoveHistory());

        // Plays sound based on whether a piece was taken or just moved
        if (prevPieceCount > chessBoard.getPieceCount())
            view.pieceTakenOutSound();
        else
            view.pieceMoveSound();

        // Switches between 'Tor' and 'Xor' view for every 2 rounds
        if (model.getRound() == 2)
            view.switchTorXorView(model.switchTorXor());

        chessBoard.setSelectedPiece(null); // Deselects the piece after the move

        // Checks if the game has ended after the move
        boolean isEnd = checkGameEnded();
        if (!isEnd) {
            String currentTeam = model.getCurrentTurnTeam();
            chessBoard.flipBoard(currentTeam); // Flips the board if needed
            view.updateTurnIndicator(currentTeam); // Updates the turn indicator
            view.flipBoardView(chessBoard.getBoard()); // Updates the board view
            view.toggleBoardLabel(currentTeam); // Toggles the label indicating the current team
            view.togglePlayerPieceState(currentTeam); // Updates the visible pieces for the current team
        }
    }

    // Ow Ka Sheng
    public boolean checkGameEnded() {
        String winner = model.determineWinner(); // Determines the winner of the game
        if (!winner.equals("None")) {
            view.displayWinner(winner); // Displays the winner
            restartGame(); // Restarts the game
            return true;
        }
        return false;
    }

    // Ow Ka Sheng
    public void restartGame() {
        view.clearMoves(); // Clears the displayed move history
        model.restartChessGame(); // Resets the model and chessboard to initial state
        ChessBoard chessBoard = model.getChessBoard();
        view.setUpChessBoard(chessBoard.getBoard()); // Sets up the new chessboard view
        view.setIsFlipped(false); // Resets the board flip state
        String currentTeam = model.getCurrentTurnTeam();
        view.toggleBoardLabel(currentTeam); // Toggles the board label for the current team
        setUpCellActionListener(); // Sets up listeners for the new game
        view.togglePlayerPieceState(currentTeam); // Toggles the pieces for the new team
        view.updateTurnIndicator(currentTeam); // Updates the turn indicator
        chessBoard.setSelectedPiece(null); // Deselects any selected piece
        view.displayRestartSuccessfulDialog(); // Notifies the user that the game has been restarted
    }

}
