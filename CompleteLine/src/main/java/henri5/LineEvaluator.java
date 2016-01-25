package henri5;

import henri5.LineCompleter.Action;

public class LineEvaluator {
  
  public static Action getAction(String line) {
    if (canInsertBrackets(line)) {
      return Action.BRACKETS;
    }
    else if (canInsertCurlyBrackets(line)) {
      return Action.CURLY_BRACKETS;
    }
    else if (canInsertColon(line)) {
      return Action.COLON;
    }
    else if (canInsertSemicolon(line)) {
      return Action.SEMICOLON;
    }
    return Action.NEW_LINE;
  }
  
  private static boolean canInsertBrackets(String line) {
    // for if/catch/while/for.. without brackets
    if (line.matches("^[ \t]*(if|else if|catch|while|for|synchronized|switch)[ ]?$")) {
      return true;
    }
    return false;
  }

  private static boolean canInsertCurlyBrackets(String line) {
    // for try/else/finally/..
    if (line.matches("^.*(?<![A-Za-z0-9_])(else|finally|try|do)[ ]?$")) {
      return true;
    }
    // for if/catch/while/for..
    if (line.matches("^.*(?<![A-Za-z0-9_])(if|catch|while|for|synchronized|switch)[ ]?\\(.*\\)[ ]?$")) {
      return true;
    }
    // for return statements
    if (line.matches("^[ \t]*return .*[^;]$")) {
      return false;
    }
    // for method declaration
    if (line.matches("^[^=,]*?[ ]?(?<!(new|throw| |\t|:|do))[ ][A-Za-z0-9_]+?\\(.*\\)[ ]?(throws [A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*(,[ ]?[A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*)*)?[ ]?$")) {
      return true;
    }
    // for class/interface/enum declaration
    if (line.matches("^.*(?<![A-Za-z0-9_])(class|interface|enum) .*[^{]$")) {
      return true;
    }
    return false;
  }

  private static boolean canInsertColon(String line) {
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

  private static boolean canInsertSemicolon(String line) {
    // for annotations
    if (line.matches("^[ \t]*\\@[A-Za-z0-9_]+(\\.[A-Za-z0-9_]+)*(\\(.*\\))?$")) {
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
    if (line.endsWith("{") && Action.CURLY_BRACKETS.equals(getAction(line.substring(0, line.length() - 1)))) {
      return false;
    }
    // does not end with semicolon
    if (!line.matches(".*;[^;)\"]*$")) {
      return true;
    }
    return false;
  }
}
