package Control;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;

public class GameToolBar extends ToolBar {
	private final Button start;
	private final Button stop;
	private final Button chooseCharacter; 
	private final Button setEnermiesNumber; 
	
	private int whichCharacter = 1; 
	private int numberOfEnermies = 5; 

	public GameToolBar() {
		this.start = new Button("Start");
		this.stop = new Button("Stop");
		this.chooseCharacter = new Button("Choose Character"); 
		this.setEnermiesNumber = new Button("Set numbers of enermies"); 
		// the game is stopped initially
		updateToolBarStatus(false);
		getItems().addAll(this.start, new Separator(), this.stop, new Separator(), this.chooseCharacter, new Separator(), this.setEnermiesNumber );
	}

	/**
	 * Initializes the actions of the toolbar buttons.
	 */
	public void initializeActions(GameBoardUI gameBoardUI) {
		this.start.setOnAction(event -> gameBoardUI.startGame());
		
		this.chooseCharacter.setOnAction(event -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "which character do you want to choose?");
			alert.setTitle("Choose the character");
			alert.setHeaderText("");
			
			ButtonType people = new ButtonType("People");
			ButtonType virus = new ButtonType("Virus");
			
			alert.getButtonTypes().setAll(people, virus); 
			
			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent()) {
				if(result.get() == people) {
					whichCharacter = 1; 
				} else {
					whichCharacter = 0; 
				}
				gameBoardUI.setup();
			}
		});
		
		this.setEnermiesNumber.setOnAction(event -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Type the numbers of enermies");
			dialog.setHeaderText("");
			dialog.setContentText("Please enter the numbers of enermies:");
			
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				int returned; 
				try {
					returned = Integer.parseInt(result.get());
					if (returned <= 0) throw new NumberFormatException(); 
				} catch (Exception e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("");
					alert.setContentText("Ooops, not a valid amount!");
					alert.showAndWait();
					returned = numberOfEnermies; 
				}
				
				numberOfEnermies = returned; 
				gameBoardUI.setup();
			}
		});

		this.stop.setOnAction(event -> {
			// stop the game while the alert is shown
			gameBoardUI.stopGame();

			Alert alert = new Alert(AlertType.CONFIRMATION, "Do you really want to stop the game?", ButtonType.YES,
					ButtonType.NO);
			alert.setTitle("Stop Game Confirmation");
			alert.setHeaderText("");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.YES) {
				// reset the game board to prepare the new game
				gameBoardUI.setup();
			} else {
				// continue running
				gameBoardUI.startGame();
			}
		});
	}

	/**
	 * Updates the status of the toolbar. This will for example enable or disable
	 * buttons.
	 *
	 * @param running true if game is running, false otherwise
	 */
	public void updateToolBarStatus(boolean running) {
		this.start.setDisable(running);
		this.chooseCharacter.setDisable(running);
		this.stop.setDisable(!running);
	}

	public int getWhichCar() {
		return whichCharacter;
	}

	public int getNumberOfEnermies() {
		return numberOfEnermies;
	}

	public void setNumberOfEnermies(int numberOfEnermies) {
		this.numberOfEnermies = numberOfEnermies;
	}
}
