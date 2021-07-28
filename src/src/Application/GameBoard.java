package Application;

import java.util.ArrayList;
import java.util.List;

import Model.AudioPlayer;
import Model.Collision;
import Model.Dimension2D;
import Model.People;
import Model.Utility;
import Model.Virus;

public class GameBoard {

	private int numberOfEnermy = 5;

	private final List<Utility> utilities = new ArrayList<>();

	private final Player player;
	private Utility playerCharacter; 

	private AudioPlayer audioPlayer;

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
		createUtilities();
	}
	
	public GameBoard(Dimension2D size, Utility playerCharacter, int numberOfEnermy) {
		this.size = size;
		this.numberOfEnermy = numberOfEnermy; 
		this.playerCharacter = playerCharacter; 
		this.player = new Player(playerCharacter);
		this.player.setup();
		createUtilities();
	}

	
	private void createUtilities() {
		for (int i = 0; i < numberOfEnermy; i++) {
			this.utilities.add(new People(this.size));
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

	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}

	public void setAudioPlayer(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	/**
	 * Updates the position 
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


	public Utility moveUtilities() {
		Utility winner = null; 

		for (Utility utility : this.utilities) {
			utility.drive(size);
		}
		this.player.getCharacter().drive(size);
		
		// check collision between others utilities 
		for (Utility utility : utilities) {
			if (utility.isCrunched()) {
				continue;
			}
			for (Utility u : utilities) {
				if (u.isCrunched()) {
					continue;
				}
				
				Collision collision = new Collision(u, utility);
				
				if (!u.getClass().equals(utility.getClass()) && collision.isCrash()) {
					this.audioPlayer.playCrashSound();
					u.crunch();
					utility.crunch();
					
					if(isWinner()) {
						gameOutcome = GameOutcome.WON; 
					}
				}
			}
		}
		
		// check collision of player
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
	 * If all others are crunched, the player wins.
	 */
	private boolean isWinner() {
		for (Utility utility : getUtilities()) {
			if (!player.getCharacter().getClass().equals(utility.getClass()) &&!utility.isCrunched()) {
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
