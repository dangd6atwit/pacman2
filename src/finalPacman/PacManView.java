/**
 * @author Alex Wilson, Danny Dang and Robert Briggs
 * Encorporates the various Views of the application that reference different parts of the Model, including the main
 * game board, the score label, the level label, and the Game Over label.
 */

package finalPacman;

import javafx.fxml.FXML;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import finalPacman.PacManModel.CellValue;

public class PacManView extends Group {
    public final static double CELL_WIDTH = 20.0;

    @FXML private int rowCount;
    @FXML private int columnCount;
    private ImageView[][] cellViews;
    private Image pacmanRight;
    private Image pacmanUp;
    private Image pacmanDown;
    private Image pacmanLeft;
    private Image ghost1;
    private Image ghost2;
    private Image blueGhost;
    private Image wall;
    private Image powerPellet;
    private Image pacDot;

    /**
     * Initializes the values of the image instance variables from files
     */
    public PacManView() {
        this.pacmanRight = new Image(getClass().getResourceAsStream("/res/pacmanRight.gif"));
        this.pacmanUp = new Image(getClass().getResourceAsStream("/res/pacmanUp.gif"));
        this.pacmanDown = new Image(getClass().getResourceAsStream("/res/pacmanDown.gif"));
        this.pacmanLeft = new Image(getClass().getResourceAsStream("/res/pacmanLeft.gif"));
        this.ghost1 = new Image(getClass().getResourceAsStream("/res/redghost.gif"));
        this.ghost2 = new Image(getClass().getResourceAsStream("/res/ghost2.gif"));
        this.blueGhost = new Image(getClass().getResourceAsStream("/res/blueghost.gif"));
        this.wall = new Image(getClass().getResourceAsStream("/res/wall.png"));
        this.powerPellet = new Image(getClass().getResourceAsStream("/res/whitedot.png"));
        this.pacDot = new Image(getClass().getResourceAsStream("/res/smalldot.png"));
    }

    /**
     * Constructs an empty grid of ImageViews
     */
    private void initializeGrid() {
        if (this.rowCount > 0 && this.columnCount > 0) {
            this.cellViews = new ImageView[this.rowCount][this.columnCount];
            for (int row = 0; row < this.rowCount; row++) {
                for (int column = 0; column < this.columnCount; column++) {
                    ImageView imageView = new ImageView();
                    imageView.setX((double)column * CELL_WIDTH);
                    imageView.setY((double)row * CELL_WIDTH);
                    imageView.setFitWidth(CELL_WIDTH);
                    imageView.setFitHeight(CELL_WIDTH);
                    this.cellViews[row][column] = imageView;
                    this.getChildren().add(imageView);
                }
            }
        }
    }

    /** Updates the view to reflect the state of the model
     *
     * @param model
     */
    public void update(PacManModel model) {
        assert model.getRowCount() == this.rowCount && model.getColumnCount() == this.columnCount;
        //for each ImageView, set the image to correspond with the CellValue of that cell
        for (int row = 0; row < this.rowCount; row++){
            for (int column = 0; column < this.columnCount; column++){
                CellValue value = model.getCellValue(row, column);
                if (value == CellValue.WALL) {
                    this.cellViews[row][column].setImage(this.wall);
                }
                else if (value == CellValue.POWERPELLET) {
                    this.cellViews[row][column].setImage(this.powerPellet);
                }
                else if (value == CellValue.PACDOT) {
                    this.cellViews[row][column].setImage(this.pacDot);
                }
                else {
                    this.cellViews[row][column].setImage(null);
                }
                //check which direction PacMan is going in and display the corresponding image
                if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && (PacManModel.getLastMovement() == PacManModel.Movement.RIGHT || PacManModel.getLastMovement() == PacManModel.Movement.STOP)) {
                    this.cellViews[row][column].setImage(this.pacmanRight);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastMovement() == PacManModel.Movement.LEFT) {
                    this.cellViews[row][column].setImage(this.pacmanLeft);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastMovement() == PacManModel.Movement.UP) {
                    this.cellViews[row][column].setImage(this.pacmanUp);
                }
                else if (row == model.getPacmanLocation().getX() && column == model.getPacmanLocation().getY() && PacManModel.getLastMovement() == PacManModel.Movement.DOWN) {
                    this.cellViews[row][column].setImage(this.pacmanDown);
                }
                //make ghosts "blink" towards the end of ghostEatingMode (display regular ghost images on alternating updates of the counter)
                if (PacManModel.isPowerPelletMode() && (Controller.getPowerPelletModeCounter() == 6 ||Controller.getPowerPelletModeCounter() == 4 || Controller.getPowerPelletModeCounter() == 2)) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2);
                    }
                }
                //display blue ghosts in ghostEatingMode
                else if (PacManModel.isPowerPelletMode()) {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhost);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.blueGhost);
                    }
                }
                //dispaly regular ghost images otherwise
                else {
                    if (row == model.getGhost1Location().getX() && column == model.getGhost1Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost1);
                    }
                    if (row == model.getGhost2Location().getX() && column == model.getGhost2Location().getY()) {
                        this.cellViews[row][column].setImage(this.ghost2);
                    }
                }
            }
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.initializeGrid();
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
        this.initializeGrid();
    }
}
