package poop2.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.StringWriter;
import java.util.Scanner;
import java.awt.Point;

import javax.xml.bind.annotation.XmlElement;

public class RectangleItem extends GraphicObject {

	int _width ;
	int _height ;
	
	
	public RectangleItem(Point point, int width, int height) {
		super();
		
		_position = point ;
		
		_width = width ;
		_height = height ;
	}


	@Override
	public void paint(Graphics g) {
		
	//	g.drawRect( (int) _position.getX(), (int) _position.getY(), _width, _height);
		g.drawRect( 0, 0, _width, _height);
		
	}


	@Override
	public Rectangle getBoundsRect() {
		// TODO Auto-generated method stub
		
		return enlargeRectForSelection( new Rectangle( 0, 0, _width, _height) );
		
	}


	@Override
	public void saveToStream(StringWriter stream) {
		
		super.saveToStream(stream);
		
		stream.write(_width + " " + _height + "\n");
	}


	@Override
	public void loadFromStream(Scanner stream) {
		
		super.loadFromStream(stream);
		
		_width = stream.nextInt();
		_height = stream.nextInt();
	}

	
	
	
}
