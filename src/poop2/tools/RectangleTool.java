package poop2.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Scanner;
import java.awt.*;

import poop2.Editor;
import poop2.objects.GraphicObject;
import poop2.objects.RectangleItem;

public class RectangleTool extends Tool {

	
	@Override
	public void OnLeftMouseClicked( Point point ) {
		
		super.OnLeftMouseClicked(point);
		
		/*
		if(2 == _leftMousePositions.size()) {
			// create rectangle
			
			Point firstPoint = _leftMousePositions.get(0);
			Point secondPoint = _leftMousePositions.get(1);
			java.awt.Rectangle rect = this.getRectFormedByPoints(firstPoint, secondPoint);
			
			items.RectangleItem rectItem = new items.RectangleItem( new Point.Double(rect.x, rect.y), rect.width, rect.height, poop2.Editor.getCurrentColor() );
			poop2.Editor.addGraphicItem(rectItem);
			
			System.out.println("created rect - " + rect.toString());
			
			// cancel current operation
			this.onCancelled();
		}
		*/
		
	}

	@Override
	public void onLeftMouseReleased(Point point) {
		
		if(_isDraggingMouse) {
			// user was dragging mouse and now released it
			// create rectangle
			
			Point firstPoint = _lastPressPos ;
			Point secondPoint = point ;
			Rectangle rect = this.getRectFormedByPoints(firstPoint, secondPoint);
			
			if(rect.width > 0 && rect.height > 0) {
				poop2.objects.RectangleItem rectItem = new poop2.objects.RectangleItem( new Point(rect.x, rect.y), rect.width, rect.height );
				Editor.addGraphicItem(rectItem, true);
				
				System.out.println("created rect - " + rect.toString());
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
		
	//	Graphics2D g2d = (Graphics2D) g ;
		Graphics g2d = g ;
		
		g2d.setColor(Editor.getCurrentColor());
		
	//	g2d.translate(_point.getX(), _point.getY());
		
		Point firstPoint = _lastPressPos ;
		Point secondPoint = Editor.getMousePositionInCanvas();
		Rectangle rect = getRectFormedByPoints(firstPoint, secondPoint);
		
		g2d.drawRect( rect.x , rect.y, rect.width, rect.height);
		
	}
	
	static Rectangle getRectFormedByPoints(Point p1, Point p2) {
		
		int x = (int) Math.min(p1.getX(), p2.getX());
		int y = (int) Math.min(p1.getY(), p2.getY());
		int width = (int) Math.abs(p2.getX() - p1.getX());
		int height = (int) Math.abs(p2.getY() - p1.getY());
		
		return new Rectangle(x, y, width, height);
	}

	
	@Override
	public String getStatusBarText() {
		// current dimensions
		if(_isDraggingMouse) {
			Point p1 = Editor.getMousePositionInCanvas();
			Point p2 = _lastPressPos ;
			return "dimensions ( " + Math.abs(p1.x - p2.x) + ", " + Math.abs(p1.y - p2.y) + ")" ;
		}
		return "" ;
	}
	
	
	@Override
	public boolean canLoadObjectFromStream(String objectName) {
		String name = RectangleItem.class.getSimpleName();
		return name.equals( objectName ) ;
	}

	@Override
	public GraphicObject loadObject(Scanner stream) {
		RectangleItem obj = new RectangleItem(new Point(), 0, 0);
		obj.loadFromStream(stream);
		return obj;
	}
	
	
	
}
