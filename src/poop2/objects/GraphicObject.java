package poop2.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.css.Rect;

import poop2.Editor;

public abstract class GraphicObject {

	protected	Point	_position ;
	protected	double	_rotation ;
	protected	Color	_color ;
	protected	int		_lineWidth ;
	
	static	final	int	selectionRectWidthDelta = 7 ;
	static	final	int	selectionRectHeightDelta = 7 ;
	
	
	
	public GraphicObject(Point point, double rotation, Color color) {
		super();
		_position = point;
		_rotation = rotation;
		_color = color;
		_lineWidth = Editor.getLineWidth();
	}
	
	public GraphicObject() {
		
		this( new Point(), 0, Editor.getCurrentColor());
		
	}
	
	
	public Point get_position() {
		return _position;
	}

	public void set_position(Point _position) {
		this._position = _position;
	}

	public double get_rotation() {
		return _rotation;
	}

	public void set_rotation(double _rotation) {
		this._rotation = _rotation;
	}

	public Color get_color() {
		return _color;
	}

	public void set_color(Color _color) {
		this._color = _color;
	}
	
	public int get_lineWidth() {
		return _lineWidth;
	}

	public void set_lineWidth(int _lineWidth) {
		this._lineWidth = _lineWidth;
	}

	
	
	public void saveToStream( StringWriter stream ) {
		
		stream.write(this.getClass().getSimpleName() + "\n");
		writePointToStream(stream, _position);
		stream.write(_rotation + "\n");
		stream.write(_color.getRed() + " " + _color.getGreen() + " " + _color.getBlue() + "\n");
		stream.write(_lineWidth + "\n");
		
	}
	
	public	static	void	writeDoubleToStream( StringWriter stream, double d ) {
		stream.write( d + "\n");
	}
	
	public	static	void	writePointToStream( StringWriter stream, Point p ) {
		stream.write( (int)p.getX() + " " + (int)p.getY() + "\n");
	}
	
	public void loadFromStream( Scanner stream ) {
		
		_position = readPointFromStream(stream);
		_rotation = stream.nextDouble();
		_color = new Color(stream.nextInt(), stream.nextInt(), stream.nextInt());
		_lineWidth = stream.nextInt();
		
	}
	
	public	static	Point	readPointFromStream( Scanner stream ) {
		Point p = new Point();
		p.x = (int) stream.nextInt();
		p.y = (int) stream.nextInt();
		return p;
	}
	
	public	static	double	readDoubleFromStream( Scanner stream ) {
		return stream.nextDouble();
	}
	
	
	
	public void paint(Graphics g) {
		
		
	}
	
	public void move( Point newPoint ) {
		_position = new Point( newPoint ) ;
	}
	
	public boolean pointBelongs(Point p) {
		return this.getBoundsRectInWorldSpace().contains(p) ;
	}
	
	// get bounds rect in local space
	public abstract Rectangle getBoundsRect();
	
	public final Rectangle getBoundsRectInWorldSpace() {
		Rectangle rect = this.getBoundsRect();
		rect.x += _position.getX();
		rect.y += _position.getY();
		return rect ;
	}
	
	public	static	Rectangle	getBoundsRectFromPoints( Point[] points ) {
		
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE ;
		int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE ;
		
		for( Point p : points ) {
			minX = Math.min( (int) p.getX(), minX);
			minY = Math.min( (int) p.getY(), minY);
			
			maxX = Math.max( (int) p.getX(), maxX);
			maxY = Math.max( (int) p.getY(), maxY);
		}
		
		return enlargeRectForSelection( new Rectangle(minX, minY, maxX - minX, maxY - minY) );
	}
	
	public	static	Rectangle	enlargeRectForSelection( Rectangle boundsRect ) {
		Rectangle rect = new Rectangle(boundsRect);
		rect.grow(selectionRectWidthDelta, selectionRectHeightDelta);
		return rect ;
	}
	
	
}
