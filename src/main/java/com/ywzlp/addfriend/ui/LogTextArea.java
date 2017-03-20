package com.ywzlp.addfriend.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * @author yuwei
 *
 */
public class LogTextArea extends JTextArea {

	private static final long serialVersionUID = -141366416864091083L;

	private PrintStream printStream;

	protected LogTextArea() {
		printStream = new PrintStream(new MyOutputStream());
		System.setOut(printStream);
		System.setErr(printStream);
		super.setLineWrap(true);
	}

	public class MyOutputStream extends OutputStream {

		@Override
		public void write(byte data[]) throws IOException {
			LogTextArea.this.append(new String(data));
		}
		
		@Override
		public void write(byte data[], int off, int len) throws IOException {
			LogTextArea.this.append(new String(data, off, len));
			LogTextArea.this.setCaretPosition(LogTextArea.this.getText().length());
		}

		@Override
		public void write(int b) throws IOException {
			//ignore
		}
	}
}
