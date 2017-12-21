package poop2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import poop2.objects.GraphicObject;
import poop2.tools.Tool;

public class Canvas extends JPanel {

	public Canvas() {
		// TODO Auto-generated constructor stub
		
		this.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		
	}
	
	
	/*
	@Override
	public Dimension getMaximumSize() {
		// TODO Auto-generated method stub
		return super.getMaximumSize();
	}

	@Override
	public Dimension getMinimumSize() {
		// TODO Auto-generated method stub
	//	return super.getMinimumSize();
		return this.getParent().getSize();
	}

	@Override
	public Dimension getPreferredSize() {
		// TODO Auto-generated method stub
		return this.getParent().getSize();
	//	return super.getPreferredSize();
	}
	*/

	@Override
	public void paintComponent( Graphics g ) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g ;
		
	//	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		
	//	g2d.setColor(Color.red);
	//	g2d.drawString("asjkffasfsdfb", 200, 200);
		
		AffineTransform originalTransform = g2d.getTransform();
		
		for( GraphicObject item : Editor.getGraphicObjects() ) {
			
			// reset transform
			g2d.setTransform(originalTransform);
			
			// set his position and rotation
			g2d.translate( (int) item.get_position().getX(), (int) item.get_position().getY());
			g2d.rotate(item.get_rotation());
			
			// set his color
			g2d.setColor(item.get_color());
			
			// set stroke
			BasicStroke stroke = null;
			if(Editor.getSelectedObject() == item)
				stroke = new BasicStroke( item.get_lineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                        10.0f, new float[]{5.0f}, 0.0f );
			else
				stroke = new BasicStroke(item.get_lineWidth());
			
			g2d.setStroke(stroke);
			
			item.paint(g2d);
		}
		
		// reset transform
		g2d.setTransform(originalTransform);
		
		Tool tool = Editor.getSelectedTool();
		if(tool != null) {
			g2d.setStroke(new BasicStroke(Editor.getLineWidth()));
			tool.paint(g2d);
		}
		
		// reset stroke
		g2d.setStroke(new BasicStroke(1));
		
		GraphicObject selectedObject = Editor.getSelectedObject();
		if( selectedObject != null ) {
			// draw selection rect
			g2d.setColor(Color.BLUE);
			Rectangle rect = selectedObject.getBoundsRectInWorldSpace();
			g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 5, 5);
		}
		
	}

}
