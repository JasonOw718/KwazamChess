
/**
 * The RedState class represents the state of a player who is on the Red team in the chess game.
 * This class implements the PlayerState interface and follows the State design pattern.
 * 
 * The State design pattern is used here to allow an object to alter its behavior when its internal state changes.
 * The RedState class is one of the concrete states that the context (ChessModel) can be in.
 * 
 * Methods:
 * - playMove(ChessModel context, int position): Changes the state to BlueState after a move is played,
 *   updates the chess board, and tracks the round.
 * - getTeam(): Returns the team name "Red".
 * 
 * Relationships:
 * - RedState is a concrete implementation of the PlayerState interface.
 * - RedState inherits Parent PlayerState
 * - It interacts with the ChessModel context to change the state to BlueState after a move is played.
 * - It updates the chess board by calling the updateBoard method on the ChessBoard object.
 */

// Ow Ka Sheng
public class RedState implements PlayerState {

    // Override playMove method
    @Override
    public void playMove(ChessModel context, int position) {
        context.setState(new BlueState()); // change model's turn
        context.getChessBoard().updateBoard(position); // make a move on chessBoard
        context.trackRound(); // add round
    }

    // Override getTeam method
    @Override
    public String getTeam() {
        return "Red";
    }
}
