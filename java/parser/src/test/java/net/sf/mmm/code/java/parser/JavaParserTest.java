package net.sf.mmm.code.java.parser;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import net.sf.mmm.code.java.parser.api.JavaParser;
import net.sf.mmm.code.java.parser.base.JavaSourceCodeParser.CompilationUnitContext;

/**
 * Test of {@link JavaParser}.
 */
public class JavaParserTest {

  @Test
  public void testAllInOneJava8() throws Exception {

    CompilationUnitContext compilationUnit = JavaParser.parse("src/test/resources/AllInOneJava8.java");
    // dump(compilationUnit);
  }

  @Test
  public void testMiniJavaDoc() throws Exception {

    CompilationUnitContext compilationUnit = JavaParser.parse("src/test/resources/MiniJavaDoc.java");
    dump(compilationUnit);
  }

  private void dump(ParseTree tree) {

    System.out.println(tree.toString());
    int count = tree.getChildCount();
    for (int i = 0; i < count; i++) {
      dump(tree.getChild(i));
    }
  }
}
