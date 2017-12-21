package poop2.tools;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Consumer;

import poop2.Editor;
import poop2.objects.GraphicObject;

public class SelectTool extends Tool {

	protected	GraphicObject	_objectForMoving = null ;
	protected	Point	_objectForMovingStartPos = null ;
	protected	boolean	_selectedObjectSinceDragStarted = false ;
	
	
	public SelectTool() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void OnLeftMouseClicked(Point point) {
		// TODO Auto-generated method stub
		
		ArrayList<GraphicObject> objects = Editor.getObjectsByPosition(point);
		if(0 == objects.size()) {
			Editor.selectObject(null);
			return ;
		}
		
		// some objects are under mouse
		if(null == Editor.getSelectedObject()) {
			// no object is selected currently
			// select the first object under mouse
			Editor.selectObject(objects.get(0));
		} else {
			// an object is already selected
			
			int index = objects.indexOf(Editor.getSelectedObject());
			if(index >= 0) {
				// currently selected object is under mouse
				// select the one below him
				index ++ ;
				index %= objects.size();
				
				Editor.selectObject(objects.get(index));
			} else {
				// select the first object under mouse
				Editor.selectObject(objects.get(0));
			}
			
		}
		
		System.out.println("Selected object - " + Editor.getSelectedObject().toString());
	}
	
	@Override
	public void onLeftMousePressed(Point point) {
		
		super.onLeftMousePressed(point);
		
		_objectForMoving = null ;
		_selectedObjectSinceDragStarted = false ;
		
		ArrayList<GraphicObject> objects = Editor.getObjectsByPosition(point);
		
		if(0 == objects.size())
			return ;
		
		if(objects.contains(Editor.getSelectedObject())) {
			// selected object is under mouse
			// mark him for moving
			_objectForMoving = Editor.getSelectedObject();
		} else {
			// selected object is not under mouse
			// find object for moving
			// select him
			
			GraphicObject o = objects.get(0);
		//	Editor.selectObject(o);
			_objectForMoving = o ;
		}
		
		_objectForMovingStartPos = new Point( _objectForMoving.get_position() ) ;
		
	}

	@Override
	public void onMouseDragged(Point point) {
		
		// move selected object
		
		if(_objectForMoving != null) {
			
			if(!_selectedObjectSinceDragStarted) {
				// select object
				Editor.selectObject(_objectForMoving);
				_selectedObjectSinceDragStarted = true ;
			}
			
			_objectForMoving.move( getPositionForMovingObject(point) );
		}
		
		super.onMouseDragged(point);
	}

	@Override
	public void onLeftMouseReleased(Point point) {
		
		super.onLeftMouseReleased(point);
		
		if(_objectForMoving != null) {
			// we finished moving an object
			// register undo and redo action
			
			GraphicObject tempObj = _objectForMoving ;
			Point originalPoint = new Point(_objectForMovingStartPos);
			Point newPoint = getPositionForMovingObject(point);
			
			if(!newPoint.equals(originalPoint)) {
				Consumer<Object> undoAction = (arg) -> { tempObj.set_position(originalPoint); } ;
				Consumer<Object> redoAction = (arg) -> { tempObj.set_position(newPoint); } ;
				
				Editor.registerUndoRedoAction("move", undoAction, redoAction, null);
			}
		}
		
		_objectForMoving = null ;
	}
	
	Point	getPositionForMovingObject( Point point ) {
		double offsetX = point.getX() - _lastPressPos.getX() ;
		double offsetY = point.getY() - _lastPressPos.getY() ;
		
		return new Point( (int) (_objectForMovingStartPos.getX() + offsetX),
				(int) (_objectForMovingStartPos.getY() + offsetY) );
	}

	@Override
	public void onCancelled() {
		
		super.onCancelled();
		
		_objectForMoving = null ;
	}
	
	

	
	
	
}
