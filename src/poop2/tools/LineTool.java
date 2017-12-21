package poop2.tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Scanner;

import poop2.Editor;
import poop2.objects.GraphicObject;
import poop2.objects.LineItem;
import poop2.objects.RectangleItem;

public class LineTool extends Tool {


	@Override
	public void onLeftMouseReleased( Point point ) {
		
		if(_isDraggingMouse) {
			// create line
			
			Point firstPoint = _lastPressPos ;
			Point secondPoint = point ;
			
			if(firstPoint.distance(secondPoint) > 0) {
				
				LineItem item = new LineItem(firstPoint, secondPoint, Editor.getCurrentColor());
				Editor.addGraphicItem(item, true);
				
				System.out.println("created line");
			}
			
			// cancel current operation
			this.onCancelled();
		}
		
	}

	@Override
	public void paint(Graphics g) {
		
		if(!_isDraggingMouse)
			return ;
		
		if(! Editor.isPointInsideCanvas(Editor.getCurrentMousePosition()))
			return ;
		
		Graphics2D g2d = (Graphics2D) g ;
		
		g2d.setColor(Editor.getCurrentColor());
		
		Point firstPoint = _lastPressPos ;
		Point secondPoint = Editor.convertToCanvasPoint( Editor.getCurrentMousePosition() );
		
		g2d.drawLine( (int)firstPoint.getX(), (int)firstPoint.getY(), (int)secondPoint.getX(), (int)secondPoint.getY());
		
	}
	
	
	@Override
	public String getStatusBarText() {
		// length of line per axes
		if(_isDraggingMouse) {
			Point p = Editor.getMousePositionInCanvas();
			return "length ( " + Math.abs(p.x - _lastPressPos.x) + ", " + Math.abs(p.y - _lastPressPos.y) + ")" ;
		}
		return "" ;
	}

	
	@Override
	public boolean canLoadObjectFromStream(String objectName) {
		return LineItem.class.getSimpleName().equals( objectName ) ;
	}

	@Override
	public GraphicObject loadObject(Scanner stream) {
		LineItem obj = new LineItem();
		obj.loadFromStream(stream);
		return obj;
	}
	
}
