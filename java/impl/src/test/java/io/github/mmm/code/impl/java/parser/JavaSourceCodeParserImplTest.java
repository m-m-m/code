/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.parser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.modifier.CodeVisibility;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.parser.SourceCodeParser;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.impl.java.AbstractBaseTypeTest;
import io.github.mmm.code.impl.java.JavaContext;
import io.github.mmm.code.impl.java.JavaRootContext;
import io.github.mmm.code.java.maven.api.MavenConstants;

/**
 * Test of {@link JavaSourceCodeParserImpl}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class JavaSourceCodeParserImplTest extends AbstractBaseTypeTest {

  SourceCodeParser getParser() {

    return JavaSourceCodeParserImpl.get();
  }

  JavaContext getContext() {

    return JavaRootContext.get();
  }

  BasePackage createPackage(BaseSource source, CodeName qName) {

    BasePackage parentPkg = source.getRootPackage();
    if (qName == null) {
      return parentPkg;
    }
    String simpleName = qName.getSimpleName();
    CodeName parentName = qName.getParent();
    if (parentName == null) {
      if (simpleName.isEmpty()) {
        return parentPkg;
      }
    } else {
      parentPkg = createPackage(source, parentName);
    }
    return new BasePackage(parentPkg, simpleName, null, null, false);
  }

  BaseFile createFile(String qualifiedName) {

    JavaContext context = getContext();
    CodeName qName = context.parseName(qualifiedName);
    BasePackage pkg = createPackage(context.getSource(), qName.getParent());
    return new BaseFile(pkg, qName.getSimpleName());
  }

  BaseType parse(Class<?> clazz) {

    String qualifiedName = clazz.getName();
    BaseFile file = createFile(qualifiedName);
    SourceCodeParser parser = getParser();
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
      return parser.parseType(reader, file);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
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
    BaseType type = parse(clazz);
    // then
    assertThat(type).isNotNull();
    assertThat(type.getSimpleName()).isEqualTo(clazz.getSimpleName());
    assertThat(type.getQualifiedName()).isEqualTo(clazz.getName());
    assertThat(type.getCategory()).isSameAs(CodeTypeCategory.CLASS);
    assertThat(type.getModifiers().getVisibility()).isSameAs(CodeVisibility.PUBLIC);
    assertThat(type.getDoc().getLines()).containsExactly("Test of {@link JavaSourceCodeParserImpl}.", "",
        "@author Joerg Hohwiller (hohwille at users.sourceforge.net)");
    BaseFile file = type.getFile();
    verifyHeader(file);
    List<String> imports = file.getImports().getDeclared().stream().map(i -> i.getReference()).toList();
    assertThat(imports).contains("java.io.FileReader", "org.junit.jupiter.api.Test",
        "io.github.mmm.code.impl.java.JavaContext");
    assertThat(type.getFields().getDeclared()).isEmpty();
    assertThat(type.getConstructors().getDeclared()).isEmpty();
    List<? extends CodeMethod> methods = type.getMethods().getDeclared();
    assertThat(methods.stream().map(x -> x.getName())).containsExactlyInAnyOrder("getParser", "getContext",
        "createPackage", "createFile", "parse", "testMyself");
    for (CodeMethod method : methods) {
      if (method.getName().equals("createFile")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.DEFAULT);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).hasSize(1);
        CodeBlockBody body = method.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getStatements().stream().map(x -> x.toString())).containsExactly(
            "JavaContext context = getContext();", "CodeName qName = context.parseName(qualifiedName);",
            "BasePackage pkg = createPackage(context.getSource(), qName.getParent());",
            "return new BaseFile(pkg, qName.getSimpleName());");
      } else if (method.getName().equals("testMyself")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.PUBLIC);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getExceptions()).isEmpty();
        assertThat(method.getReturns().getType().getQualifiedName()).isEqualTo("void");
        List<? extends CodeAnnotation> annotations = method.getAnnotations().getDeclared();
        assertThat(annotations).hasSize(1);
        CodeAnnotation annotation = annotations.get(0);
        assertThat(annotation).isNotNull();
        assertThat(annotation.toString()).isEqualTo("@Test");
        assertThat(method.getDoc().getLines())
            .containsExactly("Test reading {@link JavaSourceCodeParserImplTest} itself.");
      } else if (method.getName().equals("getParser")) {
        assertThat(method.getModifiers().getVisibility()).isEqualTo(CodeVisibility.DEFAULT);
        assertThat(method.getModifiers().getModifiers()).isEmpty();
        assertThat(method.getParameters()).isEmpty();
        assertThat(method.getExceptions()).isEmpty();
        assertThat(method.getReturns().getType().getQualifiedName())
            .isEqualTo("io.github.mmm.code.base.parser.SourceCodeParser");
      }
    }
  }

}
