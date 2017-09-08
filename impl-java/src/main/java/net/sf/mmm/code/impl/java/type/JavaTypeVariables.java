/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerFlatWithName;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariables extends JavaNodeItemContainerFlatWithName<CodeTypeVariable, JavaTypeVariable>
    implements CodeTypeVariables, CodeNodeItemWithGenericParent<JavaElement, JavaTypeVariables> {

  /** The empty and {@link #isImmutable() immutable} instance of {@link JavaTypeVariables}. */
  public static final JavaTypeVariables EMPTY = new JavaTypeVariables();

  private final JavaType declaringType;

  private final JavaOperation declaringOperation;

  /**
   * The constructor.
   */
  private JavaTypeVariables() {

    super();
    this.declaringType = null;
    this.declaringOperation = null;
    setImmutable();
  }

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaType declaringType) {

    super();
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The constructor.
   *
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaOperation declaringOperation) {

    super();
    this.declaringType = declaringOperation.getDeclaringType();
    this.declaringOperation = declaringOperation;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaType declaringType) {

    super(template);
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaOperation declaringOperation) {

    super(template);
    this.declaringType = declaringOperation.getDeclaringType();
    this.declaringOperation = declaringOperation;
  }

  @Override
  public JavaElement getParent() {

    if (this.declaringOperation != null) {
      return this.declaringOperation;
    }
    return this.declaringType;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public JavaOperation getDeclaringOperation() {

    return this.declaringOperation;
  }

  @Override
  public JavaTypeVariable add(String name) {

    JavaTypeVariable variable = new JavaTypeVariable(this, name);
    add(variable);
    return variable;
  }

  JavaTypeVariables createChild() {

    if (this.declaringOperation != null) {
      return new JavaTypeVariables(this.declaringOperation);
    } else {
      return new JavaTypeVariables(this.declaringType);
    }
  }

  @Override
  protected void rename(JavaTypeVariable child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    writeReference(sink, true);
  }

  @Override
  public JavaTypeVariables copy() {

    return copy(getParent());
  }

  @Override
  public JavaTypeVariables copy(JavaElement newParent) {

    if (newParent instanceof JavaType) {
      return new JavaTypeVariables(this, (JavaType) newParent);
    } else if (newParent instanceof JavaOperation) {
      return new JavaTypeVariables(this, (JavaOperation) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  void writeReference(Appendable sink, boolean declaration) throws IOException {

    List<JavaTypeVariable> typeVariables = getList();
    if (!typeVariables.isEmpty()) {
      String prefix = "<";
      for (JavaTypeVariable variable : typeVariables) {
        sink.append(prefix);
        variable.write(sink, "", "");
        prefix = ", ";
      }
      sink.append('>');
    }
  }

}
