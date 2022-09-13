package util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
public class GridDrawableFrame extends JFrame{
private GridDrawablePanel pnl;
private int screenWidth, screenHeight;
	public GridDrawableFrame(int gridWidth, int gridHeight, int screenWidth, int screenHeight) {
		super();
		setSize(screenWidth+15, screenHeight+35);
		this.screenWidth = screenWidth+15;
		this.screenHeight = screenHeight+35;
		setResizable(false);
		pnl = new GridDrawablePanel(gridWidth, gridHeight, this.screenWidth, this.screenHeight);
		add(pnl);
	}
	
	public void addCenterCircle(int screenX, int screenY, int radius, int gridX, int gridY, Color color) {
		Shape circle = new Ellipse2D.Float(screenX-radius, screenY-radius, radius*2, radius*2);
		pnl.addGridShape(circle, gridX, gridY, color);
	}
	
	public void addCenterCircle( int radius, int gridX, int gridY, Color color) {
		int[] cellDim = pnl.getGridCellDim();
		Shape circle = new Ellipse2D.Float(cellDim[0]/2-radius, cellDim[1]/2-radius, radius*2, radius*2);
		pnl.addGridShape(circle, gridX, gridY, color);
	}
	
	public void addCenterCircle( int radius, int gridX, int gridY, Color color, String text) {
		int[] cellDim = pnl.getGridCellDim();
		Shape circle = new Ellipse2D.Float(cellDim[0]/2-radius, cellDim[1]/2-radius, radius*2, radius*2);
		pnl.addGridShape(circle, gridX, gridY, color);
		pnl.addGridCustomOperation(g->{
			g.setColor(Color.white);
			g.drawString(text, cellDim[0]/2-radius+10,cellDim[1]/2 ) ;
		
		},gridX, gridY);
	}
	
	public void addCenterCircle( int radius, int gridX, int gridY, Color color, String text, Color textColor) {
		int[] cellDim = pnl.getGridCellDim();
		Shape circle = new Ellipse2D.Float(cellDim[0]/2-radius, cellDim[1]/2-radius, radius*2, radius*2);
		pnl.addGridShape(circle, gridX, gridY, color);
		pnl.addGridCustomOperation(g->{
			g.setColor(textColor);
			g.drawString(text, cellDim[0]/2-radius,cellDim[1]/2 ) ;
		
		},gridX, gridY);
	}
	
	public void addCenterLine(int gridXStart, int gridYStart, int gridXEnd, int gridYEnd) {
		int[] cellDim = pnl.getGridCellDim();
		Shape line = new Line2D.Double(((double)gridXStart+0.5)*(double)cellDim[0], ((double)gridYStart+0.5)*(double)cellDim[1], ((double)gridXEnd+0.5)*(double)cellDim[0], ((double)gridYEnd+0.5)*(double)cellDim[1]);
		pnl.addShape(line);
	}
	
	public void addCenterLine(int gridXStart, int gridYStart, int gridXEnd, int gridYEnd, String text) {
		addCenterLine(gridXStart, gridYStart, gridXEnd, gridYEnd, text, Color.black);
	}
	
	public void addCircle(int radius, int centerX, int centerY, Color color) {
		pnl.addShape(new Ellipse2D.Float(centerX-radius, centerY-radius, radius*2, radius*2), color);
	}
	
	public void addLine(int startX, int startY, int endX, int endY) {
		pnl.addShape(new Line2D.Float(startX, startY, endX, endY));
	}
	
	
	public void addCenterLine(int gridXStart, int gridYStart, int gridXEnd, int gridYEnd, String text, Color textColor) {
		int[] cellDim = pnl.getGridCellDim();
		double startX = ((double)gridXStart+0.5)*(double)cellDim[0];
		double startY = ((double)gridYStart+0.5)*(double)cellDim[1];
		double endX = ((double)gridXEnd+0.5)*(double)cellDim[0];
		double endY = ((double)gridYEnd+0.5)*(double)cellDim[1];
		Shape line = new Line2D.Double(startX, startY,endX , endY);
		pnl.addCustomOperation(g->{
			g.setColor(Color.black);
			g.drawString(text, (int)((startX+endX)/2), (int)((startY+endY)/2)) ;
		
		});
		pnl.addShape(line);
	}
	
	public void addCustomOp(Consumer<Graphics> op) {
		pnl.addCustomOperation(op);
	}
	
	public void addCustomOp(Consumer<Graphics> op, int gridX, int gridY) {
		pnl.addGridCustomOperation(op, gridX, gridY);
	}
	
