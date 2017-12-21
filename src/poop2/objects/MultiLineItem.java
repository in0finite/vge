package poop2.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.StringWriter;
import java.util.Scanner;
import java.awt.Point;

import poop2.tools.MultiLineTool;
import poop2.tools.Tool;

public class MultiLineItem extends GraphicObject {

	protected	Point[]	_points = new Point[0] ;
	
	
	public MultiLineItem() {
		super();
	}
	
	public MultiLineItem(Point[] points) {
		super();
		_points = points ;
	}

	
	@Override
	public void paint(Graphics g) {
		
		for(int i=1; i < _points.length; i++) {
			Tool.drawLine(g, _points[i - 1], _points[i]);
		}
		
	}

	@Override
	public Rectangle getBoundsRect() {
		
		return getBoundsRectFromPoints(_points);
		
	}

	
	@Override
	public void saveToStream(StringWriter stream) {
		
		super.saveToStream(stream);
		
		stream.write(_points.length + "\n");
		for(Point p : _points)
			writePointToStream(stream, p);
		
	}

	@Override
	public void loadFromStream(Scanner stream) {
		
		super.loadFromStream(stream);
		
		int count = stream.nextInt();
		_points = new Point[count];
		for(int i=0; i < _points.length; i++)
			_points[i] = readPointFromStream(stream);
		
	}
	
	
}
