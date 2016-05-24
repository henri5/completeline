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

  @Override
  public void addHandlerListener(IHandlerListener handlerListener) {
  }

  @Override
  public void dispose() {
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Control control = getEditorControl();
    if (control instanceof StyledText) {
      doLineCompletion((StyledText) control);
    }
    return null;
  }
  
  void doLineCompletion(StyledText text) {
    LineCompleter.completeLine(text);
  }

  @Override
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

  @Override
  public boolean isHandled() {
    return isEnabled();
  }

  @Override
  public void removeHandlerListener(IHandlerListener handlerListener) {
  }
}
