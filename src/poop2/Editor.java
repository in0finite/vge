package poop2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Point;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import poop2.objects.GraphicObject;
import poop2.tools.Tool;

class	UndoRedoAction {
	String	description = "";
	Consumer<Object>	undoAction = null;
	Consumer<Object>	redoAction = null;
	Object		argument = null;
	public UndoRedoAction(String description, Consumer<Object> undoAction, Consumer<Object> redoAction, Object argument) {
		super();
		this.description = description;
		this.undoAction = undoAction;
		this.redoAction = redoAction;
		this.argument = argument;
	}
}

public class Editor extends JFrame implements MouseListener, MouseMotionListener {

	static	ArrayList<Tool> _tools = new ArrayList<>();
	static	ArrayList<GraphicObject> _graphicObjects = new ArrayList<>();
	static	Color	_currentColor = Color.BLACK ;
	static	int		_currentLineWidth = 1 ;
	static	Tool	_selectedTool = null ;
	static	GraphicObject	_selectedObject = null ;
	
	private	static	Editor	_instance = null;
	static	JFrame	_frame = null;
	static	Canvas	_canvas = null;
	static	JLabel	_leftStatusLabel = null;
	static	JLabel	_rightStatusLabel = null;
	static	JComponent	_selectedColorComponent = null;
	static	JComboBox<Integer> 	_lineWidthComboBox = null;
	static	JButton	_undoButton = null;
	static	JButton	_redoButton = null;
	static	ArrayList<JToggleButton>	_toolButtons = new ArrayList<>();
	
	static	boolean	_sceneHasFilePath = false ;
	static	String	_sceneFilePath = "" ;
	
