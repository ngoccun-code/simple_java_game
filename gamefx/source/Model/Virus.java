package Model;

public class Virus extends Utility {

	private static final String VIRUS_IMAGE_FILE = "virus.jpg";

	private static final int MIN_SPEED_VIRUS = 4;
	private static final int MAX_SPEED_VIRUS = 4;
	private static final int WIDTH = 25;
	private static final int HEIGHT = 25;

	public Virus(Dimension2D gameBoardSize) {
		super(gameBoardSize, WIDTH, HEIGHT);
		setMinSpeed(MIN_SPEED_VIRUS);
		setMaxSpeed(MAX_SPEED_VIRUS);
		setRandomSpeed();
		setIconLocation(VIRUS_IMAGE_FILE);
	}
	

}
