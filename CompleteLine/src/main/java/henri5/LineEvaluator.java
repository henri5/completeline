package henri5;

import henri5.LineCompleter.Action;

public class LineEvaluator {
  private static final String STUFF_NAME = "[A-Za-z0-9_]+"; // TODO go crazy and generate regex for allowed identifier?
  private static final String CLASS_NAME = STUFF_NAME;
  private static final String CLASS_NAME_INNER = String.format("%1$s(\\.%1$s)*", CLASS_NAME);
  private static final String METHOD_NAME = STUFF_NAME;
  private static final String PARAMETER_NAME = STUFF_NAME;
  private static final String METHOD_PARAM = String.format("%1$s %2$s", CLASS_NAME_INNER, PARAMETER_NAME);
  private static final String METHOD_PARAMS = String.format("%1$s(,[ ]?%1$s)*", METHOD_PARAM);
  private static final String THROWS = String.format("throws %1$s(,[ ]?%1$s)*", CLASS_NAME_INNER);
  private static final String INDENTATION = "[ \t]*";
  private static final String ANYTHING_WITH_SPACE_OR_INTENTATION = String.format("(.* |%1$s)", INDENTATION);
  
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
    if (matches(line, "^%1$s(if|else if|catch|while|for|synchronized|switch)[ ]?$", INDENTATION)) {
      return true;
    }
    if (matches(line, "^%1$s}[ ]?else if[ ]?$", INDENTATION)) {
      return true;
    }
    return false;
  }

  private static boolean canInsertCurlyBrackets(String line) {
    // for try/else/finally/..
    if (matches(line, "^%1$s(else|finally|try|do)[ ]?$", ANYTHING_WITH_SPACE_OR_INTENTATION)) {
      return true;
    }
    // for if/catch/while/for..
    if (matches(line, "^%1$s(if|catch|while|for|synchronized|switch)[ ]?\\(.*\\)[ ]?$", ANYTHING_WITH_SPACE_OR_INTENTATION)) {
      return true;
    }
    // for return statements
    if (matches(line, "^%1$sreturn .*[^;]$", INDENTATION)) {
      return false;
    }
    // for non-abstract method declaration
    if (!matches(line, "^(.*? |%1$s)?abstract .*$", INDENTATION) && matches(line, "^[^=,]*?[ ]?(?<!(new|throw| |\t|:|do))[ ]%1$s\\(.*\\)[ ]?(%2$s)?[ ]?$", METHOD_NAME, THROWS)) {
      return true;
    }
    // for constructor with parameters declaration
    if (matches(line, "^%1$s((public|protected|private) )?%2$s\\(%3$s\\)[ ]?(%4$s)?[ ]?$", INDENTATION, CLASS_NAME, METHOD_PARAMS, THROWS)) {
      return true;
    }
    // for class/interface/enum declaration
    if (matches(line, "^%1$s(class|interface|enum) .*[^{]$", ANYTHING_WITH_SPACE_OR_INTENTATION)) {
      return true;
    }
    return false;
  }

  private static boolean canInsertColon(String line) {
    // for case keyword
    if (matches(line, "^%1$scase[ ][^:]+$", INDENTATION)) {
      return true;
    }
    // for default keyword
    if (matches(line, "^%1$sdefault$", INDENTATION)) {
      return true;
    }
    return false;
  }

  private static boolean canInsertSemicolon(String line) {
    // for annotations
    if (matches(line, "^%1$s\\@%2$s(\\(.*\\))?$", INDENTATION, CLASS_NAME_INNER)) {
      return false;
    }
    // nothing meaningful
    if (matches(line, "^%1$s[}]?$", INDENTATION)) {
      return false;
    }
    // single line comment
    if (matches(line, "^%1$s//.*$", INDENTATION)) {
      return false;
    }
    // case keyword
    if (matches(line, "^%1$scase[ ][^:]+:$", INDENTATION)) {
      return false;
    }
    // default keyword
    if (matches(line, "^%1$sdefault:$", INDENTATION)) {
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
  
  private static boolean matches(String line, String regex, String... replace) {
    return line.matches(String.format(regex, (Object[]) replace));
  }
}