	private	static	ArrayList<UndoRedoAction>	_undoActions = new ArrayList<>();
	private	static	ArrayList<UndoRedoAction>	_redoActions = new ArrayList<>();
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new Editor();
		
	}
	
	
	private Editor() {
		
		_instance = this ;
		_frame = this ;
		
		SwingUtilities.invokeLater( () -> init() );
		
	}
	
	private void init() {
		
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		// add some default tools
		_tools.add(new poop2.tools.SelectTool());
		_tools.add(new poop2.tools.DeleteTool());
		_tools.add(new poop2.tools.RectangleTool());
		_tools.add(new poop2.tools.LineTool());
		_tools.add(new poop2.tools.MultiLineTool());
		_tools.add(new poop2.tools.PolygonTool());
		
		// make select tool the default tool
		_selectedTool = _tools.get(0);
		
		
		// create layouts
		
		// north - edit tools
		// west - toolbar items
		// east - canvas
		
		this.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel();
	//	JPanel westPanel = new JPanel();
		JPanel canvasPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		
		// line width
		JLabel lineWidthLabel = new JLabel("line width");
		Integer[] lineWidths = new Integer[] {1, 2, 3, 4};
		_lineWidthComboBox = new JComboBox<>(lineWidths);
		_lineWidthComboBox.addActionListener( (e) -> lineWidthChanged(e) );
		
		// color
		Color[] colors = new Color[] { Color.WHITE, Color.DARK_GRAY, Color.GRAY, Color.BLACK, new Color(0, 0, 100), Color.BLUE, Color.CYAN,
				Color.GREEN, new Color(0, 100, 0), new Color(100, 50, 0), new Color(150,150,0), Color.YELLOW, Color.ORANGE, Color.RED, Color.MAGENTA, Color.PINK};
		JPanel colorsPalettePanel = new JPanel();
		colorsPalettePanel.setLayout(new GridLayout( 2, colors.length / 2 ));
		for(int i=0; i < colors.length; i++) {
			JButton clr = new JButton();
		//	clr.setSize(30, 30);
			clr.setPreferredSize(new Dimension(30, 30));
			clr.setBackground(colors[i]);
			clr.addActionListener( (e) -> Editor.setColor(clr.getBackground()) );
			colorsPalettePanel.add(clr);
		}
		
		_selectedColorComponent = new JButton();
	//	_selectedColorComponent.setMaximumSize(new Dimension( 30, 30 ));
	//	_selectedColorComponent.setMinimumSize(new Dimension( 30, 30 ));
		_selectedColorComponent.setPreferredSize(new Dimension( 40, 40 ));
		((JButton)_selectedColorComponent).addActionListener( (arg) -> Editor.setColor(JColorChooser.showDialog(_frame, "choose color", Editor.getCurrentColor())) );
		
		JPanel colorsPanel = new JPanel(new FlowLayout());
		colorsPanel.add(colorsPalettePanel);
		colorsPanel.add(_selectedColorComponent);
		
		// tools
		JPanel toolsPanel = new JPanel();
		toolsPanel.setLayout(new GridLayout(1, _tools.size()));
		
		for(Tool item : _tools) {
			Dimension dimension = new Dimension(30, 30);
			String name = item.getClass().getSimpleName();
			
			ImageIcon icon = new ImageIcon("icons/" + name + ".png");
			icon = new ImageIcon( icon.getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH) );
			
			JToggleButton b = new JToggleButton(icon);
			
		//	b.setBackground(Color.WHITE);
		//	b.setSize(dimension);
		//	b.setMinimumSize(dimension);
		//	b.setMaximumSize(dimension);
			b.setMargin(new Insets(0, 0, 0, 0));
			b.addActionListener( (arg) -> toolbarItemSelected(item) );
			
			toolsPanel.add(b);
			_toolButtons.add(b);
		}
		
		// undo and redo buttons
		_undoButton = new JButton("Undo");
		_undoButton.addActionListener( (arg) -> Editor.undo() );
		_redoButton = new JButton("Redo");
		_redoButton.addActionListener( (arg) -> Editor.redo() );
		
		
	//	northPanel.setLayout(new GridLayout(1, 5));
		northPanel.setLayout(new FlowLayout());
		northPanel.add(lineWidthLabel);
		northPanel.add(_lineWidthComboBox);
		northPanel.add(colorsPanel);
		northPanel.add(toolsPanel);
		northPanel.add(_undoButton);
		northPanel.add(_redoButton);
		
		// canvas
		canvasPanel.setBackground(Color.GRAY);
		canvasPanel.setForeground(Color.GREEN);
		canvasPanel.setLayout(new BorderLayout());
		
		_canvas = new Canvas();
	//	_canvas.setSize(400, 300);
	//	_canvas.setBackground(Color.red);
		canvasPanel.add(_canvas, BorderLayout.CENTER);
		
		
		// status bar
		_leftStatusLabel = new JLabel("", JLabel.LEFT);
		_rightStatusLabel = new JLabel("", JLabel.RIGHT);
		
		southPanel.setBackground(Color.GRAY);
		southPanel.setLayout(new GridLayout(1, 2));
		southPanel.add(_leftStatusLabel);
		southPanel.add(_rightStatusLabel);
		
		
		// add panels
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
	//	this.getContentPane().add(westPanel, BorderLayout.WEST);
		this.getContentPane().add(canvasPanel, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
	
		
		// create menu
		_frame.setJMenuBar( Menu.createMenu() );
		
		
		// listeners
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	
		
		this.setVisible(true);
		
		updateFrameTitle();
		updateUndoRedoButtons();
		updateGraphics();
		
	}
	
	
	public static void addGraphicItem( GraphicObject objectToAdd, boolean addUndoRedoAction ) {
		
		if(_graphicObjects.contains(objectToAdd))
			return ;
		
		_graphicObjects.add(objectToAdd);
		
		if(addUndoRedoAction) {
			
			Consumer<Object> undoAction = (arg) -> {
					GraphicObject obj = (GraphicObject) arg;
					Editor.removeGraphicItem(obj, false);
				};
			Consumer<Object> redoAction = (arg) -> {
				GraphicObject obj = (GraphicObject) arg;
				Editor.addGraphicItem(obj, false);
				};
			
			registerUndoRedoAction("create", undoAction, redoAction, objectToAdd);
		}
		
	}
	
	public static void removeGraphicItem( GraphicObject objectToRemove, boolean addUndoRedoAction ) {
		
		if(!_graphicObjects.contains(objectToRemove))
			return ;
		
		_graphicObjects.remove(objectToRemove);
		
		if(objectToRemove == _selectedObject)
			_selectedObject = null;
		
		if(addUndoRedoAction) {
		//	StringWriter stream = new StringWriter();
		//	item.saveToStream(stream);
			
			Consumer<Object> undoAction = (arg) -> {
			//	GraphicItem[] array = (GraphicItem[]) arg ;
				GraphicObject obj = (GraphicObject) arg;
				try {
				//	obj = Editor.createObjectFromStream( new Scanner( stream.toString() ) );
					Editor.addGraphicItem(obj, false);
				} catch (Exception ex) { }
				// update reference to newly created object
			//	array[0] = obj ;
				};
			Consumer<Object> redoAction = (arg) -> {
			//	GraphicItem[] array = (GraphicItem[]) arg ;
				GraphicObject obj = (GraphicObject) arg;
				Editor.removeGraphicItem(obj, false);
			//	array[0] = null;
				};
			
			registerUndoRedoAction("delete", undoAction, redoAction, objectToRemove);
		}
		
		updateGraphics();
	}
	
	public static Color getCurrentColor() {
		return _currentColor ;
	}
	
	public static void setColor( Color color ) {
		
		if(null == color)
			return ;
		
		_currentColor = color ;
		
		if(_selectedObject != null) {
			Color oldColor = new Color(_selectedObject.get_color().getRGB());
			
			_selectedObject.set_color(color);
			
			// register undo/redo actions
			Color newColor = new Color(color.getRGB());
			GraphicObject tempObj = _selectedObject ;
			
			Consumer<Object> undoAction = (arg) -> { tempObj.set_color(oldColor); };
			Consumer<Object> redoAction = (arg) -> { tempObj.set_color(newColor); };
			registerUndoRedoAction("color", undoAction, redoAction, null);
		}
		
		updateGraphics();
	}
	
	public static int getLineWidth() {
		return _currentLineWidth ;
	}
	
	public static java.awt.Point getCurrentMousePosition() {
		return _frame.getMousePosition();
	}
	
	public	static	Iterable<GraphicObject> 	getGraphicObjects() {
		return _graphicObjects ;
	}
	
	public	static	ArrayList<GraphicObject>	getObjectsByPosition(Point p) {
		ArrayList<GraphicObject> objects = new ArrayList<>();
		for(GraphicObject o : _graphicObjects) {
			if( o.pointBelongs(p) )
				objects.add(0, o);	// add to beginning
		}
		// reverse the order
	//	for(int i=0; i < objects.size() / 2; i++)
	//		objects.set(i, objects.get(objects.size() - i - 1));

		return objects;
	}
	
	public	static	Tool	getSelectedTool() {
		return _selectedTool ;
	}
	
	public	static	GraphicObject	getSelectedObject() {
		return _selectedObject ;
	}
	
	public	static	void	selectObject( GraphicObject o ) {
		_selectedObject = o ;
		_frame.repaint();
	}
	
	public	static	boolean	isPointInsideCanvas( Point p ) {
		return _canvas.contains( convertToCanvasPoint(p) );
	}
	
	public	static	Point	convertToCanvasPoint( Point p ) {
		// the point must be inside canvas
		java.awt.Rectangle rect = _canvas.getBounds();
		Point convertedPoint = SwingUtilities.convertPoint(_frame, p, _canvas);
	//	return new Point(p.x - _canvas.getX(), p.y - _canvas.getY());
		return convertedPoint ;
	}
	
	public	static	Point	getMousePositionInCanvas() {
		Point p = getCurrentMousePosition();
		if(null == p)
			return null;
		return convertToCanvasPoint(p);
	}

	
	static void toolbarItemSelected( Tool tool ) {
		
		if(_selectedTool != null) {
			// a tool is already selected
			if(_selectedTool != tool) {
				// another tool clicked
				return ;
			} else {
				// same tool clicked
				_selectedTool.onCancelled();
				_selectedTool = null;
			}
		} else {
			// no tool is selected
			_selectedTool = tool ;
			_selectedTool.OnSelected();
		}
		
		/*
		if(_selectedToolbarItem != null) {
			// cancel current operation
			_selectedToolbarItem.onCancelled();
		}
		
		_selectedToolbarItem = tool ;
		
		if(_selectedToolbarItem != null) {
			_selectedToolbarItem.OnSelected();
		}
		*/
		
		updateGraphics();
		
	}
	
	private static void lineWidthChanged(ActionEvent e) {
		
		JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
		
		if(cb.getSelectedIndex() < 0)
			return ;
		
		_currentLineWidth = (int) cb.getSelectedItem();
		
		if(_selectedObject != null) {
			int oldLineWidth = _selectedObject.get_lineWidth();
			
			// change line width of currently selected object
			_selectedObject.set_lineWidth(_currentLineWidth);
			
			// register undo/redo actions
			int newLineWidth = _currentLineWidth ;
			GraphicObject tempObj = _selectedObject ;
			
			Consumer<Object> undoAction = (arg) -> { tempObj.set_lineWidth(oldLineWidth); };
			Consumer<Object> redoAction = (arg) -> { tempObj.set_lineWidth(newLineWidth); };
			registerUndoRedoAction("line width", undoAction, redoAction, null);
		}
		
		updateGraphics();
	}
	
	
	public	static	void	undo() {
		
		if(0 == _undoActions.size())
			return ;
		
		UndoRedoAction action = _undoActions.remove(_undoActions.size() - 1);
		action.undoAction.accept(action.argument);
		_redoActions.add(action);
		
		updateUndoRedoButtons();
		updateGraphics();
	}
	
	public	static	void	redo() {
		
		if(0 == _redoActions.size())
			return ;
		
		UndoRedoAction action = _redoActions.remove(_redoActions.size() - 1);
		action.redoAction.accept(action.argument);
		_undoActions.add(action);
		
		updateUndoRedoButtons();
		updateGraphics();
	}
	
	public	static	void	registerUndoRedoAction( String description, Consumer<Object> undoAction, Consumer<Object> redoAction,
			Object argument) {
		
		_undoActions.add( new UndoRedoAction(description, undoAction, redoAction, argument));
		
		// we can no longer redo undoed actions
		// clear all redo actions
		_redoActions.clear();
		
		updateUndoRedoButtons();
	}
	
	static	void	clearUndoRedoActions() {
		_undoActions.clear();
		_redoActions.clear();
		updateUndoRedoButtons();
	}
	
	
	// Updates status bar, selected color, and repaints the frame. Function is thread safe.
	public	static	void	updateGraphics() {
		
		_instance.updateStatusBar();
		
		SwingUtilities.invokeLater( () -> {
			
			// update selected color
			if(!_selectedColorComponent.getBackground().equals(_currentColor))
				_selectedColorComponent.setBackground(_currentColor);
			
			// enable/disable tool buttons
			for(int i=0; i < _toolButtons.size(); i++) {
				boolean state = false;
				if(null == _selectedTool)
					state = true;
				else
					state = _tools.get(i) == _selectedTool ;
				if(state != _toolButtons.get(i).isEnabled()) {
					_toolButtons.get(i).setEnabled(state);
				//	_toolButtons.get(i).setBackground( state ? Color.WHITE : Color.RED );
				}
			}
			
			/*
			// update line width combo box
			int lineWidthToDisplay = Editor.getLineWidth();
			if(_selectedObject != null)
				lineWidthToDisplay = _selectedObject.get_lineWidth();
		//	if(_lineWidthComboBox.getSelectedIndex() >= 0 && !_lineWidthComboBox.getSelectedItem().equals(lineWidthToDisplay))
			_lineWidthComboBox.setSelectedIndex(lineWidthToDisplay - 1);
			*/
			
			} );
		
		_frame.repaint();
		
	}
	
	void	updateStatusBar() {
		
		SwingUtilities.invokeLater(() -> {

			String str = "";
			Tool tool = Editor.getSelectedTool();
			if(tool != null)
				str += tool.getClass().getSimpleName() + " ";
			Color color = Editor.getCurrentColor();
			str += "color [" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + "] " ;
			str += "line width " + Editor.getLineWidth() + " " ;
			
			str += "  undo actions: " ;
			for(UndoRedoAction a : _undoActions)
				str += a.description.charAt(0) + ", " ;
			
			_leftStatusLabel.setText(str);
			
			str = "" ;
			str += "pos" ;
			Point point = Editor.getCurrentMousePosition();
			if(point != null)
				str += " " + point.x + " " + point.y ;
			if(tool != null)
				str += " " + tool.getStatusBarText();
			
			_rightStatusLabel.setText(str);
		}

		);
		
	}
	
	// thread safe
	static	void	updateFrameTitle() {
		
		SwingUtilities.invokeLater(() -> {
			String newTitle = "VGE" ;
			if(_sceneHasFilePath) {
				Path p = Paths.get(_sceneFilePath);
				newTitle += " - " + p.getFileName().toString() ;
			}
			_frame.setTitle(newTitle);
		}
		);
		
	}
	
	// thread safe
	static	void	updateUndoRedoButtons() {
		
		SwingUtilities.invokeLater( () -> {
			_undoButton.setEnabled( !_undoActions.isEmpty() );
			_redoButton.setEnabled( !_redoActions.isEmpty() );
		});
		
	}
	
	
	public	static	boolean	saveSceneToFile( String filePath ) {

		/*
		try(FileOutputStream fileOut = new FileOutputStream(filePath);
				ObjectOutputStream oos = new ObjectOutputStream(fileOut)) {
			
			for( GraphicItem obj : _graphicItems) {
				oos.writeObject(obj);
			}
			
			oos.flush();
			
			return true;
		*/
		
		try( FileOutputStream fileOut = new FileOutputStream(filePath) ) {
			
		//	XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(fileOut));
		//	encoder.writeObject(_graphicItems);
		//	encoder.close();
			
		//	for(GraphicItem obj : _graphicItems) {
		//		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		//		Marshaller m = context.createMarshaller();
		//		m.marshal(new JAXBElement(new QName(obj.getClass().getSimpleName()), obj.getClass(), obj), fileOut);
		//	}
		//	fileOut.flush();
			
			StringWriter stream = new StringWriter();
			for( GraphicObject obj : _graphicObjects) {
				obj.saveToStream(stream);
			}
			fileOut.write(stream.getBuffer().toString().getBytes());
			fileOut.flush();
			
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(_frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			
			updateFrameTitle();
		}
		
		return false ;
	}
	
	public	static	boolean	loadSceneFromFile( String filePath ) {

		try( Scanner fileIn = new Scanner( new File(filePath) ) ) {
			
			_graphicObjects.clear();
			clearUndoRedoActions();
			
			while(fileIn.hasNext()) {
				createObjectFromStream(fileIn);
			}
			
			return true;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(_frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			
			updateFrameTitle();
		}
		
		return false ;
	}
	
	static	GraphicObject	createObjectFromStream( Scanner stream ) throws Exception {
		
		// get object name
		String name = stream.next();
		
		// ask tools to create object
	//	ToolbarItem tool = _toolbarItems.stream().filter((t) -> ! t.canLoadObjectFromStream(name)).findFirst().get();
		Tool tool = null;
		for( Tool t : _tools ) {
			if(t.canLoadObjectFromStream(name)) {
				tool = t;
				break ;
			}
		}
		
		if(null == tool) {
			// there is no tool which can load this object
			throw new Exception("There is no tool which can load object: " + name);
		}
		
		GraphicObject obj = tool.loadObject(stream);
		_graphicObjects.add( obj );
		
		return obj;
	}
	
	public	static	void	closeScene() {
		
		_sceneHasFilePath = false ;
		_graphicObjects.clear();
		_selectedObject = null;
		clearUndoRedoActions();
		updateFrameTitle();
		updateGraphics();
		
	}


	void handleMouseEvent( MouseEvent e, java.util.function.BiConsumer<Tool, Point> func ) {

		if(isPointInsideCanvas(e.getPoint())) {
			if(_selectedTool != null) {
				func.accept( _selectedTool, convertToCanvasPoint(e.getPoint()) );
			}
		}
		
		updateStatusBar();
		
		_frame.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		handleMouseEvent(e, (tool, pos) -> tool.onMouseDragged(pos) );
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		handleMouseEvent(e, (tool, pos) -> tool.OnMouseMove(pos) );
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
		System.out.println("mouse clicked " + e.getX() + " " + e.getY());
		
		handleMouseEvent(e, (tool, pos) ->
		{
			if( 2 == e.getClickCount() )
				tool.onLeftMouseDoubleClicked(pos);
			else
				tool.OnLeftMouseClicked(pos) ;
		}
		);
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		handleMouseEvent(e, (tool, pos) -> tool.onLeftMousePressed(pos) );
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		handleMouseEvent(e, (tool, pos) -> tool.onLeftMouseReleased(pos) );
	}
		
	

}
