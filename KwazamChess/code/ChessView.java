import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

// This class follows the MVC (Model-View-Controller) design pattern.
// - Model: Handles the game logic, state, and data (ChessModel).
// - View: Displays the UI (handled by ChessView).
// - Controller: Interacts with the model and updates the view (handled by ChessController).
// ChessView represents View class to displays the UI.

public class ChessView extends JFrame {

    private Color boardColor_1 = new Color(242, 202, 92);
    private Color boardColor_2 = new Color(248, 231, 187);
    private Color taskbarColor = new Color(255, 235, 205);
    private Color taskbarItemColor = new Color(250, 235, 215);
    private Color rowColor = new Color(250, 235, 215);
    private Color colColor = new Color(250, 235, 215);
    private Color movesColor = new Color(250, 223, 173);
    private Color hintColor = new Color(144, 238, 144);
    private Color turnLabelColor = new Color(255, 235, 205);

    private Border border;

    private JPanel chessPanel;
    private JButton[][] chessPieces;

    private JMenuBar taskBar;
    private JPanel moves;
    private JPanel movesContent;
    private JButton clear;

    private JPanel boardRows;
    private JPanel boardColumns;
    private JLabel rowLabel;
    private JLabel colLabel;
    private JLabel turnIndicator;

    private JButton lastHoveredPiece;
    private Color lastOriginalColor;
    private Border lastOriginalBorder;

    private JMenu game;
    private JMenu move;
    private JMenu settings;
    private JMenu sound;
    private JMenu theme;
    private JMenu help;

    private JMenuItem loadGameItem;
    private JMenuItem saveGameItem;
    private JMenuItem newGameItem;
    private JMenuItem restartGameItem;
    private JMenuItem exitItem;

    private JMenuItem rulesItem;
    private JMenuItem aboutItem;

    private JMenuItem onSoundItem;
    private JMenuItem offSoundItem;

    private JMenuItem lightThemeItem;
    private JMenuItem darkThemeItem;

    private JPanel emptyBox;

    private boolean isAudioEnabled;
    private boolean isFlipped;

    private JLayeredPane layeredPane;

