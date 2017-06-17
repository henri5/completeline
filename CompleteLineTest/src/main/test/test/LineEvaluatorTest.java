package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import henri5.LineEvaluator;
import henri5.LineCompleter.Action;

public class LineEvaluatorTest {

  @Test
  public void testSemiColonFunctionCalls() {
    assertSemiColon("new foo()");
    assertSemiColon("new foo(bar, baz)");
    assertSemiColon("Foo.bar(bat())");
    assertSemiColon("Foo.bar(baz, bat())");
    assertSemiColon("foo()");
    assertSemiColon("  foo(bar)");
    assertSemiColon(")}");
    assertSemiColon("(Foo bar).foo()");
  }

  @Test
  public void testSemiColonFieldDeclaration() {
    assertSemiColon("int i");
    assertSemiColon("i++");
    assertSemiColon("int i = asd");
    assertSemiColon("String s = \"endOfLine\"");
    assertSemiColon("String s = \"endOfLine;\"");
    assertSemiColon("String s = \"endOfLine;\".toString()");
    assertSemiColon("String at = \"@\"");
    assertSemiColon("Foo foo = (Foo) bar");
    assertSemiColon("@Foo Foo foo");
    assertSemiColon("@Foo.Bar Foo.Bar foo");
    assertSemiColon("@Foo Foo foo = foo()");
    assertSemiColon("Möclass foo");
  }

  @Test
  public void testSemiColonMatchMisc() {
    assertSemiColon("throw foo()");
    assertSemiColon("throw foo");
    assertSemiColon("import foo.bar");
    assertSemiColon("import static foo.bar.Baz");
    assertSemiColon("if (true) return false");
    assertSemiColon(" if (foo()) return bar");
    assertSemiColon("for (int i = 0; i < 100; i++) j++");
    assertSemiColon("case Foo.BAR: return");
    assertSemiColon("default: return foo");
    assertSemiColon("default: foo()");
    assertSemiColon("return foo()");
    assertSemiColon("return Foo.Bar.foo(\"foo\", bar, new Foo())");
    assertSemiColon("return \"foo\"");
    assertSemiColon("return 1");
    assertSemiColon("return foo() || bar");
    assertSemiColon("return bar && foo()");
    assertSemiColon("do foo()");
    assertSemiColon("protected abstract void foo()");
    assertSemiColon("abstract void foo()");
    assertSemiColon("\t\tabstract void foo()");
    assertSemiColon("public abstract Foo.Bar foo() throws Baz");
    // assertTrue(s(" if (true) bar()")); wishful thinking
  }

  @Test
  public void testCurlyBracketMethodDeclaration() {
    assertCurlyBrackets("public static void foo()");
    assertCurlyBrackets("Foo.bar foo()");
    // impossible to know if method call or constructor declaration with regex. Could guess
    // by checking if first letter is capital letter as normal people have, but might be a
    // stretch
//    assertTrue(c("Foo()")); 
    assertCurlyBrackets("public Foo()");
    assertCurlyBrackets("Foo(Bar bar)");
    assertCurlyBrackets("Foo(Bar bar, Baz baz)");
    assertCurlyBrackets("public Foo(Bar bar, Baz baz)");
    assertCurlyBrackets("@Foo.Bar Foo foo()");
    assertCurlyBrackets("void foo()");
    assertCurlyBrackets("void _foo()");
    // assertCurlyBrackets("void möebius()")); // not now
    assertCurlyBrackets("void foo() throws Bar");
    assertCurlyBrackets("void foo() throws Bar, Baz");
    assertCurlyBrackets("void foo() throws _Bar.Baz");
    assertCurlyBrackets("void foo()throws Bar,Bat.Man ");
    assertCurlyBrackets("Bar foo(Baz baz, Bat.Man batman)");
    assertCurlyBrackets("<T> Bar foo(T t)");
    assertCurlyBrackets("void abstractMethod()");
    assertCurlyBrackets("public myabstract foo()");

    assertFalse(cb("public static int = foo()"));
    assertFalse(cb("new foo()"));
    assertFalse(cb("throw new foo()"));
    assertFalse(cb("throw foo()"));
    assertFalse(cb(" functionif (true)"));
    assertFalse(cb("void foo() {"));
    assertFalse(cb("Foo.bar(Baz.class)"));
    assertFalse(cb("Foo.bar(bat())"));
    assertFalse(cb("foo()"));
  }

