package Model;

import java.util.concurrent.ThreadLocalRandom;

public class Utility {
	
	protected static final int MAX_ANGLE = 360;
	protected static final int HALF_ANGLE = MAX_ANGLE / 2;

	private int minSpeed;
	private int maxSpeed;
	private int speed;
	private boolean crunched;
	private Point2D position;
	private int direction; //degree within a circle, [0, 360) 
	private String iconLocation;
	private Dimension2D size;
	
	/* 
	 * Constructor 
	 */
	protected Utility(Dimension2D gameBoardSize, int width, int height) {
		setSize(new Dimension2D(width, height)); 
		setRandomPosition(gameBoardSize);
		setRandomDirection();
	}
	
	protected void setRandomPosition(Dimension2D gameBoardSize) {
		double x = calculateRandomDouble(0, gameBoardSize.getWidth() - size.getWidth());
		double y = calculateRandomDouble(0, gameBoardSize.getHeight() - size.getHeight());
		this.position = new Point2D(x, y);
	}

	protected void setRandomDirection() {
		this.direction = calculateRandomInt(0, MAX_ANGLE);
	}
	
	protected void setRandomSpeed() {
		this.speed = calculateRandomInt(this.minSpeed, this.maxSpeed + 1);
	}
	
	
	/* 
	 * calculate Random  
	 */
	protected static int calculateRandomInt(int minValue, int maxValue) {
		return ThreadLocalRandom.current().nextInt(minValue, maxValue);
	}
	protected static double calculateRandomDouble(double minValue, double maxValue) {
		return ThreadLocalRandom.current().nextDouble(minValue, maxValue);
	}

	
	/**
	 * Drives the car and updates its position and possibly its direction.
	 * <p>
	 * The X and Y coordinates of the new position are based on the current
	 * position, direction and speed.
	 */
	public void drive(Dimension2D gameBoardSize) {
		if (this.crunched) {
			return;
		}
		double maxX = gameBoardSize.getWidth();
		double maxY = gameBoardSize.getHeight();
		// calculate delta between old coordinates and new ones based on speed and direction
		double deltaX = this.speed * Math.sin(Math.toRadians(this.direction));
		double deltaY = this.speed * Math.cos(Math.toRadians(this.direction));
		double newX = this.position.getX() + deltaX;
		double newY = this.position.getY() + deltaY;

		// calculate position in case the boarder of the game board has been reached
		if (newX < 0) {
			newX = -newX;
			this.direction = MAX_ANGLE - this.direction;
		} else if (newX + this.size.getWidth() > maxX) {
			newX = 2 * maxX - newX - 2 * this.size.getWidth();
			this.direction = MAX_ANGLE - this.direction;
		}

		if (newY < 0) {
			newY = -newY;
			this.direction = HALF_ANGLE - this.direction;
			if (this.direction < 0) {
				this.direction = MAX_ANGLE + this.direction;
			}
		} else if (newY + this.size.getHeight() > maxY) {
			newY = 2 * maxY - newY - 2 * this.size.getHeight();
			this.direction = HALF_ANGLE - this.direction;
			if (this.direction < 0) {
				this.direction = MAX_ANGLE + this.direction;
			}
		}
		// set coordinates
		this.position = new Point2D(newX, newY);
	}
	
	
	/*
	 * Getter and setters
	 */
	public int getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}

	public int getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public void incrementSpeed() {
		if (this.speed < this.maxSpeed) {
			this.speed++;
		}
	}

	public void decrementSpeed() {
		if (this.speed > this.minSpeed) {
			this.speed--;
		}
	}

	public boolean isCrunched() {
		return crunched;
	}

	public void crunch() {
		this.crunched = true;
		this.speed = 0;			
	}

	public Point2D getPosition() {
		return position;
	}

	public void setPosition(double x, double y) {
		this.position = new Point2D(x, y);
	
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		if (direction < 0 || direction >= MAX_ANGLE) {
			throw new IllegalArgumentException("Direction must be between 0 (inclusive) and 360 (exclusive)");
		}
		this.direction = direction;
	}

	public String getIconLocation() {
		return iconLocation;
	}

	public void setIconLocation(String iconLocation) {
		if (iconLocation == null) {
			throw new NullPointerException("The chassis image of a car cannot be null.");
		}
		this.iconLocation = iconLocation;
	}

	public Dimension2D getSize() {
		return size;
	}

	public void setSize(Dimension2D size) {
		this.size = size;
	}

}