    // ChessView constructor
    // Yoong Tzer Shih
    public ChessView(Piece[][] board) {

        setTitle("Kwazam Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(810, 770);

        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("src/Icon.png");
        setIconImage(icon.getImage());

        border = new LineBorder(Color.black, 2);

        chessPanel = new JPanel();
        boardRows = new JPanel();
        boardColumns = new JPanel();

        lastHoveredPiece = null;
        lastOriginalColor = null;
        lastOriginalBorder = null;
        isAudioEnabled = true;
        isFlipped = false;

        setUpChessBoard(board);
        taskbarSetUp();
        movesPanelSetUp();
        toggleBoardLabel("Blue");
        layeredPaneSetUp();

        setVisible(true);
    }

    // Getter Methods
    // Yoong Tzer Shih
    // ---------------------------------------------------------------------------------------------------
    // Method to return clear button for move history
    public JButton getClearButton() {
        return clear;
    }

    // Method to return chess pieces button array
    public JButton[][] getChessPieces() {
        return chessPieces;
    }

    // Method to return load game menu item
    public JMenuItem getLoadGameItem() {
        return loadGameItem;
    }

    // Method to return save game menu item
    public JMenuItem getSaveGameItem() {
        return saveGameItem;
    }

    // Method to return new game menu item
    public JMenuItem getNewGameItem() {
        return newGameItem;
    }

    // Method to return restart game menu item
    public JMenuItem getRestartGameItem() {
        return restartGameItem;
    }

    // Method to return exit menu item
    public JMenuItem getExitItem() {
        return exitItem;
    }

    // Method to return "On Sound" menu item
    public JMenuItem getOnSoundItem() {
        return onSoundItem;
    }

    // Method to return "Off Sound" menu item
    public JMenuItem getOffSoundItem() {
        return offSoundItem;
    }

    // Method to return light theme menu item
    public JMenuItem getLightThemeItem() {
        return lightThemeItem;
    }

    // Method to return dark theme menu item
    public JMenuItem getDarkThemeItem() {
        return darkThemeItem;
    }

    // Method to return rules menu item
    public JMenuItem getRulesItem() {
        return rulesItem;
    }

    // Method to return about menu item
    public JMenuItem getAboutItem() {
        return aboutItem;
    }

    // Method to return about hover color
    private Color getHoverColor(Color baseColor) {
        return new Color(
                Math.min(255, (int) (baseColor.getRed() * 1.1)),
                Math.min(255, (int) (baseColor.getGreen() * 1.1)),
                Math.min(255, (int) (baseColor.getBlue() * 1.1)));
    }

    // Method to return about layered pane
    public JLayeredPane getLayeredPane() {
        return layeredPane;
    }
    // Setter Methods
    // Yoong Tzer Shih
    // ---------------------------------------------------------------------------------------------------

    // Set the menu style for each menu item
    // Yoong Tzer Shih
    private void setMenuStyle(JMenu menu) {

        for (int i = 0; i < menu.getItemCount(); i++) {

            JMenuItem item = menu.getItem(i);
            item.setBackground(taskbarItemColor);
            item.setBorder(border);
            item.setOpaque(true);
        }
    }

    // Set the audio enabled status
    // Yoong Tzer Shih
    public void setAudioEnabled(boolean isAudioEnabled) {
        this.isAudioEnabled = isAudioEnabled;
    }

    // Set the flip status
    // Yoong Tzer Shih
    public void setIsFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    // Sets the icon for the button and handles
    // Ow Ka Sheng
    private void setIcon(JButton btn, String iconPath, int width, int height) {
        if (width <= 0 || height <= 0)
            return;
        try {
            // Load and scale the image
            Image icon = ImageIO.read(getClass().getResource(iconPath));
            ImageIcon imageIcon = new ImageIcon(icon.getScaledInstance(width, height, Image.SCALE_SMOOTH));

            // Set the icon and disabled icon for the button
            btn.setIcon(imageIcon);
            btn.setDisabledIcon(imageIcon);
        } catch (IOException ex) {
            // Log error if image loading fails
            System.err.println("Failed to load images: " + ex.getMessage());
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Display the game rules in a dialog box
    // Yoong Tzer Shih
    public void displayRules() {
        JDialog contentWindow = new JDialog();
        contentWindow.setTitle("Rules");
        contentWindow.setSize(600, 400);
        contentWindow.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        try (BufferedReader reader = new BufferedReader(new FileReader("README/Rules.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                textArea.append(line + "\n");
            }

        }

        catch (IOException ex) {

            JOptionPane.showMessageDialog(contentWindow, "Error loading file: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        contentWindow.add(scrollPane);

        contentWindow.setVisible(true);
    }

    // ---------------------------------------------------------------------------------------------------

    // Display the about information in a dialog box
    // Yoong Tzer Shih
    public void displayAbout() {
        JDialog contentWindow = new JDialog();
        contentWindow.setTitle("Rules");
        contentWindow.setSize(600, 400);
        contentWindow.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        try (BufferedReader reader = new BufferedReader(new FileReader("README/About.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                textArea.append(line + "\n");
            }
        }

        catch (IOException ex) {

            JOptionPane.showMessageDialog(contentWindow, "Error loading file: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        contentWindow.add(scrollPane);

        contentWindow.setVisible(true);
    }

    // ---------------------------------------------------------------------------------------------------

    // Change the theme of the application based on light or dark theme
    // Yoong Tzer Shih
    public void changeTheme(boolean isLightTheme) {
        if (isLightTheme) {
            applyLightTheme();
        } else {
            applyDarkTheme();
        }

        emptyBox.setBackground(rowColor);
        turnIndicator.setBackground(turnLabelColor);
        updateTaskbarColors();
        updateMovesPanel();
        repaint();

    }

    // ---------------------------------------------------------------------------------------------------

    // Apply the dark theme to the application
    // Yoong Tzer Shih
    private void applyDarkTheme() {

        boardColor_1 = new Color(139, 69, 19);
        boardColor_2 = new Color(210, 105, 30);
        taskbarColor = new Color(181, 101, 29);
        taskbarItemColor = new Color(181, 101, 29);
        rowColor = new Color(218, 145, 0);
        colColor = new Color(218, 145, 0);
        movesColor = new Color(244, 164, 96);
        hintColor = new Color(255, 218, 185);
        turnLabelColor = new Color(181, 101, 29);

        updateBoardColor(boardColor_1, boardColor_2);
        updateBoardLabelColors(rowColor, colColor);
        updateMenuForegroundColors(new Color(255, 239, 213));
        clear.setForeground(new Color(255, 239, 213));
    }

    // ---------------------------------------------------------------------------------------------------

    // Apply the light theme to the application
    // Yoong Tzer Shih
    private void applyLightTheme() {

        boardColor_1 = new Color(242, 202, 92);
        boardColor_2 = new Color(248, 231, 187);
        taskbarColor = new Color(255, 235, 205);
        taskbarItemColor = new Color(250, 235, 215);
        rowColor = new Color(250, 235, 215);
        colColor = new Color(250, 235, 215);
        movesColor = new Color(250, 223, 173);
        hintColor = new Color(144, 238, 144);
        turnLabelColor = new Color(255, 235, 205);

        updateBoardColor(boardColor_1, boardColor_2);
        updateBoardLabelColors(rowColor, colColor);
        updateMenuForegroundColors(Color.black);
        clear.setForeground(Color.black);

    }

    // ---------------------------------------------------------------------------------------------------

    // Update the colors of the board labels (row and column labels)
    // Yoong Tzer Shih
    private void updateBoardLabelColors(Color rowColor, Color colColor) {

        boardRows.setBackground(rowColor);
        boardColumns.setBackground(colColor);
    }

    // ---------------------------------------------------------------------------------------------------

    // Update the board colors based on the specified colors
    // Yoong Tzer Shih
    private void updateBoardColor(Color color1, Color color2) {

        for (int row = 0; row < chessPieces.length; row++) {

            for (int col = 0; col < chessPieces[0].length; col++) {

                JButton piece = chessPieces[row][col];

                if ((row + col) % 2 == 0) {

                    piece.setBackground(color1);
                }

                else {

                    piece.setBackground(color2);
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Update the foreground colors of the menu items
    // Yoong Tzer Shih
    private void updateMenuForegroundColors(Color color) {

        updateMenuColors(game, color);
        updateMenuColors(move, color);
        updateMenuColors(settings, color);
        updateMenuColors(sound, color);
        updateMenuColors(theme, color);
        updateMenuColors(help, color);
    }

    // ---------------------------------------------------------------------------------------------------

    // Update the colors of menu items for a specific menu
    // Yoong Tzer Shih
    private void updateMenuColors(JMenu menu, Color color) {

        for (int i = 0; i < menu.getItemCount(); i++) {

            menu.setForeground(color);

            JMenuItem item = menu.getItem(i);
            item.setForeground(color);
            item.setBackground(taskbarItemColor);
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Update the taskbar background color
    // Yoong Tzer Shih
    private void updateTaskbarColors() {

        taskBar.setBackground(taskbarColor);
    }

    // ---------------------------------------------------------------------------------------------------

    // Update the colors of the moves panel
    // Yoong Tzer Shih
    private void updateMovesPanel() {

        moves.setBackground(movesColor);
        movesContent.setBackground(movesColor);
        clear.setBackground(taskbarColor);

    }

    // ---------------------------------------------------------------------------------------------------

    // Sets up the chessboard UI with buttons representing each position
    // Ow Ka Sheng
    public void setUpChessBoard(Piece[][] board) {

        // Initialize the chess pieces grid and clear the panel
        chessPieces = new JButton[board.length][board[0].length];
        chessPanel.removeAll();
        chessPanel.setBackground(Color.black);
        chessPanel.setLayout(new GridLayout(8, 5));

        // Iterate through each position on the board
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Piece piece = board[row][col];
                JButton cell = new JButton();
                int position = Position.convertRowColumnToPosition(row, col);
                cell.setFocusable(false);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
                cell.setActionCommand(position + "");
                cell.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
                cell.setBorder(new LineBorder(Color.black, 2));
                cell.setEnabled(true);

                // Set the background color based on the position
                if (position % 2 == 0) {
                    cell.setBackground(boardColor_1);
                } else {
                    cell.setBackground(boardColor_2);
                }

                // Add the cell to the panel and store the button reference
                chessPanel.add(cell);
                chessPieces[row][col] = cell;

                // Skip if there is no piece at the current position
                if (piece == null) {
                    cell.setEnabled(false);
                    continue;
                }

                // Set the icon for the piece on the button
                if (layeredPane != null) {
                    resizeButtonIcon(board[row][col], chessPieces[row][col], layeredPane.getSize());
                } else {
                    setIcon(cell, piece.getIconPath(), 120, 80);
                }

                // Store the team information for the piece
                cell.putClientProperty("team", piece.getTeam());
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Shows hover effect on the button when the mouse hovers over it
    // Ow Ka Sheng
    public void showHoverEffect(JButton cell) {
        // Check if the cell is enabled and has an icon (indicating it's a valid piece)
        if (cell.isEnabled() && cell.getIcon() != null) {
            // Store original properties for later restoration
            lastHoveredPiece = cell;
            lastOriginalColor = cell.getBackground();
            lastOriginalBorder = cell.getBorder();

            // Apply hover effect: change background and add border
            cell.setBackground(getHoverColor(lastOriginalColor)); // Set a lighter background on hover
            cell.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(255, 255, 255, 100), 1), // Semi-transparent white border
                    new LineBorder(Color.black, 1))); // Black border around the piece
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Removes the hover effect and restores the original properties of the button
    // Ow Ka Sheng
    public void hideHoverEffect() {
        // Check if a piece was previously hovered
        if (lastHoveredPiece != null) {
            // Restore the original background and border
            lastHoveredPiece.setBackground(lastOriginalColor);
            lastHoveredPiece.setBorder(lastOriginalBorder);

            // Clear the stored hover information
            lastHoveredPiece = null;
            lastOriginalColor = null;
            lastOriginalBorder = null;
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Set up the taskbar UI elements
    // Yoong Tzer Shih
    private void taskbarSetUp() {

        taskBar = new JMenuBar();
        taskBar.setPreferredSize(new Dimension(800, 30));
        taskBar.setLayout(new BoxLayout(taskBar, BoxLayout.X_AXIS));
        taskBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Menu padding

        taskBar.setBackground(taskbarColor);
        taskBar.setBorder(border);

        game = new JMenu("Game");
        move = new JMenu("Moves");
        settings = new JMenu("Settings");
        sound = new JMenu("Sound");
        theme = new JMenu("Theme");
        help = new JMenu("Help");

        newGameItem = new JMenuItem("New Game");
        restartGameItem = new JMenuItem("Restart Game");
        exitItem = new JMenuItem("Exit");

        saveGameItem = new JMenuItem("Save Game");
        loadGameItem = new JMenuItem("Load Game");

        onSoundItem = new JMenuItem("On");
        offSoundItem = new JMenuItem("Off");

        lightThemeItem = new JMenuItem("Light");
        darkThemeItem = new JMenuItem("Dark");

        rulesItem = new JMenuItem("Rules");
        aboutItem = new JMenuItem("About");

        game.add(newGameItem);
        game.add(restartGameItem);
        game.add(exitItem);

        move.add(saveGameItem);
        move.add(loadGameItem);

        sound.add(onSoundItem);
        sound.add(offSoundItem);

        theme.add(lightThemeItem);
        theme.add(darkThemeItem);

        settings.add(sound);
        settings.add(theme);

        help.add(rulesItem);
        help.add(aboutItem);

        setMenuStyle(game);
        setMenuStyle(move);
        setMenuStyle(settings);
        setMenuStyle(sound);
        setMenuStyle(theme);
        setMenuStyle(help);

        taskBar.add(game);
        taskBar.add(Box.createHorizontalGlue()); // Add space between menus and only adjust the space when resize
        taskBar.add(move);
        taskBar.add(Box.createHorizontalGlue());
        taskBar.add(settings);
        taskBar.add(Box.createHorizontalGlue());
        taskBar.add(help);

        setJMenuBar(taskBar);
    }

    // ---------------------------------------------------------------------------------------------------

    // Set up the moves panel UI elements
    // Yoong Tzer Shih
    private void movesPanelSetUp() {

        moves = new JPanel();
        moves.setBackground(movesColor);
        moves.setBorder(border);
        moves.setLayout(new BoxLayout(moves, BoxLayout.Y_AXIS));

        movesContent = new JPanel();
        movesContent.setLayout(new BoxLayout(movesContent, BoxLayout.Y_AXIS));
        movesContent.setBackground(movesColor);

        JScrollPane scrollPane = new JScrollPane(movesContent);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 5));

        clear = new JButton("Clear");
        clear.setFocusable(false);
        clear.setAlignmentX(Component.CENTER_ALIGNMENT);
        clear.setBackground(taskbarColor);
        clear.setBounds(600, 0, 80, 50);

        moves.add(Box.createVerticalStrut(10));
        moves.add(clear);

        moves.add(Box.createVerticalStrut(10));
        moves.add(scrollPane);
    }

    // ---------------------------------------------------------------------------------------------------

    // Updates Move Panel Content
    // Harold Goh
    public void refreshMoveHistory(ArrayList<String> moveHistory) {

        movesContent.removeAll(); // Clear existing moves

        int fontSize = getWidth() / 48;

        for (int i = 0; i < moveHistory.size(); i++) {
            JLabel moveLabel = new JLabel(i + 1 + ": " + moveHistory.get(i));
            moveLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
            moveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movesContent.add(Box.createVerticalStrut(10));
            movesContent.add(moveLabel);
        }

        movesContent.revalidate();
        movesContent.repaint();
    }

    // ---------------------------------------------------------------------------------------------------

    // Flip board label
    // Ow Ka Sheng
    public void toggleBoardLabel(String team) {
        // Set up board rows
        boardRows.removeAll();
        boardRows.setBorder(border);
        boardRows.setBackground(rowColor);
        boardRows.setLayout(new GridLayout(8, 1));
        int fontSize = getWidth() / 50;

        if (team.equals("Red")) {
            // If team is red, reverse the row labels
            for (int i = 0; i < 8; i++) {
                rowLabel = new JLabel(String.valueOf(i + 1), SwingConstants.CENTER); // Reversed order
                rowLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
                boardRows.add(rowLabel);
            }
        } else {
            // Normal row label order
            for (int i = 0; i < 8; i++) {
                rowLabel = new JLabel(String.valueOf(8 - i), SwingConstants.CENTER);
                rowLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
                boardRows.add(rowLabel);
            }
        }

        // Set up board columns
        boardColumns.removeAll();
        boardColumns.setBorder(border);
        boardColumns.setBackground(colColor);
        boardColumns.setLayout(new GridLayout(1, 5));

        if (team.equals("Red")) {
            // If team is blue, reverse the column labels
            for (int i = 0; i < 5; i++) {
                colLabel = new JLabel(String.valueOf((char) ('E' - i)), SwingConstants.CENTER); // Reversed order
                colLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
                boardColumns.add(colLabel);
            }
        } else {
            // Normal column label order
            for (int i = 0; i < 5; i++) {
                colLabel = new JLabel(String.valueOf((char) ('A' + i)), SwingConstants.CENTER);
                colLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
                boardColumns.add(colLabel);
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Set up the layered pane UI elements
    // Yoong Tzer Shih
    private void layeredPaneSetUp() {
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        emptyBox = new JPanel();
        emptyBox.setBorder(border);
        emptyBox.setBackground(rowColor);

        turnIndicator = new JLabel("Blue's Turn", SwingConstants.CENTER);
        turnIndicator.setFont(new Font("Arial", Font.BOLD, 20));
        turnIndicator.setForeground(new Color(53, 89, 131, 255));
        turnIndicator.setOpaque(true);
        turnIndicator.setBackground(taskbarColor);
        turnIndicator.setBorder(border);
        // Adjust turn indicator to only span the chess panel width
        turnIndicator.setBounds(0, 0, 600, 30);

        // Keep moves panel at the top right, overlapping turn indicator
        moves.setBounds(600, 0, 200, 700);

        // Other components
        chessPanel.setBounds(30, 30, 570, 640);
        boardRows.setBounds(0, 30, 30, 640);
        boardColumns.setBounds(30, 670, 570, 30);
        emptyBox.setBounds(0, 664, 30, 40);

        // Add components with appropriate z-ordering
        layeredPane.add(chessPanel, Integer.valueOf(1));
        layeredPane.add(moves, Integer.valueOf(3)); // Highest z-index to overlap turn indicator
        layeredPane.add(boardRows, Integer.valueOf(2));
        layeredPane.add(boardColumns, Integer.valueOf(1));
        layeredPane.add(emptyBox, Integer.valueOf(1));
        layeredPane.add(turnIndicator, Integer.valueOf(2));

        add(layeredPane);
    }

    // ---------------------------------------------------------------------------------------------------

    // Resize Window
    // Yoong Tzer Shih
    public void resizeWindow(Piece[][] board) {

        Dimension newSize = layeredPane.getSize();

        // Turn indicator spans only up to moves panel
        turnIndicator.setBounds(0, 0, newSize.width - 200, 30);

        // Moves panel stays on top right
        moves.setBounds(newSize.width - 200, 0, 200, newSize.height);

        // Other components
        chessPanel.setBounds(30, 30, newSize.width - 230, newSize.height - 60);
        boardRows.setBounds(0, 30, 30, newSize.height - 60);
        boardColumns.setBounds(30, newSize.height - 30, newSize.width - 230, 30);
        emptyBox.setBounds(0, newSize.height - 39, 30, 40);

        int baseWidth = 800;
        int baseHeight = 700;
        int fontSize = getWidth() / 48;

        for (Component comp : movesContent.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel moveLabel = (JLabel) comp;
                moveLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
                moveLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }

        for (Component comp : boardRows.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel rowLabel = (JLabel) comp;
                rowLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
            }
        }

        for (Component comp : boardColumns.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel colLabel = (JLabel) comp;
                colLabel.setFont(new Font("Arial", Font.BOLD, fontSize));
            }
        }

        movesContent.revalidate();
        movesContent.repaint();
        boardColumns.revalidate();
        boardColumns.repaint();

        double scaleX = newSize.width / (double) baseWidth;
        double scaleY = newSize.height / (double) baseHeight;

        // Scale and set bounds for each component
        turnIndicator.setBounds(
                (int) (0 * scaleX), // x
                (int) (0 * scaleY), // y
                (int) (600 * scaleX), // width
                (int) (30 * scaleY) // height
        );
        int clearBtnFontSize = (int) (15 * scaleY);
        clear.setFont(new Font("Arial", Font.BOLD, clearBtnFontSize));
        clear.setBounds(
                (int) (600 * scaleX),
                (int) (0 * scaleY),
                (int) (80 * scaleX),
                (int) (50 * scaleY));
        
        moves.setBounds(
                (int) (600 * scaleX),
                (int) (0 * scaleY),
                (int) (200 * scaleX),
                (int) (700 * scaleY));

        chessPanel.setBounds(
                (int) (30 * scaleX),
                (int) (30 * scaleY),
                (int) (571 * scaleX),
                (int) (640 * scaleY));

        boardRows.setBounds(
                (int) (0 * scaleX),
                (int) (30 * scaleY),
                (int) (31 * scaleX),
                (int) (639 * scaleY));
        boardColumns.setBounds(
                (int) (31 * scaleX),
                (int) (669 * scaleY),
                (int) (570 * scaleX),
                (int) (32 * scaleY));
        emptyBox.setBounds(
                (int) (0 * scaleX),
                (int) (659 * scaleY),
                (int) (31 * scaleX),
                (int) (45 * scaleY));

        int turnIndicatorFontSize = (int) (20 * scaleY);

        turnIndicator.setFont(new Font("Arial", Font.BOLD, turnIndicatorFontSize));

        for (int row = 0; row < chessPieces.length; row++) {

            for (int col = 0; col < chessPieces[row].length; col++) {

                resizeButtonIcon(board[row][col], chessPieces[row][col], newSize);

            }
        }
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Resize Button
    // Yoong Tzer Shih
    private void resizeButtonIcon(Piece piece, JButton button, Dimension newSize) {
        boolean isMaximized = getExtendedState() == Frame.MAXIMIZED_BOTH;
        if (piece == null) {
            return;
        }

        if (isMaximized) {
            setIcon(button, piece.getIconPath(), 120, 80);
        }

        else {

            setIcon(button, piece.getIconPath(), (newSize.width - 230) / 5, (newSize.height - 60) / 8);
        }

    }

    // ---------------------------------------------------------------------------------------------------

    // create winner(blue or red) panel
    // Ow Ka Sheng
    private JPanel createWinnerPanel(Color backgroundColor, Color shadowColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Draw shadow
                g2d.setColor(shadowColor);
                g2d.fillRoundRect(5, 5, width - 10, height - 10, 30, 30);

                // Draw background
                g2d.setColor(backgroundColor);
                g2d.fillRoundRect(0, 0, width - 10, height - 10, 30, 30);
            }
        };
        panel.setOpaque(false); // Make the panel transparent to show custom painting
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    // ---------------------------------------------------------------------------------------------------

    // Play sound when a piece is selected
    // Yoong Tzer Shih
    public void pieceSelectSound() {
        if (isAudioEnabled) {
            try {

                File pieceSelect = new File("Audio/Piece_Select.wav");

                if (!pieceSelect.exists()) {
                    System.err.println("Audio file not found: " + "Audio/Piece_Select.wav");
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(pieceSelect);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                System.err.println("Error playing audio: " + ex.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Play sound when a piece is moved
    // Yoong Tzer Shih
    public void pieceMoveSound() {

        if (isAudioEnabled == true) {

            try {

                File pieceMoveSound = new File("Audio/Piece_Move.wav");

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(pieceMoveSound);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                if (!pieceMoveSound.exists()) {

                    System.err.println("Audio file not found: " + "Audio/Piece_Move.wav");
                }

            }

            catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {

                System.out.println(ex.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Play sound when a piece is taken out (captured)
    // Yoong Tzer Shih
    public void pieceTakenOutSound() {

        if (isAudioEnabled == true) {

            try {

                File pieceTakenOut = new File("Audio/Piece_Taken_Out.wav");

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(pieceTakenOut);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                if (!pieceTakenOut.exists()) {

                    System.err.println("Audio file not found: " + "Audio/Piece_Taken_Out.wav");
                }

            }

            catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {

                System.out.println(ex.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Toggles the enabled state of the pieces based on the current player's color
    // Ow Ka Sheng
    public void togglePlayerPieceState(String color) {
        // Iterate through each row and button (piece) on the chessboard
        for (JButton[] row : chessPieces) {
            for (JButton pieceBtn : row) {
                // Get the team (color) of the current piece
                String team = (String) pieceBtn.getClientProperty("team");

                // Enable the piece if it belongs to the current player, else disable it
                if (color.equals(team)) {
                    pieceBtn.setEnabled(true); // Enable the current player's pieces
                } else {
                    pieceBtn.setEnabled(false); // Disable the opponent's pieces
                    pieceBtn.setDisabledIcon(pieceBtn.getIcon()); // Keep the disabled icon for the opponent's pieces
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Highlights the possible path for a piece and enables or disables the buttons
    // based on the action
    // Ow Ka Sheng
    public void setHighlightPath(ArrayList<int[]> possiblePath, boolean enableButton) {
        // Iterate through each destination in the possible path
        for (int[] dest : possiblePath) {
            int row = dest[0]; // Row position of the destination
            int column = dest[1]; // Column position of the destination
            JButton btn = chessPieces[row][column]; // Button representing the destination cell

            // Enable the button and highlight it if enabling, else reset the button state
            if (enableButton) {
                btn.setEnabled(true); // Enable the button
                btn.setBackground(hintColor); // Set the highlight color
            } else {
                // Reset the background color and disable the button
                btn.setBackground((row + column) % 2 == 0 ? boardColor_1 : boardColor_2);
                btn.setEnabled(false); // Disable the button
            }
        }
    }

    // --------------------------------------------------------------------------------------------------

    // Updates the board view by transferring the piece from the previous to the new
    // position
    // Ow Ka Sheng
    public void updateBoardView(int prevRow, int prevCol, int newRow, int newCol) {

        // Get buttons for the previous and new positions
        JButton prevBtn = chessPieces[prevRow][prevCol];
        JButton newBtn = chessPieces[newRow][newCol];

        // Transfer icon and properties from the previous to the new button
        newBtn.setIcon(prevBtn.getIcon());
        newBtn.setDisabledIcon(prevBtn.getDisabledIcon());
        prevBtn.setIcon(null);
        prevBtn.setDisabledIcon(null);
        newBtn.putClientProperty("team", prevBtn.getClientProperty("team"));
        prevBtn.putClientProperty("team", null);

        // Store the background color of the new button
        lastOriginalColor = newBtn.getBackground();
    }

    // ---------------------------------------------------------------------------------------------------

    // Flips the chessboard based on the current state (flipped or not)
    // Ow Ka Sheng
    public void flipBoardView(Piece[][] board) {
        int rows = ChessBoard.ROWS;
        int cols = ChessBoard.COLUMNS;

        Dimension newSize = layeredPane.getSize();
        // Resize the button icons based on the new board size
        for (int row = 0; row < chessPieces.length; row++) {
            for (int col = 0; col < chessPieces[row].length; col++) {
                resizeButtonIcon(board[row][col], chessPieces[row][col], newSize);
            }
        }

        // Set flip direction based on current state
        int startRow = isFlipped ? 0 : rows - 1;
        int rowIncrement = isFlipped ? 1 : -1;
        int startCol = isFlipped ? 0 : cols - 1;
        int colIncrement = isFlipped ? 1 : -1;

        // Reposition pieces on the board after flipping
        for (int i = startRow; i >= 0 && i < rows; i += rowIncrement) {
            for (int j = startCol; j >= 0 && j < cols; j += colIncrement) {
                JButton piece = chessPieces[i][j];
                chessPanel.add(piece);
            }
        }

        isFlipped = !isFlipped; // Toggle the flipped state
    }

    // ---------------------------------------------------------------------------------------------------

    // Updates the turn indicator text and color based on the current team
    // Ow Ka Sheng
    public void updateTurnIndicator(String team) {
        turnIndicator.setText(team + "'s Turn");
        // Set color based on team (Blue or Red)
        if (team.equals("Blue")) {
            turnIndicator.setForeground(new Color(53, 89, 131, 255)); // Dark blue
        } else {
            turnIndicator.setForeground(new Color(222, 0, 0, 255)); // Dark red
        }
        repaint();
        revalidate();
    }

    // ---------------------------------------------------------------------------------------------------

    // Clears the list of moves in the UI
    // Harold Goh
    public void clearMoves() {
        movesContent.removeAll(); // Remove all move entries
        movesContent.revalidate(); // Refresh the moves content
        movesContent.repaint(); // Repaint the moves content
        moves.revalidate(); // Refresh moves panel
        moves.repaint(); // Repaint moves panel
    }

    // ---------------------------------------------------------------------------------------------------

    // Swaps the Tor and Xor pieces' icons
    // Ow Ka Sheng
    private void swapTorXorIcon(ArrayList<Piece> pieces) {
        // Iterate through each tor/xor in the list
        for (Piece piece : pieces) {
            Position piecePos = piece.getPosition(); // Get the position of the piece
            int row = piecePos.getRow(); // Row position of the piece
            int col = piecePos.getColumn(); // Column position of the piece
            JButton cell = chessPieces[row][col]; // Get the button representing the piece's cell

            setIcon(cell, piece.getIconPath(), 120, 80); // Update the icon
        }
    }

    // ---------------------------------------------------------------------------------------------------

    // Switches the view of Tor and Xor pieces for both Red and Blue teams
    // Ow Ka Sheng
    public void switchTorXorView(Map<String, ArrayList<Piece>> xorTorPieces) {
        ArrayList<Piece> redPositions = xorTorPieces.get("Red");
        ArrayList<Piece> bluePositions = xorTorPieces.get("Blue");

        swapTorXorIcon(redPositions); // Update Red team pieces
        swapTorXorIcon(bluePositions); // Update Blue team pieces
    }

    // ---------------------------------------------------------------------------------------------------

    // show winner panel based on team
    // Ow Ka Sheng
    public void displayWinner(String winner) {

        JDialog winnerMessage = new JDialog(this, "WINNER", true);
        winnerMessage.setSize(600, 450);
        winnerMessage.setLocationRelativeTo(this);
        winnerMessage.setResizable(false);
        winnerMessage.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Colors
        Color backgroundColor = new Color(240, 240, 240); // Light neutral background
        Color textColor = new Color(50, 50, 50); // Dark neutral text
        Color vsColor = new Color(100, 100, 100); // Medium neutral gray for "VS"
        // Dynamically set colors based on the winner
        Color winnerPanelColor = winner.equals("Blue") ? new Color(200, 230, 255) : new Color(255, 220, 220);
        Color opponentPanelColor = winner.equals("Blue") ? new Color(255, 220, 220) : new Color(200, 230, 255);
        Color borderShadowColor = new Color(200, 200, 200); // Shadow color for panels

        // Winner and Opponent Information
        String opponentName = winner.equals("Blue") ? "RED" : "BLUE";
        String winnerAvatar = winner.equals("Blue") ? "src/Blue_Biz.png" : "src/Red_Biz.png";
        String opponentAvatar = winner.equals("Blue") ? "src/Red_Biz.png" : "src/Blue_Biz.png";

        // Header Label
        JLabel headerLabel = new JLabel("YOU BEAT " + opponentName + "!");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setForeground(textColor);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Winner Panel
        JPanel winnerPanel = createWinnerPanel(winnerPanelColor, borderShadowColor);
        JLabel winnerAvatarLabel = new JLabel(new ImageIcon(
                new ImageIcon(winnerAvatar).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        JLabel winnerCrown = new JLabel(new ImageIcon(
                new ImageIcon(getClass().getResource("src/crown.png")).getImage().getScaledInstance(80, 50,
                        Image.SCALE_SMOOTH)));
        winnerCrown.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel winnerLabel = new JLabel(winner.toUpperCase(), SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        winnerPanel.add(winnerCrown, BorderLayout.NORTH);
        winnerPanel.add(winnerAvatarLabel, BorderLayout.CENTER);
        winnerPanel.add(winnerLabel, BorderLayout.SOUTH);

        // Opponent Panel
        JPanel opponentPanel = createWinnerPanel(opponentPanelColor, borderShadowColor);
        JLabel opponentAvatarLabel = new JLabel(new ImageIcon(
                new ImageIcon(opponentAvatar).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        JLabel opponentLabel = new JLabel(opponentName, SwingConstants.CENTER);
        opponentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        opponentPanel.add(opponentAvatarLabel, BorderLayout.CENTER);
        opponentPanel.add(opponentLabel, BorderLayout.SOUTH);

        // VS Label
        JLabel vsLabel = new JLabel("VS", SwingConstants.CENTER);
        vsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        vsLabel.setForeground(vsColor);
        vsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Layout: Winner vs Opponent
        JPanel layoutPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        layoutPanel.setBackground(backgroundColor);
        layoutPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        layoutPanel.add(winnerPanel);
        layoutPanel.add(vsLabel);
        layoutPanel.add(opponentPanel);

        // Combine everything
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(layoutPanel, BorderLayout.CENTER);

        winnerMessage.add(mainPanel);
        winnerMessage.setVisible(true);
    }

    // ---------------------------------------------------------------------------------------------------

    // Display restart success dialog
    // Ow Ka Sheng
    public void displayRestartSuccessfulDialog() {
        // Create and configure the dialog
        JOptionPane.showMessageDialog(
                null,
                "Game restarted successfully!",
                "Restart Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------------------------------------------------------------------------------------------------

    // Display save success dialog
    // Ow Ka Sheng
    public void displaySaveSuccessfulDialog() {
        // Create and configure the dialog
        JOptionPane.showMessageDialog(
                null,
                "Game saved successfully!",
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ---------------------------------------------------------------------------------------------------

    // Display load success/fail dialog
    // Ow Ka Sheng
    public void displayLoadDialog(String status) {
        // Create and configure the dialog
        JOptionPane.showMessageDialog(
                null,
                "Game loaded " + status + " !",
                "Load " + status,
                JOptionPane.INFORMATION_MESSAGE);
    }
}