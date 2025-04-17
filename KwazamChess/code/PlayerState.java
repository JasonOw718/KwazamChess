/**
 * This implementation follows state design pattern.
 * PlayerState serves as an interface, representing the state of a player in the chess game.
 * It is part of the State design pattern, where each concrete state
 * will implement this interface to define specific behaviors.
 * 
 * It is a parent interface to child class to implements(RedState and BlueState)
 */
// Ow Ka Sheng
interface PlayerState {

    void playMove(ChessModel context, int position);

    String getTeam();

}
