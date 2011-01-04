package qc.gui;

import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;

public class GuiOutputStream extends OutputStream {
	private TextArea _text;
	private byte _c;
	private boolean _b = false;

	public GuiOutputStream(TextArea text) {
		_text = text;
	}

	@Override
	public void write(int i) throws IOException {
		try {
			byte c = (byte) i;
			if (_b) {
				_b = false;
				byte[] bs = new byte[2];
				bs[0] = _c;
				bs[1] = c;
				_text.append(new String(bs));
			} else if (c > 0) {
				_b = false;
				_text.append(String.valueOf((char) c));
			} else {
				_b = true;
				_c = c;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
