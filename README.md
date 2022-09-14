<h1>ImageOp</h1>
This is a library that I wrote for performing Image Operations. I intended it to be easily integrated with the core java functional interfaces/ streams library.

Please note that I did not write the Perlin Noise, GifSequenceWriter, or the Delauney Triangle code. The real authors are listed in the code.

For examples on how to use it look that the Data2Test class.

Some example of things that it can do:

<h2>Canny Edge Detection with Gif Generation</h2>

![alt text](https://github.com/RylanSanders/ImageOp/blob/main/ReadMeImages/example.gif?raw=true)

Code:
	<pre lang="java"> <code>
	BufferedImage cat = SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg").FileToImg().img();
	SourceOperations.generateIntListOp(0, 600, x->x+10) //Generate the different parameters to use
	<pre>	.monitor(x->System.out.println(x)) //Status update - print out the parameter that is being used
	<pre>	.map(num->
	<pre>			SourceOperations.imgSource(cat)
	<pre>			.maskComposite(
	<pre>					SourceOperations.imgSource(cat)
	<pre>					.detectEdges(1, 600-num)
	<pre>					.img()
	<pre>					) //Maps the parameter to the actual image
	<pre>			.img()
	<pre>			)
	<pre>	.consume(x->GifUtil.generateGif(x, "example.gif", 25));  //Generates the gif off over the list of images
	</pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></code></pre>

<h2>Pixelization with a control panel for testing different input parameters:</h2>       

![alt text](https://github.com/RylanSanders/ImageOp/blob/main/ReadMeImages/PixelBook.PNG?raw=true)

![alt text](https://github.com/RylanSanders/ImageOp/blob/main/ReadMeImages/PixelImageController.PNG?raw=true)

Code:
	<pre lang="java"> <code>
`Consumer<List<Object>>` r = (arr)->SourceOperations.fileSource(exampleImgs + "BookV1.jpg")
<pre>	.FileToImg()  
    	.simpleSharpen() 
    	.tolerancePixelize((int)arr.get(0), (int)arr.get(1)) 
    	.showImage();
//This Control Panel sets up a panel to use for input values to the consumer. The strings are a very basic configuration for the panel controls 
new ControlPanel(r, "Slider Tolerance 1 255", "Slider Pix 1 1100");
	</pre></code></pre>


<h2>Delauney Triangulation for an image with edge detection and kernel operations for specifying points</h2>

![alt text](https://github.com/RylanSanders/ImageOp/blob/main/ReadMeImages/CatImages.png?raw=true)

Code:
     <pre lang="java"> <code><pre>
     SourceOperations.fileSource(exampleImgs + "cat_simple_background.jpg")
	<pre>	.FileToImg()
	<pre>	.detectEdges(65, 150) //Use Canny Edge Detection
	<pre>	.aside(x->x.showImage()) //Show the edges image but continue the operation
	<pre>	.kernelPixelOperation(4, 4, group->{
	<pre>		int hits = group.pixels.stream().mapToInt(p->{
	<pre>			if(p.getColor().equals(Color.black))
	<pre>				return 0;
	<pre>			return 1;
	<pre>		})
	<pre>		.sum();
	<pre>		group.pixels.stream().forEach(p->p.setColor(Color.black));
	<pre>		if(hits>6) {
	<pre>			 group.getPixels().get(8).setColor(Color.white);
	<pre>		}
	<pre>		return group;
	<pre>	}) //Split the Image into 4x4 kernels and if a kernal contains more than 6 white pixels then mark it as white and make all other black
	<pre>	.toPixelList()
	<pre>	.aside(lst->lst.buildOperation(OperationBuilders.PixelListToImage()).showImage()) // Show the image with the condensed points for the edges
	<pre>	.filter(p->p.color==Color.WHITE.getRGB())
	<pre>	.map((Function<Pixel,imageOp.Point>)(p->new imageOp.Point(p.x, p.y))) //Convert the white pixels to geometric points
	<pre>	.buildOperation(points -> new ListOperation<Triangle>(Geometry.delanyTriangulationV2(points))) //Use Delauney triangulation to convert the points to triangles
	<pre>	.map(t->(IDrawable)t)
	<pre>	.buildOperation(OperationBuilders.GeometryToImage(474, 842)) //Build the shapes into an image
	<pre>	.showImage();
	</pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></pre></code></pre>
