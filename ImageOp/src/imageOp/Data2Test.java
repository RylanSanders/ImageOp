package imageOp;

import java.awt.Color;
import java.awt.Point;
import java.awt.GridLayout;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.GUIImageUtil;
public class Data2Test {
	
	public static String exampleImgs = "./ExampleImgs/";
	public static void main(String[] args) {
		

		testBasicPixelizer();
		//testBasicMorphoOperations();
		//testBasicCanny();
		
		//testLongCannyGifGeneration();
		
		
		
		//testCannyEdgeDetectionWithSliders();
		

		//testCannyEdgeGifGeneration();
		
		
		//testPerlinNoiseWithSliders();
		
		//testColorTolerence();
		
		
		//testPixelChangeOperation();
		
		//testPixelizeWithGroups();
		
		//testDelauneyTriangulation();
		
		
}
	
	static void testBasicPixelizer() {
		Consumer<List<Object>> r = (arr)->SourceOperations.fileSource(exampleImgs + "BookV1.jpg")
				.FileToImg()
				.simpleSharpen()
				.tolerancePixelize((int)arr.get(0), (int)arr.get(1))
				.showImage();
				new ControlPanel(r, "Slider Tolerance 1 255", "Slider Pix 1 1100");
	}
	
	static void testBasicMorphoOperations() {
		Consumer<List<Object>> r = (arr)->SourceOperations.fileSource(exampleImgs +"BookV1.jpg")
				.FileToImg()
				.advMorpho(ImageOperation.AdvMorphoOps.opening,(int)arr.get(0), (int)arr.get(1))
				.showImage((ImageFrame)arr.get(2));
				
				new ControlPanel(r, "Slider Threshold1 1 20", "Slider Threshold2 1 20", "Frame 500 500");
	}
	
	static void testBasicCanny() {
		SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
		.FileToImg()
		.maskComposite(
				SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
				.FileToImg()
				.detectEdges(1, 200)
				.img()
				)
		.showImage();
	}
	
	static void testLongCannyGifGeneration() {
		BufferedImage cat = SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg").FileToImg().img();
		SourceOperations.generateIntListOp(1, 400, x->x+1)
		.monitor(x->System.out.println(x))
		.map(num->
				SourceOperations.imgSource(cat)
				.maskComposite(
						SourceOperations.imgSource(cat)
						.detectEdges(num, 1)
						.img()
						)
				.img()
				)
		.consume(x->GifUtil.generateGif(x, "example2.gif", 50));
	}
	
	static void testCannyEdgeDetectionWithSliders() {
		BufferedImage cat = SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg").FileToImg().img();
		Consumer<List<Object>> r = (arr)->SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
		.FileToImg()
		.maskComposite(
				SourceOperations.imgSource(cat)
				.detectEdges((int)arr.get(0), (int)arr.get(1))
				.img()
				)
		.showImage((ImageFrame)arr.get(2));
		
		new ControlPanel(r, "Slider Threshold1 1 600", "Slider Threshold2 1 1000", "Frame 500 500");
	}
	
	
	static void testCannyEdgeGifGeneration() {
		BufferedImage cat = SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg").FileToImg().img();
		SourceOperations.generateIntListOp(0, 600, x->x+10)
		.monitor(x->System.out.println(x))
		.map(num->
				SourceOperations.imgSource(cat)
				.maskComposite(
						SourceOperations.imgSource(cat)
						.detectEdges(1, 600-num)
						.img()
						)
				.img()
				)
		.consume(x->GifUtil.generateGif(x, "example.gif", 25));   
	}
	
	static void testPerlinNoiseWithSliders() {
		Consumer<List<Object>> r = (arr)->SourceOperations
				.perlinImageSrc(500, 500, (Integer)arr.get(0)/1000.0, (Integer)arr.get(1)/100.0, val->CommonFunctions.getColor(val))
		.showImage((ImageFrame)arr.get(2));
		
		new ControlPanel(r, "Slider Step 1 6000", "Slider Amp 1 4000", "Frame 500 500");
	}
	
	static void testColorTolerence() {
		BufferedImage cat = SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg").FileToImg().img();
		SourceOperations.imgSource(cat).map(img->{
			return ToleranceColor.applyMap(ToleranceColor.mostColMap(100, img), img);
		}).showImage();
	}
	
