import java.util.ArrayList;

// Abstract class(Parent class) representing a game piece on the board
// Inherited by child classes(Ram,Biz,Sau,Tor,Xor)
// Ow Ka Sheng
public abstract class Piece {

    // Instance variables representing the position, team, icon path, and possible moves of the piece
    protected Position position; // The current position of the piece on the board
    protected String team; // The team (e.g., "Red", "Blue") the piece belongs to
    protected String iconPath; // The icon file path representing the piece
    protected ArrayList<int[]> possibleNextMove; // List of possible moves for the piece

    // Constructor to initialize the piece with its position, team, and icon path
    public Piece(Position position, String team, String iconPath) {
        this.position = position; // Set the position of the piece
        this.team = team; // Set the team of the piece
        this.iconPath = iconPath; // Set the icon path representing the piece
        this.possibleNextMove = new ArrayList<int[]>(); // Initialize the list for possible moves
    }

    // Method to get the list of valid (possible) moves for the piece
    public ArrayList<int[]> getValidMoves() {
        return possibleNextMove; // Return the list of valid moves
    }

    // Getter method to return the team of the piece
    public String getTeam() {
        return team; // Return the team of the piece
    }

    // Getter method to return the icon path for the piece
    public String getIconPath() {
        return iconPath; // Return the icon path
    }

    // Getter method to return the current position of the piece
    public Position getPosition() {
        return position; // Return the position of the piece
    }

    // Setter method to update the position of the piece
    public void setPosition(Position position) {
        this.position = position; // Set the position of the piece
    }

    // Setter method to update the icon path of the piece
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath; // Set the icon path for the piece
    }

    // Method to move the piece to a new position based on the given move (nextMove)
    public Position move(int nextMove) {
        this.position = new Position(nextMove); // Update the position to the new position
        return this.position; // Return the new position
    }

    // Abstract method to set the valid moves for a specific piece
    public abstract void setCurrentValidMoves(Piece[][] board);
}
