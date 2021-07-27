package Control;

import Model.Point2D;
import Model.Utility;
import javafx.scene.input.MouseEvent;

public class MouseSteering {
	
	private static final int ANGLE_90_DEGREES = 90;
	private static final int ANGLE_270_DEGREES = 270;

	private final Utility player;

	public MouseSteering(GameBoardUI gameBoardUI, Utility player) {
		this.player = player;
		gameBoardUI.addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
	}

	private void mousePressed(MouseEvent clickEvent) {
		Point2D carPosition = player.getPosition();
		Point2D clickPosition = new Point2D(clickEvent.getX(), clickEvent.getY());
		double deltaX = clickPosition.getX() - carPosition.getX();
		deltaX = Math.abs(deltaX);
		double deltaY = clickPosition.getY() - carPosition.getY();
		int degree = (int) Math.toDegrees(Math.atan2(deltaY, deltaX));

		if (clickPosition.getX() > carPosition.getX()) {
			degree = ANGLE_90_DEGREES - degree;	
			
		} else {
			degree = ANGLE_270_DEGREES + degree;
		}

		player.setDirection(degree);
	}

}
