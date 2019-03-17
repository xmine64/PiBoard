package com.mmdmine.board;

import javax.microedition.lcdui.*;
import com.nokia.mid.ui.*;

public class Keyboard extends Canvas implements KeyboardVisibilityListener {
	private final char[] NUM_COL1 = new char[] { '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0' };
	private final char[] NUM_COL2 = new char[] { '/', '*', '-', '+', '=', '$',
			'%', '@', '(', ')' };
	private final char[] NUM_COL3 = new char[] { '\\', '-', '<', '>', '\'',
			'"', ';', ':', '[', ']' };
	private final char[] NUM_COL4 = new char[] { '|', '!', '?', '_', ',',
			'\u00ab', '\u00bb', '\u061b', '{', '}' };
	private final char[] ABC_COL1 = new char[] { 'ض', 'ص', 'ق', 'ف', 'غ', 'ع',
			'خ', 'ح', 'ج', 'چ' };
	private final char[] ABC_COL2 = new char[] { 'ش', 'س', 'ی', 'ب', 'ل', 'ا',
			'ت', 'ن', 'م', 'ک' };
	private final char[] ABC_COL3 = new char[] { 'ظ', 'ط', 'ز', 'ر', 'ذ', 'د',
			'و', 'ه', 'ة', 'گ' };
	private final char[] ABC_COL4 = new char[] { '؟', '!', 'ژ', 'ئ', 'أ', 'آ',
			'ء', 'ث', 'پ', 'ـ' };
	private final char[] OPTS = new char[] { '\t', '.', '\u060c', ' ', '\b',
			'\n' };

	public final int KB_MARGIN = 5;
	public final int BTN_MARGIN = 2;
	public final int BTN_WIDTH = 23;
	public final int BTN_HEIGHT = 34;

	public final int MARGIN = 6;
	private boolean isNum = false;
	private TextEditor te = null;

	private Display display;
	
	public Keyboard(Display dis) {
		te = TextEditor.createTextEditor(240, TextField.ANY, getWidth()
				- (MARGIN * 2), 3);
		te.setParent(this);
		te.setVisible(true);
		te.setPosition(MARGIN, 0);
		display = dis;
	}

	protected void pointerPressed(int x, int y) {
		super.pointerPressed(x, y);
		if (visible) {
			if (y > getHeight() - (BTN_HEIGHT * 5) - KB_MARGIN) {
				if (x > KB_MARGIN && x < getWidth() - KB_MARGIN) {
					char c = findBtn(x, y);
					switch (c) {
					case '\0':
						break;
					case '\b':
						if (te.getContent().length() > 0)
							te.delete(te.getContent().length() - 1, 1);
						break;
					case '\t':
						isNum = !isNum;
						break;
					default:
						te.insert("" + c, te.getContent().length());
						break;
					}
				}
			} else {
				visible = false;
				te.setFocus(true);
			}
		} else {
			if (te.hasFocus()) {
				te.setFocus(false);
			} else {
				visible = true;
			}
		}
		repaint();
	}

	private boolean visible = true;

	public void paint(Graphics g) {
		g.setColor(display.getColor(Display.COLOR_BACKGROUND));
		g.fillRect(0, 0, getWidth(), getHeight());
		if (!visible)
			return;
		if (isNum) {
			paintCol(g, NUM_COL1, getHeight() - (BTN_HEIGHT * 5) - KB_MARGIN);
			paintCol(g, NUM_COL2, getHeight() - (BTN_HEIGHT * 4) - KB_MARGIN);
			paintCol(g, NUM_COL3, getHeight() - (BTN_HEIGHT * 3) - KB_MARGIN);
			paintCol(g, NUM_COL4, getHeight() - (BTN_HEIGHT * 2) - KB_MARGIN);
		} else {
			paintCol(g, ABC_COL1, getHeight() - (BTN_HEIGHT * 5) - KB_MARGIN);
			paintCol(g, ABC_COL2, getHeight() - (BTN_HEIGHT * 4) - KB_MARGIN);
			paintCol(g, ABC_COL3, getHeight() - (BTN_HEIGHT * 3) - KB_MARGIN);
			paintCol(g, ABC_COL4, getHeight() - (BTN_HEIGHT * 2) - KB_MARGIN);
		}
		paintOpts(g, getHeight() - BTN_HEIGHT - KB_MARGIN);
	}

