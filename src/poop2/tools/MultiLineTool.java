package poop2.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Scanner;

import poop2.Editor;
import poop2.objects.GraphicObject;
import poop2.objects.LineItem;
import poop2.objects.MultiLineItem;

public class MultiLineTool extends Tool {

	public MultiLineTool() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onLeftMouseDoubleClicked( Point point ) {
		
		if(_leftMousePositions.size() >= this.getMinNumberOfPoints()) {
			
			_leftMousePositions.add( this.getLastPositionForMultiLine(point) );
			
			if(this.getLength() > 1.5) {
				this.createObject();
			}
			
			// cancel current operation
			this.onCancelled();
		}
		
	}
	
	protected int getMinNumberOfPoints() {
		return 1 ;
	}
	
	protected Point getLastPositionForMultiLine( Point clickPoint) {
		return clickPoint ;
	}
	
	public double getLength() {
		double length = 0;
		for(int i=1; i < _leftMousePositions.size(); i++) {
			length += _leftMousePositions.get(i).distance(_leftMousePositions.get(i - 1)) ;
		}
		return length ;
	}
	
	protected void createObject() {
		
		MultiLineItem item = new MultiLineItem( _leftMousePositions.toArray(new Point[_leftMousePositions.size()]) );
		Editor.addGraphicItem(item, true);
		
		System.out.println("created multiline");
	}

	@Override
	public void paint(Graphics g) {
		
		if(0 == _leftMousePositions.size())
			return ;
		
		if(! Editor.isPointInsideCanvas(Editor.getCurrentMousePosition()))
			return ;
		
		g.setColor(Editor.getCurrentColor());
		
		for(int i=1; i < _leftMousePositions.size(); i++) {
			drawLine(g, _leftMousePositions.get(i - 1), _leftMousePositions.get(i));
		}
		
		drawLine(g, _leftMousePositions.get( _leftMousePositions.size() - 1 ),
				Editor.convertToCanvasPoint( Editor.getCurrentMousePosition() ));
		
	}
	
	
	@Override
	public String getStatusBarText() {
		// length of last segment per axes
		if(_leftMousePositions.size() > 0) {
			Point p1 = Editor.getMousePositionInCanvas();
			Point p2 = _leftMousePositions.get(_leftMousePositions.size() - 1);
			return "length ( " + Math.abs(p1.x - p2.x) + ", " + Math.abs(p1.y - p2.y) + ")" ;
		}
		return "" ;
	}

	
	@Override
	public boolean canLoadObjectFromStream(String objectName) {
		return MultiLineItem.class.getSimpleName().equals( objectName ) ;
	}

	@Override
	public GraphicObject loadObject(Scanner stream) {
		MultiLineItem obj = new MultiLineItem();
		obj.loadFromStream(stream);
		return obj;
	}
	
}
