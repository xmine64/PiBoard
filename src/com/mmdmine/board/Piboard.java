package com.mmdmine.board;

import javax.microedition.content.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import com.nokia.mid.ui.*;

public class Piboard extends MIDlet implements CommandListener {

	public Piboard() {
		display = Display.getDisplay(this);
		keyboard = new Keyboard(display);
		keyboard.addCommand(sendCommand);
		keyboard.addCommand(searchCommand);
		keyboard.addCommand(exitCommand);
		keyboard.setCommandListener(this);
	}

	protected void destroyApp(boolean arg0) {

	}

	protected void pauseApp() {

	}

	protected void startApp() {
		if (display.getCurrent() == null) {
			VirtualKeyboard.setVisibilityListener(keyboard);
			display.setCurrent(keyboard);
		}
	}

	private final Command exitCommand = new Command("خروج", Command.EXIT, 1);
	private final Command sendCommand = new Command("ارسال", Command.OK, 1);
	private final Command searchCommand = new Command("جستجو در گوگل", Command.OK, 2);
	private final Keyboard keyboard;
	private final Display display;

	public void commandAction(Command command, Displayable d) {
		if (command == exitCommand) {
			notifyDestroyed();
		} else if (command == sendCommand) {
			share(keyboard.getContent());
			keyboard.clear();
		} else if (command == searchCommand) {
			searchInOperaMini(keyboard.getContent());
			notifyDestroyed();
		}
	}

	private boolean share(String text) {
		try {
			if (text == null | text.length() < 1)
				throw new Exception("متنی وارد نشده است");
			final Registry registry = Registry.getRegistry(this.getClass()
					.getName());
			Invocation invocation = new Invocation(null, "text/plain",
					"com.nokia.share");
			invocation.setAction("share");
			invocation.setArgs(new String[] { "text=" + text });
			registry.invoke(invocation);
			return false;
		} catch (Exception e) {
			display.setCurrent(new Alert("خطا", e.getMessage(), null, AlertType.ERROR));
		}
		return false;
	}
	
	private void searchInOperaMini(String text) {
		try {
			if (text == null | text.length() < 1)
				throw new Exception("متنی وارد نشده است");
			final Registry registry = Registry.getRegistry(this.getClass()
					.getName());
			Invocation invocation = new Invocation(text, "text/plain",
					"com.nokia.browser");
			registry.invoke(invocation);
		} catch (Exception e) {
			display.setCurrent(new Alert("خطا", e.getMessage(), null, AlertType.ERROR));
		}
	}
}
