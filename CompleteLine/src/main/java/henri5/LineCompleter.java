package henri5;

import org.eclipse.swt.custom.StyledText;

public class LineCompleter {
  private final StyledText styledText;
  private final String EOL;

  public LineCompleter(StyledText styledText) {
    this.styledText = styledText;
    EOL = styledText.getLineDelimiter();
  }

  protected void completeLine() {
    final String line = getLineText(getCurrentCaretLine());
    if (LineEvaluator.canInsertCurlyBrackets(line)) {
      String intentation = getIntentation(line);
      trimEnding();
      insert(" {" + EOL + EOL + intentation + "}");
      goToNextLine();
      insert(intentation); // IDE automatically actually adds extra intentation
    }
    else if (LineEvaluator.canInsertColon(line)) {
      trimEnding();
      insert(":");
    }
    else if (LineEvaluator.canInsertSemicolon(line)) {
      trimEnding();
      insert(";");
    }
    else {
      // just insert new line
      if (isNextLineEmpty()) {
        goToNextLine();
        trimEnding();
      }
      else {
        goToEndOfCurrentLine();
        insert(EOL);
        goToNextLine();
      }
    }
    goToEndOfCurrentLine();
  }

  private void trimEnding() {
    int currentLineIndex = getCurrentCaretLine();
    String currentLine = getLineText(currentLineIndex);
    String currentLineTrimmedEnd = getIntentation(currentLine) + currentLine.trim();
    styledText.replaceTextRange(getLineOffsetPosition(currentLineIndex), currentLine.length(), currentLineTrimmedEnd);
  }

  private boolean isNextLineEmpty() {
    if (getCurrentCaretLine() == styledText.getLineCount()) { // indexOutOfBounds
      return false;
    }

    String line = getLineText(getCurrentCaretLine() + 1);
    if (!line.trim().isEmpty()) {
      // there must've been at least one non-whitespace char
      return false;
    }
    return true;
  }

  private void insert(String string) {
    styledText.insert(string);
  }

  private String getIntentation(String line) {
    StringBuilder prefix = new StringBuilder();
    for (int i = 0; i < line.length(); i++) {
      char charAt = line.charAt(i);
      if (Character.isWhitespace(charAt)) {
        prefix.append(charAt);
      }
      else {
        break;
      }
    }
    return prefix.toString();
  }

  private void goToEndOfCurrentLine() {
    String line = getLineText(getCurrentCaretLine());
    styledText.setCaretOffset(getLineOffsetPosition(getCurrentCaretLine()) + line.length());
  }

  private void goToNextLine() {
    styledText.setCaretOffset(getLineOffsetPosition(getCurrentCaretLine() + 1));
  }

  private String getLineText(int lineNumber) {
    return styledText.getLine(lineNumber);
  }

  private int getLineOffsetPosition(int lineNumber) {
    return styledText.getOffsetAtLine(lineNumber);
  }

  private int getCurrentCaretLine() {
    return styledText.getLineAtOffset(getCaretPosition());
  }

  private int getCaretPosition() {
    return styledText.getCaretOffset();
  }
}