  @Test
  public void testCurlyBracketClassIntfEnumDeclaration() {
    assertCurlyBrackets("public static class Foo");
    assertCurlyBrackets("public static class Foo implements Bar.Baz");
    assertCurlyBrackets("protected abstract class Foo extends Bar");
    assertCurlyBrackets("protected abstract class Foo extends Bar implements Baz");
    assertCurlyBrackets("interface Foo");
    assertCurlyBrackets("private interface Foo implements Bar");
    assertCurlyBrackets("enum Foo");
    assertCurlyBrackets("enum _Foo");

    assertFalse(cb("fakeenum Foo"));
    assertFalse(cb("enumfake Foo"));
    assertFalse(cb("enum"));
    assertFalse(cb("_enum Foo"));
    assertFalse(cb("enum Foo {"));
    assertFalse(cb("public static class Foo {"));
    assertFalse(cb("public classic Foo"));
  }

  @Test
  public void testCurlyBracketIfWhileEtc() {
    assertCurlyBrackets(" if (true)");
    assertCurlyBrackets("} else if (true)");
    assertCurlyBrackets("if (true) ");
    assertCurlyBrackets("if(true) ");
    assertCurlyBrackets("\twhile (something) ");
    assertCurlyBrackets(" else if (true)");
    assertCurlyBrackets("} else if (true)");
    assertCurlyBrackets("\tfor (int i = 0; i < 100; i++)");
    assertCurlyBrackets("synchronized (foo)");
    assertCurlyBrackets(" synchronized(foo())");
    assertCurlyBrackets("switch(foo)");
    
    assertFalse(cb("_if (Foo foo)"));
  }

  @Test
  public void testCurlyBracketParameterlessKeywords() {
    assertCurlyBrackets(" do");
    assertCurlyBrackets("else ");
    assertCurlyBrackets("finally");
    assertCurlyBrackets("try");

    assertFalse(cb(" dont"));
    assertFalse(cb("_else "));
  }

  @Test
  public void testColonMisc() {
    assertColon("case 0");
    assertColon("case Foo.BAR");
    assertColon("case _Foo.BAR");
    assertColon("case \"foo\"");
    // assertTrue(k("case \"foo:bar\"")); // not going to that hell hole, no thanks
    assertColon("default");
    assertColon("  default");
  }
  
  @Test
  public void testBrackets() {
    assertBrackets("if");
    assertBrackets(" if");
    assertBrackets("else if");
    assertBrackets("} else if");
    assertBrackets("while");
    assertBrackets("for");
    assertBrackets("synchronized");
    assertBrackets("catch");
    assertBrackets("switch");

    assertFalse(br("if ("));
    assertFalse(br("if ()"));
    assertFalse(br("gif ()"));
    assertFalse(br("iff ()"));
  }

  @Test
  public void testNoneMatch() {
    assertNewLine("");
    assertNewLine(" ");
    assertNewLine("\t");
    assertNewLine("//CAPS");
    assertNewLine("   //foo");
    assertNewLine("}");
    assertNewLine("@Test");
    assertNewLine("@Foo.Bar");
    assertNewLine("@Ignore(\"becauseWhyNot\")");
    assertNewLine(" if (true) {");
    assertNewLine("\tfor (int i = 0;;i++) {");
    assertNewLine("while(isFoo()){");
    assertNewLine("new foo();");
    assertNewLine("String s = \"endOfLine;\".toString();");
    assertNewLine("case Foo.BAR:");
    assertNewLine("case Foo.BAR: return;");
    assertNewLine("default:");
    assertNewLine("default: return;");
    assertNewLine("return Foo.Bar.foo(\"foo\", bar, new Foo());");
    assertNewLine("return \"foo\";");
  }
  
  private void assertSemiColon(String string) {
    assertEquals(Action.SEMICOLON, LineEvaluator.getAction(string));
  }
  
  private void assertNewLine(String string) {
    assertEquals(Action.NEW_LINE, LineEvaluator.getAction(string));
  }
  
  private void assertCurlyBrackets(String string) {
    assertEquals(Action.CURLY_BRACKETS, LineEvaluator.getAction(string));
  }
  
  private void assertBrackets(String string) {
    assertEquals(Action.BRACKETS, LineEvaluator.getAction(string));
  }
  
  private void assertColon(String string) {
    assertEquals(Action.COLON, LineEvaluator.getAction(string));
  }

  private boolean cb(String string) {
    return Action.CURLY_BRACKETS.equals(LineEvaluator.getAction(string));
  }
  
  private boolean br(String string) {
    return Action.BRACKETS.equals(LineEvaluator.getAction(string));
  }
}
