package poop2.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.StringWriter;
import java.security.KeyStore.LoadStoreParameter;
import java.sql.Savepoint;
import java.util.Scanner;
import java.awt.Point;

import javax.xml.bind.annotation.XmlElement;

public class LineItem extends GraphicObject {

	Point	_firstPoint ;
	Point	_secondPoint ;
	
	
	public LineItem() {
		super();
	}
	
	public LineItem(Point firstPoint, Point secondPoint, Color color) {
		super(new Point(), 0, color);
		
		_firstPoint = firstPoint ;
		_secondPoint = secondPoint ;
	}
	
	@Override
	public void paint(Graphics g) {
		
		g.drawLine( (int) _firstPoint.getX(), (int) _firstPoint.getY(), (int) _secondPoint.getX(), (int) _secondPoint.getY());
		
	}

	@Override
	public Rectangle getBoundsRect() {
		
		return getBoundsRectFromPoints(new Point[] {_firstPoint, _secondPoint});
		
	}

	
	@Override
	public void saveToStream(StringWriter stream) {
		
		super.saveToStream(stream);
		
		writePointToStream(stream, _firstPoint);
		writePointToStream(stream, _secondPoint);
	}

	@Override
	public void loadFromStream(Scanner stream) {
		
		super.loadFromStream(stream);
		
		_firstPoint = readPointFromStream(stream);
		_secondPoint = readPointFromStream(stream);
	}

	
	
}
