package poop2.tools;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

import poop2.objects.GraphicObject;

public abstract class Tool {

	
	protected	ArrayList<Point>	_leftMousePositions = new ArrayList<>();
	protected	Point	_lastPressPos = new Point();
	protected	boolean	_isDraggingMouse = false ;
	
	
	public void OnLeftMouseClicked( Point point ) {
		
		_leftMousePositions.add(point);
		
	}
	
	public void onLeftMouseDoubleClicked( Point point ) {
		
		
	}
	
	public void onLeftMousePressed( Point point ) {
		
		_lastPressPos = new Point(point) ;
		_isDraggingMouse = true ;
		
	}
	
	public void onLeftMouseReleased( Point point ) {
		
		_isDraggingMouse = false ;
		
	}
	
	public void OnMouseMove( Point point ) {
		
		
	}
	
	public void onMouseDragged( Point point ) {
		
		
	}
	
	public void OnSelected() {
		
		_isDraggingMouse = false ;
		
	}
	
	public void onCancelled() {
		_leftMousePositions.clear();
		_isDraggingMouse = false;
	}
	
	public void paint( Graphics g ) {
		
		
	}
	
	public static void drawLine( Graphics g, Point p1, Point p2 ) {
		
		g.drawLine( (int)p1.getX(), (int)p1.getY(), (int)p2.getX(), (int)p2.getY());
		
	}
	
	public String getStatusBarText() {
		return "" ;
	}
	
	public	boolean	canLoadObjectFromStream( String objectName ) {
		return false ;
	}
	
	public	GraphicObject	loadObject(Scanner stream) {
		return null;
	}

	
	
}
