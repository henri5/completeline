package henri5;

import org.eclipse.swt.custom.StyledText;

public class LineCompleter {
  private final StyledText styledText;
  private final String EOL;

  private LineCompleter(StyledText styledText) {
    this.styledText = styledText;
    EOL = styledText.getLineDelimiter();
  }

  protected static void completeLine(StyledText styledText) {
    final String line = styledText.getLine(styledText.getLineAtOffset(styledText.getCaretOffset()));
    Action action = LineEvaluator.getAction(line);
    action.run(styledText);
  }
  
  private void addBracket() {
    final String line = getLineText(getCurrentCaretLine());
    String intentation = getIntentation(line);
    trimEnding();
    goToEndOfCurrentLine();
    insert(" () {" + EOL + intentation + "}");
    moveCaret(" (".length()); 
  }
  
  private void addCurlyBracket() {
    final String line = getLineText(getCurrentCaretLine());
    String intentation = getIntentation(line);
    trimEnding();
    insert(" {" + EOL + EOL + intentation + "}");
    goToNextLine();
    insert(intentation); // IDE automatically actually adds extra indentation
    goToEndOfCurrentLine();
  }
  
  private void addColon() {
    trimEnding();
    insert(":");
    goToEndOfCurrentLine();
  }
  
  private void addSemiColon() {
    trimEnding();
    insert(";");
    goToEndOfCurrentLine();
  }

  private void addNewLine() {
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

  private void moveCaret(int delta) {
    styledText.setCaretOffset(getCaretPosition() + delta);
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

  public enum Action {
    BRACKETS(new LineCompleterAction() {

      @Override
      public void run(LineCompleter completer) {
        completer.addBracket();
      }

    }),
    CURLY_BRACKETS(new LineCompleterAction() {

      @Override
      public void run(LineCompleter completer) {
        completer.addCurlyBracket();
      }

    }),
    COLON(new LineCompleterAction() {

      @Override
      public void run(LineCompleter completer) {
        completer.addColon();
      }

    }),
    SEMICOLON(new LineCompleterAction() {

      @Override
      public void run(LineCompleter completer) {
        completer.addSemiColon();
      }

    }),
    NEW_LINE(new LineCompleterAction() {

      @Override
      public void run(LineCompleter completer) {
        completer.addNewLine();
      }

    });

    private final LineCompleterAction action;

    Action(LineCompleterAction action) {
      this.action = action;
    }

    private void run(StyledText text) {
      action.run(new LineCompleter(text));
    }
  }

  private interface LineCompleterAction {
    void run(LineCompleter completer);
  }
}
