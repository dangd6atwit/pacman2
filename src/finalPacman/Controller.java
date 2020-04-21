/**
 * @author Alex Wilson, Danny Dang and Robert Briggs
 * The Controller handles user input and coordinates the updating of the model and the view with the help of a timer.
 */

package finalPacman;

import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements EventHandler<KeyEvent> {
    final private static double FRAMES_PER_SECOND = 5.0;

    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Label gameOverLabel;
    @FXML private PacManView pacManView;
    private PacManModel pacManModel;
    private static final String[] levelFiles = {"src/levels/level1.txt", "src/levels/level2.txt", "src/levels/level3.txt"};

    private Timer timer;
    private static int powerPelletModeCounter;
    private boolean paused;

    public Controller() {
        this.paused = false;
    }

    /**
     * Initialize and update the model and view from the first txt file and starts the timer.
     */
    public void initialize() {
        String file = this.getLevelFile(0);
        this.pacManModel = new PacManModel();
        this.update(PacManModel.Movement.STOP);
        powerPelletModeCounter = 25;
        this.startTimer();
    }

    /**
     * Schedules the model to update based on the timer.
     */
    private void startTimer() {
        this.timer = new java.util.Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        update(pacManModel.getCurrentMovement());
                    }
                });
            }
        };

        long frameTimeInMilliseconds = (long)(1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 0, frameTimeInMilliseconds);
    }

    /**
     * Steps the PacManModel, updates the view, updates score and level, displays Game Over/You Won, and instructions of how to play
     * @param direction the most recently inputted direction for PacMan to move in
     */
    private void update(PacManModel.Movement movement) {
        this.pacManModel.move(movement);
        this.pacManView.update(pacManModel);
        this.scoreLabel.setText(String.format("Score: %d", this.pacManModel.getScore()));
        this.levelLabel.setText(String.format("Level: %d", this.pacManModel.getLevel()));
        if (pacManModel.isGameOver()) {
            this.gameOverLabel.setText(String.format("GAME OVER"));
            pause();
        }
        if (pacManModel.isYouWon()) {
            this.gameOverLabel.setText(String.format("YOU WON!"));
        }
        //when PacMan is in ghostEatingMode, count down the ghostEatingModeCounter to reset ghostEatingMode to false when the counter is 0
        if (pacManModel.isPowerPelletMode()) {
            powerPelletModeCounter--;
        }
        if (powerPelletModeCounter == 0 && pacManModel.isPowerPelletMode()) {
            pacManModel.setPowerPelletMode(false);
        }
    }

    /**
     * Takes in user keyboard input to control the movement of PacMan and start new games
     * @param keyEvent user's key click
     */
    @Override
    public void handle(KeyEvent ke) {
        boolean keyRecognized = true;
        KeyCode keyCode = ke.getCode();
        PacManModel.Movement movement = PacManModel.Movement.STOP;
        if (keyCode == KeyCode.A) {
            movement = PacManModel.Movement.LEFT;
        } else if (keyCode == KeyCode.D) {
            movement = PacManModel.Movement.RIGHT;
        } else if (keyCode == KeyCode.W) {
            movement = PacManModel.Movement.UP;
        } else if (keyCode == KeyCode.S) {
            movement = PacManModel.Movement.DOWN;
        } else if (keyCode == KeyCode.P) {
            pause();
            this.pacManModel.startNewGame();
            this.gameOverLabel.setText(String.format(""));
            paused = false;
            this.startTimer();
        } else {
            keyRecognized = false;
        }
        if (keyRecognized) {
            ke.consume();
            pacManModel.setCurrentMovement(movement);
        }
    }

    /**
     * Pause the timer
     */
    public void pause() {
            this.timer.cancel();
            this.paused = true;
    }

    public double getBoardWidth() {
        return PacManView.CELL_WIDTH * this.pacManView.getColumnCount();
    }

    public double getBoardHeight() {
        return PacManView.CELL_WIDTH * this.pacManView.getRowCount();
    }

    public static void setPowerPelletModeCounter() {
        powerPelletModeCounter = 25;
    }

    public static int getPowerPelletModeCounter() {
        return powerPelletModeCounter;
    }

    public static String getLevelFile(int x)
    {
        return levelFiles[x];
    }

    public boolean getPaused() {
        return paused;
    }
}
