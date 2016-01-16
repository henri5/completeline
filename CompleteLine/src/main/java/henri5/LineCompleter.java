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
      goToEndOfCurrentLine();
      insert((line.endsWith(" ") ? "" : " ") + "{" + EOL + EOL + intentation + "}");
      goToNextLine();
      insert(intentation); // IDE automatically actually adds extra intentation
      goToEndOfCurrentLine();
    }
    else if (canInsertSemicolon(line)) {
      goToEndOfCurrentLine();
      insert(";" + EOL + getIntentation(line));
      goToNextLine();
      goToEndOfCurrentLine();
    }
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

  private String getLineText(int lineAtOffset) {
    return styledText.getLine(lineAtOffset);
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
    // for try/else/finally
    if (line.matches("^.*(?<![A-Za-z0-9])(else|finally|try)[ ]?$")) {
      return true;
    }
    // for if/catch/while/for
    if (line.matches("^.*(?<![A-Za-z0-9])(if|catch|while|for) \\(.*\\)[ ]?$")) {
      return true;
    }
    // for method declaration
    if (line.matches("^[^=,]*?[ ]?(?<!(new|throw))[ ][A-Za-z0-9]+?\\(.*\\)[ ]?$")) {
      return true;
    }
    // for class/interface/enum declaration
    if (line.matches("^.*(?<![A-Za-z0-9])(class|interface|enum) .*[^{]$")) {
      return true;
    }
    return false;
  }

  public static boolean canInsertSemicolon(String line) {
    // actually curly brackets
    if (canInsertCurlyBrackets(line)) {
      return false;
    }
    // for annotations
    if (line.matches("^[ \t]*\\@.*$")) {
      return false;
    }
    // nothing meaningful
    if (line.matches("^[ \t]*$")) {
      return false;
    }
    // does not end with semicolon
    if (!line.matches(".*;[^;)\"]*$")) {
      return true;
    }
    return false;
  }
}
