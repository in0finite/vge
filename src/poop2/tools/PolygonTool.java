package poop2.tools;

import java.awt.Point;
import java.util.Scanner;

import poop2.Editor;
import poop2.objects.GraphicObject;
import poop2.objects.MultiLineItem;
import poop2.objects.PolygonItem;

public class PolygonTool extends MultiLineTool {

	
	@Override
	protected Point getLastPositionForMultiLine(Point clickPoint) {
		return _leftMousePositions.get(0);
	}

	@Override
	protected void createObject() {
		PolygonItem item = new PolygonItem( _leftMousePositions.toArray(new Point[_leftMousePositions.size()]) );
		Editor.addGraphicItem(item, true);
	}

	@Override
	protected int getMinNumberOfPoints() {
		return 3 ;
	}

	
	@Override
	public boolean canLoadObjectFromStream(String objectName) {
		return PolygonItem.class.getSimpleName().equals( objectName ) ;
	}

	@Override
	public GraphicObject loadObject(Scanner stream) {
		PolygonItem obj = new PolygonItem();
		obj.loadFromStream(stream);
		return obj;
	}
	

}
