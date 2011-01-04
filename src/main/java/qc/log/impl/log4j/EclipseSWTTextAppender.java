/**
 * 
 */
package qc.log.impl.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.swt.widgets.Text;

/**
 * 扩展AppenderSkeleton，实现append方法即可
 * 
 * @author dragon
 * 
 */
public class EclipseSWTTextAppender extends AppenderSkeleton {
	protected Text comp; // 用来展现log信息的gui组件

	public EclipseSWTTextAppender() {
	}

	public EclipseSWTTextAppender(Layout layout) {
		setLayout(layout);
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	@Override
	public void append(LoggingEvent event) {
		final String text = this.layout.format(event);
		if (comp != null && !comp.isDisposed()) {
			comp.getDisplay().syncExec(new Runnable() {
				public void run() {
					comp.append(text);
				}
			});
		}
	}

	@Override
	public void close() {
		reset();
	}

	public void reset() {
		if (comp != null && !comp.isDisposed()){
			comp.getDisplay().syncExec(new Runnable() {
				public void run() {
					comp.setText("");
				}
			});
		}
	}

	public Text getControl() {
		return this.comp;
	}

	public void setControl(Text comp) {
		this.comp = comp;
	}
}
