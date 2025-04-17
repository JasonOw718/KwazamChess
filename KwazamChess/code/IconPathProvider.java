// Interface that defines methods to get the icon paths for pieces (Sau and Ram)
// Ow Ka Sheng
public interface IconPathProvider {

    // Method to get the icon path for the flipped version of a piece
    String getFlipIconPath();

    // Method to get the icon path for the initial version of a piece
    String getInitialIconPath();
}