	public void colorTile(int gridX, int gridY, Color color) {
		pnl.addColorGridTile(gridX, gridY, color);
	}
	
	public void reset() {
		pnl.reset();
	}
	
	public void setShowGridLines(boolean showGridLines) {
		pnl.showGrid = showGridLines;
	}
	
	public void setGridLinesColor(Color color) {
		pnl.gridLinesColor = color;
	}
	
	public static void main(String[] args) {
		
		GridDrawableFrame frame = new GridDrawableFrame(8, 8, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addCenterCircle( 50, 4, 2, Color.blue, "Work");
		frame.addCenterCircle(50, 2, 3, Color.blue);
		frame.addCenterCircle( 50, 1, 4, Color.blue);
		frame.addCenterLine(2,2,3,8, "Hello");
		frame.setVisible(true);
		
		
		
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		pnl.paintImage(g);
		g.dispose();
		return img;
	}
}

class GridDrawablePanel extends DrawablePanel{
	private int gridWidth, gridHeight;
	private int screenWidth=0, screenHeight=0;
	private int gridCellWidth, gridCellHeight;
	private List<GridPosition<Shape>> gridShapes;
	private List<GridPosition<Consumer<Graphics>>> gridCustomOps;
	boolean showGrid = false;
	Color gridLinesColor = Color.black;
	public GridDrawablePanel(int gridWidth, int gridHeight, int screenWidth, int screenHeight) {
		super();
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		gridShapes = new ArrayList<>();
		gridCustomOps = new ArrayList<>();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		gridCellWidth = screenWidth/gridWidth;
		gridCellHeight = screenHeight/gridHeight;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		//Use this for anything that needs to be changed on resize
		if(screenWidth!=this.getWidth() || screenHeight!=this.getHeight()) {

		}
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		super.paintComponent(g);
		
		
		for(GridPosition<Shape> shape: gridShapes) {
			if(shape instanceof ColorGridPosition)
				g2.setColor(((ColorGridPosition<Shape>)shape).getColor());
			else
				g2.setColor(Color.black);
			
			g2.translate(shape.getX()*gridCellWidth, shape.getY()*gridCellHeight);
			g2.fill(shape.data);
			g2.translate(-shape.getX()*gridCellWidth, -shape.getY()*gridCellHeight);
		}
		
		for(GridPosition<Consumer<Graphics>> op: gridCustomOps) {
			g2.translate(op.getX()*gridCellWidth, op.getY()*gridCellHeight);
			op.data.accept(g2);
			g2.translate(-op.getX()*gridCellWidth, -op.getY()*gridCellHeight);
		}
		if(showGrid) {
			g2.setColor(gridLinesColor);
		for(int x=0;x<screenWidth;x+=gridCellWidth) {
			g2.drawLine(x, 0, x, screenHeight);
		}
		for(int x=0;x<screenHeight;x+=gridCellHeight) {
			g2.drawLine(0, x,screenWidth, x);
		}
		}

	}
	
	public void paintImage(Graphics g) {
		
		//Use this for anything that needs to be changed on resize
		if(screenWidth!=this.getWidth() || screenHeight!=this.getHeight()) {

		}
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		super.paintImage(g2);
		
		
		for(GridPosition<Shape> shape: gridShapes) {
			if(shape instanceof ColorGridPosition)
				g2.setColor(((ColorGridPosition<Shape>)shape).getColor());
			else
				g2.setColor(Color.black);
			
			g2.translate(shape.getX()*gridCellWidth, shape.getY()*gridCellHeight);
			g2.fill(shape.data);
			g2.translate(-shape.getX()*gridCellWidth, -shape.getY()*gridCellHeight);
		}
		
		for(GridPosition<Consumer<Graphics>> op: gridCustomOps) {
			g2.translate(op.getX()*gridCellWidth, op.getY()*gridCellHeight);
			op.data.accept(g2);
			g2.translate(-op.getX()*gridCellWidth, -op.getY()*gridCellHeight);
		}
		if(showGrid) {
			g2.setColor(gridLinesColor);
		for(int x=0;x<screenWidth;x+=gridCellWidth) {
			g2.drawLine(x, 0, x, screenHeight);
		}
		for(int x=0;x<screenHeight;x+=gridCellHeight) {
			g2.drawLine(0, x,screenWidth, x);
		}
		}

	}
	
	public int[] getGridCellDim() {
		return new int[] {gridCellWidth, gridCellHeight};
	}
	public void addGridShape(Shape shape, int gridX, int gridY) {
		gridShapes.add(new GridPosition<Shape>(shape, gridX, gridY));
	}
	
