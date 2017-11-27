/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.node.CodeFunction;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.base.node.BaseFunction;

/**
 * Base implementation of {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockBody extends BaseBlock implements CodeBlockBody {

  private BaseFunction parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseBlockBody(BaseFunction parent) {

    this(parent, new ArrayList<>());
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockBody(BaseFunction parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockBody(BaseFunction parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlock} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockBody(BaseBlock template, BaseFunction parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
  }

  @Override
  public BaseFunction getParent() {

    return this.parent;
  }

  /**
   * @param parent the new value of {@link #getParent()}.
   */
  public void setParent(BaseFunction parent) {

    verifyMutalbe();
    this.parent = parent;
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    return this.parent.getVariable(name);
  }

  @Override
  public BaseBlockBody copy() {

    return copy(this.parent);
  }

  @Override
  public BaseBlockBody copy(CodeFunction newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseBlockBody copy(CodeFunction newParent, CodeCopyMapper mapper) {

    return new BaseBlockBody(this, (BaseFunction) newParent, mapper);
  }

}