	private void paintKey(Graphics g, String c, int x, int y, int width,
			int height) {
		g.setColor(0xFF353535);
		g.fillRoundRect(x + BTN_MARGIN, y + BTN_MARGIN, width - BTN_MARGIN,
				height - BTN_MARGIN, 4, 6);
		g.setColor(0xFFFFFF);
		g.drawString(c, x + (width / 2) - (g.getFont().stringWidth(c) / 2), y
				+ (height / 2) - (g.getFont().getHeight() / 2), Graphics.LEFT
				| Graphics.TOP);
	}

	private void paintCol(Graphics g, char[] chars, int y) {
		for (int i = 0; i < chars.length; i++) {
			paintKey(g, "" + chars[i], (i * BTN_WIDTH) + KB_MARGIN, y,
					BTN_WIDTH, BTN_HEIGHT);
		}
	}

	private void paintOpts(Graphics g, int y) {
		int x = KB_MARGIN;
		for (int i = 0; i < OPTS.length; i++) {
			switch (OPTS[i]) {
			case ' ':
				paintKey(g, "", x, y, BTN_WIDTH * 4, BTN_HEIGHT);
				x += (BTN_WIDTH * 4);
				break;
			case '\n':
				paintKey(g, "\u21B2", x, y, BTN_WIDTH, BTN_HEIGHT);
				x += BTN_WIDTH;
				break;
			case '\b':
				paintKey(g, "\u2190", x, y, BTN_WIDTH * 2, BTN_HEIGHT);
				x += BTN_WIDTH * 2;
				break;
			case '\t':
				paintKey(g, "\u21B9", x, y, BTN_WIDTH, BTN_HEIGHT);
				x += BTN_WIDTH;
				break;
			default:
				paintKey(g, "" + OPTS[i], x, y, BTN_WIDTH, BTN_HEIGHT);
				x += BTN_WIDTH;
				break;
			}
		}
	}

	private char findBtn(int x, int y) {
		int i = (x - KB_MARGIN) / BTN_WIDTH;
		if (y >= getHeight() - (BTN_HEIGHT * 5) - KB_MARGIN
				&& y < getHeight() - (BTN_HEIGHT * 4) - KB_MARGIN) {
			return isNum ? NUM_COL1[i] : ABC_COL1[i];
		}
		if (y >= getHeight() - (BTN_HEIGHT * 4) - KB_MARGIN
				&& y < getHeight() - (BTN_HEIGHT * 3) - KB_MARGIN) {
			return isNum ? NUM_COL2[i] : ABC_COL2[i];
		}
		if (y >= getHeight() - (BTN_HEIGHT * 3) - KB_MARGIN
				&& y < getHeight() - (BTN_HEIGHT * 2) - KB_MARGIN) {
			return isNum ? NUM_COL3[i] : ABC_COL3[i];
		}
		if (y >= getHeight() - (BTN_HEIGHT * 2) - KB_MARGIN
				&& y < getHeight() - BTN_HEIGHT - KB_MARGIN) {
			return isNum ? NUM_COL4[i] : ABC_COL4[i];
		}
		if (y >= getHeight() - BTN_HEIGHT - KB_MARGIN
				&& y < getHeight() - KB_MARGIN) {
			if (i < 3)
				return OPTS[i];
			if (i >= 3 && i < 7)
				return ' ';
			if (i > 6 && i < 9)
				return '\b';
			if (i == 9)
				return '\n';
		}
		return '\0';
	}

	public String getContent() {
		return te.getContent();
	}
	
	public void clear() {
		te.setContent("");
	}

	public void hideNotify(int keyboardCategory) {
		visible = true;
		if (te.hasFocus())
			te.setFocus(false);
		repaint();
	}

	public void showNotify(int keyboardCategory) {
		visible = false;
		if (!te.hasFocus())
			te.setFocus(true);
		repaint();
	}

	public int getKY() {
		return getHeight() - (BTN_HEIGHT * 5) - KB_MARGIN;
	}
}