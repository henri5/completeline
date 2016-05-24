package henri5;

import org.eclipse.swt.custom.StyledText;

public class CompleteLineCommandAddLine extends CompleteLineCommand {
  
  @Override
  void doLineCompletion(StyledText text) {
    LineCompleter.completeLine(text, true);
  }
}
