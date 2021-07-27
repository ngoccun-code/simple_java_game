package Application;

import java.util.ArrayList;
import java.util.List;

import Audio.AudioPlayerInterface;
import Model.Collision;
import Model.Dimension2D;
import Model.People;
import Model.Utility;
import Model.Virus;

public class GameBoard {

	private static final int NUMBER_OF_PEOPLE = 10;
	private static final int NUMBER_OF_TESLA_VIRUS = 3;

	private final List<Utility> utilities = new ArrayList<>();

	private final Player player;
	private Utility playerCharacter; 

	private AudioPlayerInterface audioPlayer;

	private final Dimension2D size;

	private boolean running;

	private final List<Utility> loserUtilities = new ArrayList<>();

	private GameOutcome gameOutcome = GameOutcome.OPEN;

	/**
	 * Creates the game board based on the given size.
	 */
	public GameBoard(Dimension2D size) {
		this.size = size;
		playerCharacter = new People(size);
		this.player = new Player(playerCharacter);
		this.player.setup();
		createCars();
	}
	
	public GameBoard(Dimension2D size, Utility playerCharacter) {
		this.size = size;
		this.playerCharacter = playerCharacter; 
		this.player = new Player(playerCharacter);
		this.player.setup();
		createCars();
	}

	/**
	 * Creates as many cars as specified by {@link #NUMBER_OF_SLOW_CARS} and adds
	 * them to the cars list.
	 */
	private void createCars() {
		for (int i = 0; i < NUMBER_OF_PEOPLE; i++) {
			this.utilities.add(new People(this.size));
		}
		for (int i = 0; i < NUMBER_OF_TESLA_VIRUS; i++) {
			this.utilities.add(new Virus(this.size));
		}		
	}

	public Dimension2D getSize() {
		return size;
	}

	
	public boolean isRunning() {
		return this.running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public GameOutcome getGameOutcome() {
		return gameOutcome;
	}

	public List<Utility> getUtilities() {
		return this.utilities;
	}

	public Utility getPlayerCharacter() {
		return this.player.getCharacter();
	}

	public AudioPlayerInterface getAudioPlayer() {
		return this.audioPlayer;
	}

	public void setAudioPlayer(AudioPlayerInterface audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * Updates the position of each car.
	 */
	public Utility update() {
		return moveUtilities();
	}

	
	public void startGame() {
		playMusic();
		this.running = true;
	}

	public void stopGame() {
		stopMusic();
		this.running = false;
	}

	public void playMusic() {
		this.audioPlayer.playBackgroundMusic();
	}

	public void stopMusic() {
		this.audioPlayer.stopBackgroundMusic();
	}

	public List<Utility> getLoserUtilities() {
		return this.loserUtilities;
	}

	/**
	 * Moves all cars on this game board one step further.
	 */
	public Utility moveUtilities() {
		Utility winner = null; 
		
		// update the positions of the player car and the autonomous cars
		for (Utility utility : this.utilities) {
			utility.drive(size);
		}
		this.player.getCharacter().drive(size);

		// iterate through all cars (except player car) and check if it is crunched
		for (Utility utility : utilities) {
			if (utility.isCrunched()) {
				//no need to check for a collision
				continue;
			}

			Collision collision = new Collision(player.getCharacter(), utility);

			if (!player.getCharacter().getClass().equals(utility.getClass()) && collision.isCrash()) {
				winner = collision.evaluate();
				Utility loser = collision.evaluateLoser();
				printWinner(winner);
				loserUtilities.add(loser);

				this.audioPlayer.playCrashSound();

				//The loser car is crunched and stops driving
				loser.crunch();
				
				// The player gets notified when he looses or wins the game
				if(isWinner()) {
					gameOutcome = GameOutcome.WON; 
				} else if (loser.equals(player.getCharacter())) {
					gameOutcome = GameOutcome.LOST; 
				}
			}
		}
		return winner; 
	}

	/**
	 * If all other cars are crunched, the player wins.
	 *
	 * @return true if the game is over and the player won, false otherwise
	 */
	private boolean isWinner() {
		for (Utility utility : getUtilities()) {
			if (!utility.getClass().equals(player.getCharacter().getClass()) && !utility.isCrunched()) {
				return false;
			}
		}
		return true;
	}

	private void printWinner(Utility winner) {
		
		if (winner == this.player.getCharacter()) {
			System.out.println("The player won the collision!");
		
		} else if (winner != null) {
			System.out.println(winner.getClass().getSimpleName() + " won the collision!");
			
		} else {
			System.err.println("Winner character was null!");
		}
		
	}
}
