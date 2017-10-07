/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeNestedTypes;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerHierarchicalWithName;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaNestedTypes extends JavaNodeItemContainerHierarchicalWithName<JavaType>
    implements CodeNestedTypes<JavaType>, CodeNodeItemWithGenericParent<JavaType, JavaNestedTypes> {

  private final JavaType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaNestedTypes(JavaType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNestedTypes} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaNestedTypes(JavaNestedTypes template, JavaType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaType getParent() {

    return this.parent;
  }

  @Override
  public List<? extends JavaType> getAll() {

    List<JavaType> list = new ArrayList<>(getList());
    collectNestedTypes(list);
    return list;
  }

  private void collectNestedTypes(List<JavaType> list) {

    for (JavaType nested : getDeclared()) {
      JavaNestedTypes nestedContainer = nested.getNestedTypes();
      list.addAll(nestedContainer.getDeclared());
      nestedContainer.collectNestedTypes(list);
    }
  }

  @Override
  protected JavaType get(String name, boolean init) {

    JavaType nestedType = getDeclared(name, init);
    if (nestedType != null) {
      return nestedType;
    }
    for (JavaType nested : getDeclared()) {
      nestedType = nested.getNestedTypes().get(name);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  @Override
  public JavaType add(String name) {

    JavaType nestedType = new JavaType(getDeclaringType().getFile(), name, getDeclaringType(), null);
    add(nestedType);
    return nestedType;
  }

  @Override
  protected void add(JavaType item) {

    super.add(item);
  }

  @Override
  protected void rename(JavaType child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  public JavaNestedTypes copy() {

    return copy(this.parent);
  }

  @Override
  public JavaNestedTypes copy(JavaType newParent) {

    return new JavaNestedTypes(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    List<? extends JavaType> nestedTypes = getDeclared();
    if (nestedTypes.isEmpty()) {
      return;
    }
    String childIndent = currentIndent + defaultIndent;
    for (JavaType nestedType : nestedTypes) {
      sink.append(newline);
      nestedType.write(sink, newline, defaultIndent, childIndent);
    }
  }

}
