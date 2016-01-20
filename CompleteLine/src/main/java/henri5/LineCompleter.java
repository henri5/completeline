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
    if (canInsertCurlyBrackets(line)) {
      String intentation = getIntentation(line);
      trimEnding();
      insert(" {" + EOL + EOL + intentation + "}");
      goToNextLine();
      insert(intentation); // IDE automatically actually adds extra intentation
    }
    else if (canInsertColon(line)) {
      trimEnding();
      insert(":");
    }
    else if (canInsertSemicolon(line)) {
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

  public static boolean canInsertCurlyBrackets(String line) {
    // for try/else/finally/..
    if (line.matches("^.*(?<![A-Za-z0-9])(else|finally|try|do)[ ]?$")) {
      return true;
    }
    // for if/catch/while/for..
    if (line.matches("^.*(?<![A-Za-z0-9])(if|catch|while|for|synchronized|switch)[ ]?\\(.*\\)[ ]?$")) {
      return true;
    }
    // for return statements
    if (line.matches("^[ \t]*return .*[^;]$")) {
      return false;
    }
    // for method declaration
    if (line.matches("^[^=,]*?[ ]?(?<!(new|throw| |\t|:))[ ][A-Za-z0-9]+?\\(.*\\)[ ]?(throws [A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(,[ ]?[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*)*)?[ ]?$")) {
      return true;
    }
    // for class/interface/enum declaration
    if (line.matches("^.*(?<![A-Za-z0-9])(class|interface|enum) .*[^{]$")) {
      return true;
    }
    return false;
  }

  public static boolean canInsertColon(String line) {
    // actually curly brackets
    if (canInsertCurlyBrackets(line)) {
      return false;
    }
    // for case keyword
    if (line.matches("^[ \t]*case[ ][^:]+$")) {
      return true;
    }
    // for default keyword
    if (line.matches("^[ \t]*default$")) {
      return true;
    }
    return false;
  }
  
  public static boolean canInsertSemicolon(String line) {
    // also check other options
    if (canInsertCurlyBrackets(line) ||canInsertColon(line)) {
      return false;
    }
    // for annotations
    if (line.matches("^[ \t]*\\@.*$")) {
      return false;
    }
    // nothing meaningful
    if (line.matches("^[ \t]*[}]?$")) {
      return false;
    }
    // single line comment
    if (line.matches("^[ \t]*//.*$")) {
      return false;
    }
    // case keyword
    if (line.matches("^[ \t]*case[ ][^:]+:$")) {
      return false;
    }
    // default keyword
    if (line.matches("^[ \t]*default:$")) {
      return false;
    }
    // we added curly brackets ourselves
    if (line.endsWith("{") && canInsertCurlyBrackets(line.substring(0, line.length() - 1))) {
      return false;
    }
    // does not end with semicolon
    if (!line.matches(".*;[^;)\"]*$")) {
      return true;
    }
    return false;
  }
}
