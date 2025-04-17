
/**
 * The BlueState class represents the state of a player who is on the Blue team
 * in the chess game.
 * This class implements the PlayerState interface and follows the State design
 * pattern.
 * 
 * The State design pattern is used here to allow an object to alter its
 * behavior when its internal state changes.
 * The BlueState class is one of the concrete states that the context
 * (ChessModel) can be in.
 * 
 * Methods:
 * - playMove(ChessModel context, int position): Changes the state to RedState after a move is played,
 * updates the chess board, and tracks the round.
 * - getTeam(): Returns the team name "Blue".
 * 
 * Relationships:
 * - BlueState is a concrete implementation of the PlayerState interface.
 * - it inherit parent PlayerState
 * - It interacts with the ChessModel context to change the state to RedState
 * after a move is played.
 * - It updates the chess board by calling the updateBoard method on the
 * ChessBoard object.
 * 
 */
public class BlueState implements PlayerState {

    // Override play move method
    // Ow Ka Sheng
    @Override
    public void playMove(ChessModel context, int position) {
        context.setState(new RedState()); // change model's turn
        context.getChessBoard().updateBoard(position); // make a move on chessBoard
    }

    // Override get team method
    // Ow Ka Sheng
    @Override
    public String getTeam() {
        return "Blue";
    }
}
