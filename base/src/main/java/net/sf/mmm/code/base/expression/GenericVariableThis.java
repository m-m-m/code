/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeVariableThis;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;

/**
 * Generic implementation of {@link CodeVariableThis} using {@link #NAME_THIS}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericVariableThis extends GenericExpression implements CodeVariableThis {

  private final CodeGenericType type;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public GenericVariableThis(CodeGenericType type) {

    super();
    Objects.requireNonNull(type, "type");
    this.type = type;
  }

  @Override
  public CodeConstant evaluate() {

    return null;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public String getName() {

    return this.type.getSyntax().getVariableNameThis();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    sink.append(getName());
  }

}