	public void addGridShape(Shape shape, int gridX, int gridY, Color color) {
		gridShapes.add(new ColorGridPosition<Shape>(shape, gridX, gridY, color));
	}
	
	public void addColorGridTile(int gridX, int gridY, Color color) {
		gridShapes.add(new ColorGridPosition<Shape>(new Rectangle(0,0,gridCellWidth, gridCellHeight), gridX, gridY, color));
	}
	
	public void removeGridShape(int gridX, int gridY) {
		gridShapes.removeIf(shape->shape.getX()==gridX&&shape.getY()==gridY);
	}
	
	public List<GridPosition<Shape>> getGridShapes() {
		return gridShapes;
	}
	
	public void removeGridShape(int index) {
		gridShapes.remove(index);
	}
	
	public void addGridCustomOperation(Consumer<Graphics> op, int gridX, int gridY) {
		gridCustomOps.add(new GridPosition<Consumer<Graphics>>(op, gridX, gridY));
	}
	
	public void removeGridCustomOperation(int gridX, int gridY) {
		gridCustomOps.removeIf(op->op.getX()==gridX&&op.getY()==gridY);
	}
	
	public List<GridPosition<Consumer<Graphics>>> getGridCustomOperations() {
		return gridCustomOps;
	}
	
	public void removeGridCustomOperation(int index) {
		gridCustomOps.remove(index);
	}
	
	class ColorGridPosition<T> extends GridPosition<T>{
		private Color color;
		public ColorGridPosition(T data, int gridX, int gridY, Color color) {
			super(data, gridX, gridY);
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
	}
	
	class GridPosition<T>{
		int gridX, gridY;
		T data;
		public GridPosition(T data, int gridX, int gridY) {
			this.gridX = gridX;
			this.gridY = gridY;
			this.data = data;
		}
		
		public int getX() {
			return gridX;
		}
		public int getY() {
			return gridY;
		}
		public T getData() {
			return data;
		}
	}
	
	public void reset() {
		super.reset();
		gridShapes = new ArrayList<>();
		gridCustomOps = new ArrayList<>();
	}
}

class DrawablePanel extends JPanel{
	List<ColorShape> shapes;
	List<Consumer<Graphics>> customOperations;
	public DrawablePanel() {
		super();
		shapes = new ArrayList<>();
		customOperations = new ArrayList<>();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g.create();
		for(ColorShape shape: shapes) {
			g2.setColor(shape.getColor());
			if(shape.getShape() instanceof Line2D.Float || shape.getShape() instanceof Line2D.Double)
				g2.draw(shape.getShape());
			else
				g2.fill(shape.getShape());
		}
		for(Consumer<Graphics> op: customOperations)
			op.accept(g2);
	}
	
	public void paintImage(Graphics g) {
		Graphics2D g2 = (Graphics2D)g.create();
		for(ColorShape shape: shapes) {
			g2.setColor(shape.getColor());
			if(shape.getShape() instanceof Line2D.Float || shape.getShape() instanceof Line2D.Double)
				g2.draw(shape.getShape());
			else
				g2.fill(shape.getShape());
		}
		for(Consumer<Graphics> op: customOperations)
			op.accept(g2);
	}
	
	public void addShape(Shape shape) {
		shapes.add(new ColorShape(shape, Color.black));
	}
	
	public void addShape(Shape shape, Color color) {
		shapes.add(new ColorShape(shape, color));
	}
	
	public void removeShape(Shape shape) {
		shapes.removeIf(cshape->cshape.getShape().equals(shape));
	}
	
	public List<ColorShape> getShapes() {
		return shapes;
	}
	
	public void removeShape(int index) {
		shapes.remove(index);
	}
	
	public void addCustomOperation(Consumer<Graphics> op) {
		customOperations.add(op);
	}
	
	public void removeCustomOperation(Consumer<Graphics> op) {
		customOperations.remove(op);
	}
	
	public List<Consumer<Graphics>> getCustomOperations() {
		return customOperations;
	}
	
	public void removeCustomOperation(int index) {
		customOperations.remove(index);
	}
	
	class ColorShape{
		private Shape shape;
		private Color color;
		public ColorShape(Shape shape, Color color) {
			this.shape = shape;
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
		
		public Shape getShape() {
			return shape;
		}
	}
	
	public void reset() {
		shapes = new ArrayList<ColorShape>();
		customOperations = new ArrayList<Consumer<Graphics>>();
	}
	
}
