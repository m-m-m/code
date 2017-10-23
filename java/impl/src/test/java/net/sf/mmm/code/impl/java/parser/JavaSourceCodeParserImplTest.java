/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.java.parser.JavaSourceCodeParser;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.JavaRootContext;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Test of {@link JavaSourceCodeParserImpl}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class JavaSourceCodeParserImplTest extends Assertions {

  JavaSourceCodeParser getParser() {

    return JavaSourceCodeParserImpl.get();
  }

  JavaContext getContext() {

    return JavaRootContext.get();
  }

  JavaPackage createPackage(JavaContext context, CodeName qName) {

    if (qName == null) {
      return context.getRootPackage();
    }
    String simpleName = qName.getSimpleName();
    CodeName parentName = qName.getParent();
    JavaPackage parentPkg;
    if (parentName == null) {
      parentPkg = context.getRootPackage();
      if (simpleName.isEmpty()) {
        return parentPkg;
      }
    } else {
      parentPkg = createPackage(context, parentName);
    }
    return new JavaPackage(parentPkg, simpleName, null, null);
  }

  JavaFile createFile(String qualifiedName) {

    JavaContext context = getContext();
    CodeName qName = context.parseName(qualifiedName);
    JavaPackage pkg = createPackage(context, qName.getParent());
    return new JavaFile(pkg, qName.getSimpleName());
  }

  JavaType parse(Class<?> clazz) {

    String qualifiedName = clazz.getName();
    JavaFile file = createFile(qualifiedName);
    JavaSourceCodeParser parser = getParser();
    StringBuilder sb = new StringBuilder();
    if (qualifiedName.endsWith("Test")) {
      sb.append(MavenConstants.DEFAULT_TEST_SOURCE_DIRECTORY);
    } else {
      sb.append(MavenConstants.DEFAULT_SOURCE_DIRECTORY);
    }
    sb.append('/');
    sb.append(qualifiedName.replace('.', '/'));
    sb.append(".java");
    try (Reader reader = new FileReader(sb.toString())) {
      return parser.parse(reader, file);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    }
  }

  /**
   * Test reading {@link JavaSourceCodeParserImplTest} itself.
   */
  @Test
  public void testMyself() {

    // given
    Class<?> clazz = JavaSourceCodeParserImplTest.class;
    // when
    JavaType type = parse(clazz);
    // then
    assertThat(type).isNotNull();
    assertThat(type.getSimpleName()).isEqualTo(clazz.getSimpleName());
    assertThat(type.getQualifiedName()).isEqualTo(clazz.getName());
    assertThat(type.getCategory()).isSameAs(CodeTypeCategory.CLASS);
    assertThat(type.getModifiers().getVisibility()).isSameAs(CodeVisibility.PUBLIC);
    assertThat(type.getDoc().getLines()).containsExactly("Test of {@link JavaSourceCodeParserImpl}.", "",
        "@author Joerg Hohwiller (hohwille at users.sourceforge.net)");
    assertThat(type.getFile().getComment().getCommentLines()).containsExactly("Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0",
        "http://www.apache.org/licenses/LICENSE-2.0");
    assertThat(type.getFields().getDeclared()).isEmpty();
    assertThat(type.getConstructors().getDeclared()).isEmpty();
    List<? extends JavaMethod> methods = type.getMethods().getDeclared();
    assertThat(methods.stream().map(x -> x.getName())).containsExactlyInAnyOrder("getParser", "getContext", "createPackage", "createFile", "parse",
        "testMyself");
    for (JavaMethod method : methods) {
      if (method.getName().equals("createFile")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.DEFAULT);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).hasSize(1);
        CodeBlockBody body = method.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatements().stream().map(x -> x.toString())).containsExactly("JavaContext context = getContext();",
            "CodeName qName = context.parseName(qualifiedName);", "JavaPackage pkg = createPackage(context, qName.getParent());",
            "return new JavaFile(pkg, qName.getSimpleName());");
      } else if (method.getName().equals("testMyself")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.PUBLIC);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getExceptions()).isEmpty();
        assertThat(method.getReturns().getType().getQualifiedName()).isEqualTo("void");
        List<? extends JavaAnnotation> annotations = method.getAnnotations().getDeclared();
        assertThat(annotations).hasSize(1);
        JavaAnnotation annotation = annotations.get(0);
        assertThat(annotation).isNotNull();
        assertThat(annotation.getSourceCode()).isEqualTo("@Test");
        assertThat(method.getDoc().getLines()).containsExactly("Test reading {@link JavaSourceCodeParserImplTest} itself.");
      } else if (method.getName().equals("getParser")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.DEFAULT);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getExceptions()).isEmpty();
        // assertThat(method.getReturns().getType().getQualifiedName()).isEqualTo("net.sf.mmm.code.api.java.parser.JavaSourceCodeParser");
      }
    }
  }

}
