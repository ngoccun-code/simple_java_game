package Model;

public class Collision {

	protected final Utility u1;
	protected final Utility u2;
	private final boolean crash;

	public Collision(Utility u1, Utility u2) {
		this.u1 = u1;
		this.u2 = u2;
		this.crash = detectCollision();
	}

	public boolean isCrash() {
		return crash;
	}

	protected Point2D p1;
	protected Dimension2D d1;
	protected Point2D p2;
	protected Dimension2D d2;
	protected boolean above;
	protected boolean below;
	protected boolean right;
	protected boolean left; 
	
	
	protected void checkPosition() {
		  p1 = u1.getPosition();
		  d1 = u1.getSize();
		  p2 = u2.getPosition();
		  d2 = u2.getSize();
		  above = p1.getY() + d1.getHeight() < p2.getY();
		  below = p1.getY() > p2.getY() + d2.getHeight();
		  right = p1.getX() + d1.getWidth() < p2.getX();
		  left = p1.getX() > p2.getX() + d2.getWidth();
	}
	
	
	public boolean detectCollision() {
		checkPosition(); 
		return !above && !below && !right && !left;
	}

	

	/**
	 * Evaluates winner of the collision.
	 */
	public Utility evaluate() {
		if (Virus.class.isInstance(u1) && !Virus.class.isInstance(u2))
			return u1; 
		
		return u2;
	}

	/**
	 * Evaluates loser of the collision.
	 */
	public Utility evaluateLoser() {
		Utility winner = evaluate();
		if (this.u1 == winner) {
			return this.u2;
		}
		return this.u1;
	}
}
