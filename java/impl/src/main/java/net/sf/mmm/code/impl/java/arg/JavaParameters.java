/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.List;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.arg.CodeParameters;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.member.JavaOperation;

/**
 * Implementation of {@link CodeParameters} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameters extends JavaOperationArgs<JavaParameter>
    implements CodeParameters<JavaParameter>, CodeNodeItemWithGenericParent<JavaOperation, JavaParameters> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameters(JavaOperation parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameters} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameters(JavaParameters template, JavaOperation parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Executable reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      List<JavaParameter> list = getList();
      for (Parameter param : reflectiveObject.getParameters()) {
        JavaParameter parameter = new JavaParameter(this, param);
        list.add(parameter);
      }
    }
  }

  @Override
  public JavaParameter get(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public JavaParameter getRequired(String name) {

    return super.getRequired(name);
  }

  @Override
  public JavaParameter add(String name) {

    JavaParameter parameter = new JavaParameter(this, name);
    add(parameter);
    return parameter;
  }

  @Override
  public JavaParameters copy() {

    return copy(getParent());
  }

  @Override
  public JavaParameters copy(JavaOperation newParent) {

    return new JavaParameters(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    writeReference(sink, newline, true);
  }

  void writeReference(Appendable sink, String newline, boolean declaration) throws IOException {

    String prefix = "";
    for (CodeParameter parameter : getList()) {
      sink.append(prefix);
      parameter.write(sink, newline, null, null);
      prefix = ", ";
    }
  }

}
