package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

public class ControlUtil {

	public static KeyListener getKeyListener(char key, Runnable a) {
		return new CustomKeyListener(key, a);
	}
	
	public static KeyListener getConsumerKeyListener(Consumer<KeyEvent> c) {
		return new ConsumerKeyListener(c);
	}
}

class ConsumerKeyListener implements KeyListener{
	Consumer<KeyEvent> c;
	public ConsumerKeyListener(Consumer<KeyEvent> c) {
		this.c = c;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		c.accept(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class CustomKeyListener implements KeyListener {
	char k;
	Runnable a;
	public CustomKeyListener(char k, Runnable a) {
		this.k = k;
		this.a = a;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar()==k) {
			a.run();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
