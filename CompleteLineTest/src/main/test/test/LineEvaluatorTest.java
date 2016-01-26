package test;

import static org.junit.Assert.*;

import org.junit.Test;

import henri5.LineEvaluator;
import henri5.LineCompleter.Action;

public class LineEvaluatorTest {

  @Test
  public void testSemiColonFunctionCalls() {
    assertTrue(sc("new foo()"));
    assertTrue(sc("new foo(bar, baz)"));
    assertTrue(sc("Foo.bar(bat())"));
    assertTrue(sc("Foo.bar(baz, bat())"));
    assertTrue(sc("foo()"));
    assertTrue(sc("  foo(bar)"));
    assertTrue(sc(")}"));
    assertTrue(sc("(Foo bar).foo()"));

    assertFalse(sc("void foo()"));
  }

  @Test
  public void testSemiColonFieldDeclaration() {
    assertTrue(sc("int i"));
    assertTrue(sc("i++"));
    assertTrue(sc("int i = asd"));
    assertTrue(sc("String s = \"endOfLine\""));
    assertTrue(sc("String s = \"endOfLine;\""));
    assertTrue(sc("String s = \"endOfLine;\".toString()"));
    assertTrue(sc("String at = \"@\""));
    assertTrue(sc("Foo foo = (Foo) bar"));
    assertTrue(sc("@Foo Foo foo"));
    assertTrue(sc("@Foo.Bar Foo.Bar foo"));
    assertTrue(sc("@Foo Foo foo = foo()"));
  }

  @Test
  public void testSemiColonMatchMisc() {
    assertTrue(sc("throw foo()"));
    assertTrue(sc("throw foo"));
    assertTrue(sc("import foo.bar"));
    assertTrue(sc("import static foo.bar.Baz"));
    assertTrue(sc("if (true) return false"));
    assertTrue(sc(" if (foo()) return bar"));
    assertTrue(sc("for (int i = 0; i < 100; i++) j++"));
    assertTrue(sc("case Foo.BAR: return"));
    assertTrue(sc("default: return foo"));
    assertTrue(sc("default: foo()"));
    assertTrue(sc("return foo()"));
    assertTrue(sc("return Foo.Bar.foo(\"foo\", bar, new Foo())"));
    assertTrue(sc("return \"foo\""));
    assertTrue(sc("return 1"));
    assertTrue(sc("return foo() || bar"));
    assertTrue(sc("return bar && foo()"));
    assertTrue(sc("do foo()"));
    // assertTrue(s(" if (true) bar()")); wishful thinking
  }

  @Test
  public void testCurlyBracketMethodDeclaration() {
    assertTrue(cb("public static void foo()"));
    assertTrue(cb("Foo.bar foo()"));
    // impossible to know if method call or constructor declaration with regex. Could guess
    // by checking if first letter is capital letter as normal people have, but might be a
    // stretch
//    assertTrue(c("Foo()")); 
    assertTrue(cb("public Foo()"));
    assertTrue(cb("Foo(Bar bar)"));
    assertTrue(cb("Foo(Bar bar, Baz baz)"));
    assertTrue(cb("public Foo(Bar bar, Baz baz)"));
    assertTrue(cb("@Foo.Bar Foo foo()"));
    assertTrue(cb("void foo()"));
    assertTrue(cb("void _foo()"));
    assertTrue(cb("void foo() throws Bar"));
    assertTrue(cb("void foo() throws Bar, Baz"));
    assertTrue(cb("void foo() throws _Bar.Baz"));
    assertTrue(cb("void foo()throws Bar,Bat.Man "));
    assertTrue(cb("Bar foo(Baz baz, Bat.Man batman)"));
    assertTrue(cb("<T> Bar foo(T t)"));

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
    assertTrue(cb("public static class Foo"));
    assertTrue(cb("public static class Foo implements Bar.Baz"));
    assertTrue(cb("protected abstract class Foo extends Bar"));
    assertTrue(cb("protected abstract class Foo extends Bar implements Baz"));
    assertTrue(cb("interface Foo"));
    assertTrue(cb("private interface Foo implements Bar"));
    assertTrue(cb("enum Foo"));
    assertTrue(cb("enum _Foo"));

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
    assertTrue(cb(" if (true)"));
    assertTrue(cb("if (true) "));
    assertTrue(cb("if(true) "));
    assertTrue(cb("\twhile (something) "));
    assertTrue(cb(" else if (true)"));
    assertTrue(cb("} else if (true)"));
    assertTrue(cb("\tfor (int i = 0; i < 100; i++)"));
    assertTrue(cb("synchronized (foo)"));
    assertTrue(cb(" synchronized(foo())"));
    assertTrue(cb("switch(foo)"));
    
    assertFalse(cb("_if (Foo foo)"));
  }

  @Test
  public void testCurlyBracketParameterlessKeywords() {
    assertTrue(cb(" do"));
    assertTrue(cb("else "));
    assertTrue(cb("finally"));
    assertTrue(cb("try"));

    assertFalse(cb(" dont"));
    assertFalse(cb("_else "));
  }

  @Test
  public void testColonMisc() {
    assertTrue(co("case 0"));
    assertTrue(co("case Foo.BAR"));
    assertTrue(co("case _Foo.BAR"));
    assertTrue(co("case \"foo\""));
    // assertTrue(k("case \"foo:bar\"")); // not going to that hell hole, no thanks
    assertTrue(co("default"));
    assertTrue(co("  default"));
  }
  
  @Test
  public void testBrackets() {
    assertTrue(br("if"));
    assertTrue(br(" if"));
    assertTrue(br("else if"));
    assertTrue(br("while"));
    assertTrue(br("for"));
    assertTrue(br("synchronized"));
    assertTrue(br("catch"));
    assertTrue(br("switch"));
    

    assertFalse(br("if ("));
    assertFalse(br("if ()"));
    assertFalse(br("gif ()"));
    assertFalse(br("iff ()"));
  }

  @Test
  public void testNoneMatch() {
    assertTrue(nl(""));
    assertTrue(nl(" "));
    assertTrue(nl("\t"));
    assertTrue(nl("//CAPS"));
    assertTrue(nl("   //foo"));
    assertTrue(nl("}"));
    assertTrue(nl("@Test"));
    assertTrue(nl("@Foo.Bar"));
    assertTrue(nl("@Ignore(\"becauseWhyNot\")"));
    assertTrue(nl(" if (true) {"));
    assertTrue(nl("\tfor (int i = 0;;i++) {"));
    assertTrue(nl("while(isFoo()){"));
    assertTrue(nl("new foo();"));
    assertTrue(nl("String s = \"endOfLine;\".toString();"));
    assertTrue(nl("case Foo.BAR:"));
    assertTrue(nl("case Foo.BAR: return;"));
    assertTrue(nl("default:"));
    assertTrue(nl("default: return;"));
    assertTrue(nl("return Foo.Bar.foo(\"foo\", bar, new Foo());"));
    assertTrue(nl("return \"foo\";"));
  }

  private boolean sc(String string) {
    return Action.SEMICOLON.equals(LineEvaluator.getAction(string));
  }

  private boolean cb(String string) {
    return Action.CURLY_BRACKETS.equals(LineEvaluator.getAction(string));
  }

  private boolean co(String string) {
    return Action.COLON.equals(LineEvaluator.getAction(string));
  }
  
  private boolean br(String string) {
    return Action.BRACKETS.equals(LineEvaluator.getAction(string));
  }

  private boolean nl(String string) {
    return Action.NEW_LINE.equals(LineEvaluator.getAction(string));
  }
}
