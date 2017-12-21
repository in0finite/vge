package poop2.tools;

import java.awt.Point;
import java.util.ArrayList;

import poop2.Editor;
import poop2.objects.GraphicObject;

public class DeleteTool extends Tool {

	public DeleteTool() {
		
	}

	
	@Override
	public void OnLeftMouseClicked(Point point) {
		
		ArrayList<GraphicObject> objects = Editor.getObjectsByPosition(point);
		
		if(0 == objects.size()) {
			return ;
		}
		
		if(objects.size() > 1) {
			
			if(objects.contains(Editor.getSelectedObject())) {
				// selected object has priority when deleting
				Editor.removeGraphicItem(Editor.getSelectedObject(), true);
			} else {
				// delete first one
				Editor.removeGraphicItem(objects.get(0), true);
			}
			
		} else if( 1 == objects.size()) {
			// just delete him
			Editor.removeGraphicItem(objects.get(0), true);
		}
		
	}
	
}
