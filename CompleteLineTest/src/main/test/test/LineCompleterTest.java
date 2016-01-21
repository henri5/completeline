package test;

import static org.junit.Assert.*;

import org.junit.Test;

import henri5.LineCompleter;

public class LineCompleterTest {

  @Test
  public void testSemiColonFunctionCalls() {
    assertTrue(s("new foo()"));
    assertTrue(s("new foo(bar, baz)"));
    assertTrue(s("Foo.bar(bat())"));
    assertTrue(s("Foo.bar(baz, bat())"));
    assertTrue(s("foo()"));
    assertTrue(s("  foo(bar)"));
    assertTrue(s(")}"));
    assertTrue(s("(Foo bar).foo()"));

    assertFalse(s("void foo()"));
  }

  @Test
  public void testSemiColonFieldDeclaration() {
    assertTrue(s("int i"));
    assertTrue(s("i++"));
    assertTrue(s("int i = asd"));
    assertTrue(s("String s = \"endOfLine\""));
    assertTrue(s("String s = \"endOfLine;\""));
    assertTrue(s("String s = \"endOfLine;\".toString()"));
    assertTrue(s("String at = \"@\""));
    assertTrue(s("Foo foo = (Foo) bar"));
  }

  @Test
  public void testSemiColonMatchMisc() {
    assertTrue(s("throw foo()"));
    assertTrue(s("throw foo"));
    assertTrue(s("import foo.bar"));
    assertTrue(s("import static foo.bar.Baz"));
    assertTrue(s("if (true) return false"));
    assertTrue(s(" if (foo()) return bar"));
    assertTrue(s("for (int i = 0; i < 100; i++) j++"));
    assertTrue(s("case Foo.BAR: return"));
    assertTrue(s("default: return foo"));
    assertTrue(s("default: foo()"));
    assertTrue(s("return foo()"));
    assertTrue(s("return Foo.Bar.foo(\"foo\", bar, new Foo())"));
    assertTrue(s("return \"foo\""));
    assertTrue(s("return 1"));
    assertTrue(s("return foo() || bar"));
    assertTrue(s("return bar && foo()"));
    assertTrue(s("do foo()"));
    // assertTrue(s(" if (true) bar()")); wishful thinking
  }

  @Test
  public void testCurlyBracketMethodDeclaration() {
    assertTrue(c("public static void foo()"));
    assertTrue(c("Foo.bar foo()"));
    assertTrue(c("@NotNull Foo foo()"));
    assertTrue(c("void foo()"));
    assertTrue(c("void _foo()"));
    assertTrue(c("void foo() throws Bar"));
    assertTrue(c("void foo() throws Bar, Baz"));
    assertTrue(c("void foo() throws _Bar.Baz"));
    assertTrue(c("void foo()throws Bar,Bat.Man "));
    assertTrue(c("Bar foo(Baz baz, Bat.Man batman)"));

    assertFalse(c("public static int = foo()"));
    assertFalse(c("new foo()"));
    assertFalse(c("throw new foo()"));
    assertFalse(c("throw foo()"));
    assertFalse(c(" functionif (true)"));
    assertFalse(c("void foo() {"));
    assertFalse(c("Foo.bar(Baz.class)"));
    assertFalse(c("Foo.bar(bat())"));
    assertFalse(c("foo()"));
  }

  @Test
  public void testCurlyBracketClassIntfEnumDeclaration() {
    assertTrue(c("public static class Foo"));
    assertTrue(c("public static class Foo implements Bar.Baz"));
    assertTrue(c("protected abstract class Foo extends Bar"));
    assertTrue(c("protected abstract class Foo extends Bar implements Baz"));
    assertTrue(c("interface Foo"));
    assertTrue(c("private interface Foo implements Bar"));
    assertTrue(c("enum Foo"));
    assertTrue(c("enum _Foo"));

    assertFalse(c("fakeenum Foo"));
    assertFalse(c("enumfake Foo"));
    assertFalse(c("enum"));
    assertFalse(c("_enum Foo"));
    assertFalse(c("enum Foo {"));
    assertFalse(c("public static class Foo {"));
    assertFalse(c("public classic Foo"));
  }

  @Test
  public void testCurlyBracketIfWhileEtc() {
    assertTrue(c(" if (true)"));
    assertTrue(c("if (true) "));
    assertTrue(c("if(true) "));
    assertTrue(c("\twhile (something) "));
    assertTrue(c(" else if (true)"));
    assertTrue(c("} else if (true)"));
    assertTrue(c("\tfor (int i = 0; i < 100; i++)"));
    assertTrue(c("synchronized (foo)"));
    assertTrue(c(" synchronized(foo())"));
    assertTrue(c("switch(foo)"));
    
    assertFalse(c("_if (Foo foo)"));
  }

  @Test
  public void testCurlyBracketParameterlessKeywords() {
    assertTrue(c(" do"));
    assertTrue(c("else "));
    assertTrue(c("finally"));
    assertTrue(c("try"));

    assertFalse(c(" dont"));
    assertFalse(c("_else "));
  }

  @Test
  public void testColonMisc() {
    assertTrue(k("case 0"));
    assertTrue(k("case Foo.BAR"));
    assertTrue(k("case _Foo.BAR"));
    assertTrue(k("case \"foo\""));
    // assertTrue(k("case \"foo:bar\"")); // not going to that hell hole, no thanks
    assertTrue(k("default"));
    assertTrue(k("  default"));
  }

  @Test
  public void testNoneMatch() {
    assertTrue(n(""));
    assertTrue(n(" "));
    assertTrue(n("\t"));
    assertTrue(n("//CAPS"));
    assertTrue(n("   //foo"));
    assertTrue(n("}"));
    assertTrue(n("@Test"));
    assertTrue(n("@Ignore(\"becauseWhyNot\")"));
    assertTrue(n(" if (true) {"));
    assertTrue(n("\tfor (int i = 0;;i++) {"));
    assertTrue(n("while(isFoo()){"));
    assertTrue(n("new foo();"));
    assertTrue(n("String s = \"endOfLine;\".toString();"));
    assertTrue(n("case Foo.BAR:"));
    assertTrue(n("case Foo.BAR: return;"));
    assertTrue(n("default:"));
    assertTrue(n("default: return;"));
    assertTrue(n("return Foo.Bar.foo(\"foo\", bar, new Foo());"));
    assertTrue(n("return \"foo\";"));
  }

  private boolean s(String string) {
    return LineCompleter.canInsertSemicolon(string);
  }

  private boolean c(String string) {
    return LineCompleter.canInsertCurlyBrackets(string);
  }

  // colon
  private boolean k(String string) {
    return LineCompleter.canInsertColon(string);
  }

  // neither
  private boolean n(String string) {
    return !c(string) && !s(string) && !k(string);
  }
}
