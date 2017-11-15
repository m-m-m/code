/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeVariableThis;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;

/**
 * Generic implementation of {@link CodeVariableThis} using {@link #NAME_THIS}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseVariableThis extends BaseExpression implements CodeVariableThis {

  private final CodeGenericType type;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public BaseVariableThis(CodeGenericType type) {

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

    return this.type.getLanguage().getVariableNameThis();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(getName());
  }

}
