package Model;

public class People extends Utility {

	private static final String PEOPLE_IMAGE_FILE = "people.png";

	private static final int MIN_SPEED_PEOPLE = 8;
	private static final int MAX_SPEED_PEOPLE = 8;
	private static final int WIDTH = 20;
	private static final int HEIGHT = 50;

	public People(Dimension2D gameBoardSize) {
		super(gameBoardSize, WIDTH, HEIGHT);
		setMinSpeed(MIN_SPEED_PEOPLE);
		setMaxSpeed(MAX_SPEED_PEOPLE);
		setRandomSpeed();
		setIconLocation(PEOPLE_IMAGE_FILE);
	}

}
