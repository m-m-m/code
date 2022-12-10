/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.block.CodeBlockInitializer;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.statement.CodeStatement;
import io.github.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockInitializer extends BaseBlock implements CodeBlockInitializer {

  private BaseType parent;

  private boolean isStatic;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseBlockInitializer(BaseType parent) {

    this(parent, new ArrayList<>());
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockInitializer(BaseType parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockInitializer(BaseType parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlock} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockInitializer(BaseBlockInitializer template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  /**
   * @param parent the new value of {@link #getParent()}.
   */
  public void setParent(BaseType parent) {

    verifyMutalbe();
    this.parent = parent;
  }

  @Override
  public boolean isStatic() {

    return this.isStatic;
  }

  @Override
  public void setStatic(boolean isStatic) {

    verifyMutalbe();
    this.isStatic = isStatic;
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    return this.parent.getFields().get(name);
  }

  @Override
  public BaseBlockInitializer copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseBlockInitializer copy(CodeCopyMapper mapper) {

    return new BaseBlockInitializer(this, mapper);
  }

  @Override
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    if (this.isStatic) {
      sink.append("static ");
    }
    super.writePrefix(sink, newline, defaultIndent, currentIndent);
  }

}