	static void testPixelChangeOperation() {
		SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
		.FileToImg()
		.singlePixelOperation(p->{
			p.changeRGB("g", 100);
			return p;
			}
		)
		.showImage();
	}
	
	static void testPixelizeWithGroups() {
		SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
		.FileToImg()
		.pixelOperation(100,100, group->{
			Color avgCol = group.getAvgColor();
			List<Pixel> changes = group.getPixels().stream().map(p->{
				p.setColor(avgCol);
				return p;
			}).collect(Collectors.toList());
			PixelGroup groupChanges = new PixelGroup(changes);
			return groupChanges;
		})
		.showImage();
		
	}
	
	static void testDelauneyTriangulation() {
		SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
		.FileToImg()
		.detectEdges(65, 150)
		.aside(x->x.showImage())
		.kernelPixelOperation(4, 4, group->{
			int hits = group.pixels.stream().mapToInt(p->{
				if(p.getColor().equals(Color.black))
					return 0;
				return 1;
			})
			.sum();
			group.pixels.stream().forEach(p->p.setColor(Color.black));
			if(hits>6) {
				 group.getPixels().get(8).setColor(Color.white);
			}
			return group;
		})
		.toPixelList()
		.aside(lst->lst.buildOperation(OperationBuilders.PixelListToImage()).showImage())
		.filter(p->p.color==Color.WHITE.getRGB())
		.map((Function<Pixel,imageOp.Point>)(p->new imageOp.Point(p.x, p.y)))
		.buildOperation(points -> new ListOperation<Triangle>(Geometry.delanyTriangulationV2(points)))
		.map(t->(IDrawable)t)
		.buildOperation(OperationBuilders.GeometryToImage(474, 842))
		.showImage();
	}
	


static class ControlPanel extends JFrame{
	public ControlPanel(Consumer<List<Object>> c, String ... controllers) {
		super("Control Panel");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		List<Slider> sliders = new ArrayList<Slider>();
		List<JFrame> frames = new ArrayList<JFrame>();
		JPanel pnl = new JPanel();
		for(String controller: controllers) {
			if(controller.substring(0, 6).equals("Slider")) {
				String[] settings = controller.split(" ");
				Slider s = new Slider();
				s.slid.setMinimum(Integer.parseInt(settings[2]));
				s.slid.setMaximum(Integer.parseInt(settings[3]));
				s.label.setText(settings[1]);
				sliders.add(s);
				pnl.add(s);
			}
			if(controller.substring(0, 5).equals("Frame")) {
				ImageFrame frame = new ImageFrame();
				String[] settings = controller.split(" ");
				frame.setSize(Integer.parseInt(settings[1]), Integer.parseInt(settings[2]));
				frame.setVisible(true);
				frames.add(frame);
			}
		}
		JButton activateBtn = new JButton("Reload");

		activateBtn.addActionListener((x)->{
			List<Object> values = new ArrayList<>();
			//TODO make it not matter what order Controls are in (right now it needs to be sliders then frames)
			for(Slider s: sliders) {
				values.add(s.getValue());
			}
			for(JFrame f: frames) {
				values.add(f);
			}
			c.accept(values);
		});
		pnl.add(activateBtn);
		add(pnl);
		setVisible(true);
	}
}

static class Slider extends JPanel{
	JSlider slid;
	JTextField field;
	JLabel label;
	public Slider() {
		setSize(300,100);
		setLayout(new GridLayout(1,3));
		JPanel labelPnl = new JPanel();
		label = new JLabel("Test");
		label.setBackground(Color.red);
		labelPnl.add(label);
		slid = new JSlider();
		slid.setMinimum(1);
		slid.setMaximum(100);
		slid.addChangeListener(this::moveSlid);
		field = new JTextField();
		field.addActionListener(this::changeField);
		add(labelPnl);
		add(slid);
		add(field);
	}
	
	private void moveSlid(ChangeEvent e) {
		field.setText(Integer.toString(slid.getValue()));
	}
	
	private void changeField(ActionEvent e) {
		slid.setValue(Integer.parseInt(field.getText()));
	}
	
	public int getValue() {
		return slid.getValue();
	}
	
}

static class ImageFrame extends JFrame{
	public ImagePanel pnl;
	public ImageFrame() {
		pnl = new ImagePanel();
		add(pnl);
	}
	public void switchImage(BufferedImage img) {
		pnl.switchImage(img);
	}
	
	
}
}

