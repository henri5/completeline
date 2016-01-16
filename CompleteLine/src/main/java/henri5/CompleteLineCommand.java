package henri5;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

public class CompleteLineCommand implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {}

	public void dispose() {}

	public Object execute(ExecutionEvent event) throws ExecutionException {
	  Control control = getEditorControl();
	  if (control != null) {
      if (control instanceof StyledText) {
        new LineCompleter((StyledText) control).completeLine();
      }
	  }
		return null;
	}

	public boolean isEnabled() {
		return getEditorControl() != null;
	}
	
	private Control getEditorControl() {
    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (activeWorkbenchWindow == null) {
      return null;
    }
    IEditorPart activeEditor = activeWorkbenchWindow.getActivePage().getActiveEditor();
    if (activeEditor instanceof AbstractDecoratedTextEditor) {
      AbstractDecoratedTextEditor editor = (AbstractDecoratedTextEditor) activeEditor;
      return (Control) editor.getAdapter(Control.class);
    }
    return null;
	}

	public boolean isHandled() {
		return isEnabled();
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {}
}
