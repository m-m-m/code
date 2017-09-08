/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.io.IOException;

import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.node.JavaNode;
import net.sf.mmm.code.impl.java.type.JavaGenericType;

/**
 * Implementation of {@link CodeOperationArg} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaOperationArg extends JavaElement implements CodeOperationArg {

  private JavaGenericType type;

  /**
   * The constructor.
   */
  public JavaOperationArg() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperationArg} to copy.
   */
  public JavaOperationArg(JavaOperationArg template) {

    super(template);
    this.type = template.type;
  }

  @Override
  public abstract JavaNode getParent();

  @Override
  public JavaGenericType getType() {

    if (this.type == null) {
      this.type = getContext().getRootType();
    }
    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = (JavaGenericType) type;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    doWriteAnnotations(sink, " ", null, null);
    this.type.writeReference(sink, false);
  }

}
