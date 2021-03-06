package Control;

import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Application.GameBoard;
import Application.GameOutcome;
import Model.AudioPlayer;
import Model.Dimension2D;
import Model.People;
import Model.Point2D;
import Model.Utility;
import Model.Virus;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameBoardUI extends Canvas {

	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final int UPDATE_PERIOD = 1000 / 25;
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 300;
	private static final Dimension2D DEFAULT_SIZE = new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	public static Dimension2D getPreferredSize() {
		return DEFAULT_SIZE;
	}

	/**
	 * Timer responsible for updating the game every frame that runs in a separate
	 * thread.
	 */
	private Timer gameTimer;

	private GameBoard gameBoard;
	private Utility playerCharacter; 

	private final GameToolBar gameToolBar;

	private MouseSteering mouseSteering;

	private HashMap<String, Image> imageCache;

	public GameBoardUI(GameToolBar gameToolBar) {
		this.gameToolBar = gameToolBar;
		setup();
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public MouseSteering getMouseSteering() {
		return mouseSteering;
	}

	/**
	 * Removes all existing cars from the game board and re-adds them. Player car is
	 * reset to default starting position. Renders graphics.
	 */
	public void setup() {
		setupGameBoard();
		setupImageCache();
		this.gameToolBar.updateToolBarStatus(false);
		paint();
	}

	private void setupGameBoard() {
		Dimension2D size = getPreferredSize();
		this.gameBoard = new GameBoard(size);
		
		////////////
		switch (this.gameToolBar.getWhichCar()) {
		case 0: 
			this.playerCharacter = new Virus(size);
			break;
		default: 
			this.playerCharacter = new People(size);
		}
		///////
		
		this.gameBoard = new GameBoard(size, playerCharacter, this.gameToolBar.getNumberOfEnermies());
		
		this.gameBoard.setAudioPlayer(new AudioPlayer());
		widthProperty().set(size.getWidth());
		heightProperty().set(size.getHeight());
		this.mouseSteering = new MouseSteering(this, this.gameBoard.getPlayerCharacter());
	}

	private void setupImageCache() {
		this.imageCache = new HashMap<>();
		for (Utility utility : this.gameBoard.getUtilities()) {
			String imageLocation = utility.getIconLocation();
			this.imageCache.computeIfAbsent(imageLocation, this::getImage);
		}
		String playerImageLocation = this.gameBoard.getPlayerCharacter().getIconLocation();
		this.imageCache.put(playerImageLocation, getImage(playerImageLocation));
	}

	/**
	 * Sets the car's image.
	 *
	 * @param carImageFilePath an image file path that needs to be available in the
	 *                         resources folder of the project
	 */
	private Image getImage(String carImageFilePath) {
		URL carImageUrl = getClass().getClassLoader().getResource(carImageFilePath);
		if (carImageUrl == null) {
			throw new IllegalArgumentException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
		}
		return new Image(carImageUrl.toExternalForm());
	}

	/**
	 * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
	 * which causes the cars to change their positions (i.e. move). Renders graphics
	 * and updates tool bar status.
	 */
	public void startGame() {
		if (!this.gameBoard.isRunning()) {
			this.gameBoard.startGame();
			this.gameToolBar.updateToolBarStatus(true);
			startTimer();
			paint();
		}
	}

	private void startTimer() {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				updateGame();
			}
		};
		if (this.gameTimer != null) {
			this.gameTimer.cancel();
		}
		this.gameTimer = new Timer();
		this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
	}

	private void updateGame() { 
		if (gameBoard.isRunning()) {
			// updates car positions and re-renders graphics
			Utility winner = this.gameBoard.update();
			// when this.gameBoard.getOutcome() is OPEN, do nothing
			if (this.gameBoard.getGameOutcome() == GameOutcome.LOST) {				

				showAsyncAlert("You were killed by " + winner.getClass().getSimpleName() + ". Try next time!");
				this.stopGame();
				
			} else if (this.gameBoard.getGameOutcome() == GameOutcome.WON) {
				showAsyncAlert("Congratulations! You won!!");
				this.stopGame();
			}
			paint();
		}
	}

	/**
	 * Stops the game board and set the tool bar to default values.
	 */
	public void stopGame() {
		if (this.gameBoard.isRunning()) {
			this.gameBoard.stopGame();
			this.gameToolBar.updateToolBarStatus(false);
			this.gameTimer.cancel();
		}
	}

	/**
	 * Render the graphics of the whole game by iterating through the cars of the
	 * game board at render each of them individually.
	 */
	private void paint() {
		getGraphicsContext2D().setFill(BACKGROUND_COLOR);
		getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());

		for (Utility utility : this.gameBoard.getUtilities()) {
			paintCar(utility);
		}
		// render player car
		paintCar(this.gameBoard.getPlayerCharacter());
	}

	/**
	 * Show image of a car at the current position of the car.
	 *
	 * @param car to be drawn
	 */
	private void paintCar(Utility utility) {
		Point2D carPosition = utility.getPosition();

		getGraphicsContext2D().drawImage(this.imageCache.get(utility.getIconLocation()), carPosition.getX(),
				carPosition.getY(), utility.getSize().getWidth(), utility.getSize().getHeight());
	}

	/**
	 * Method used to display alerts in moveCars().
	 *
	 * @param message you want to display as a String
	 */
	private void showAsyncAlert(String message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(message);
			alert.showAndWait();
			this.setup();
		});
	}
}
