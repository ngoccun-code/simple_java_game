package Application;

import Model.Utility;

public class Player {

	private static final double START_X_COORDINATE = 0.0;
	private static final double START_Y_COORDINATE = 0.0;
	private static final int START_DIRECTION = 90;

	private Utility character;

	
	public Player(Utility character) {
		this.character = character;
	}

	public void setCharacter(Utility character) {
		this.character = character;
	}

	public Utility getCharacter() {
		return this.character;
	}

	/**
	 * Prepares player's car for the start of the game.
	 */
	public void setup() {
		// The player always starts in the upper left corner facing to the right
		character.setPosition(START_X_COORDINATE, START_Y_COORDINATE);
		character.setDirection(START_DIRECTION);
	}
}
