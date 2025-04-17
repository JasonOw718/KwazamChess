/**
 * Represents the position of a piece on the chessboard.
 * The position can be represented in terms of row-column or as a single index. (on view)
 */
// Ow Ka Sheng
public class Position {
    private int row; // The row index of the position on the board.
    private int column; // The column index of the position on the board.

    /**
     * Constructor to create a Position from a single integer index.
     *
     * @param pos The index representing the position on the board view.
     *            It is converted to row and column using board dimensions.
     */
    public Position(int pos) {
        int[] rowColumn = convertPositionToRowColumn(pos);
        this.row = rowColumn[0];
        this.column = rowColumn[1];
    }

    /**
     * Constructor to create a Position from row and column indices.
     *
     * @param row    The row index of the position.
     * @param column The column index of the position.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Converts a position index to chessboard notation (e.g., A1, B2).
     *
     * @param position The index of the position on the board.
     * @return The position in chess notation format.
     */
    public static String convertPositionToChessNotation(int position) {
        // Calculate the column index from the position.
        int column = position % ChessBoard.COLUMNS;
        // Calculate the row index from the position.
        int row = position / ChessBoard.COLUMNS;

        // Convert the column index to a letter (e.g., 0 -> A, 1 -> B).
        char columnChar = (char) ('A' + column);
        // Convert the row index to a chessboard row number (e.g., 0 -> 8, 1 -> 7).
        int rowNumber = 8 - row;

        // Return the chess notation as a string.
        return columnChar + String.valueOf(rowNumber);
    }

    /**
     * Converts a row and column index to a single position index.
     *
     * @param row    The row index of the position.
     * @param column The column index of the position.
     * @return The position index calculated from row and column.
     */
    public static int convertRowColumnToPosition(int row, int column) {
        // Multiply the row by the number of columns and add the column index.
        return row * ChessBoard.COLUMNS + column;
    }

    /**
     * Converts a single position index into row and column indices.
     *
     * @param position The index of the position on the board.
     * @return An array containing row and column indices.
     */
    public static int[] convertPositionToRowColumn(int position) {
        // Calculate the column index from the position.
        int column = position % ChessBoard.COLUMNS;
        // Calculate the row index from the position.
        int row = position / ChessBoard.COLUMNS;
        // Return the row and column as an array.
        return new int[] { row, column };
    }

    /**
     * Getter for the row index of the position.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for the column index of the position.
     *
     * @return The column index.
     */
    public int getColumn() {
        return column;
    }
}
