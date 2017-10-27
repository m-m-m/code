/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.block.CodeBlockInitializer;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockInitializer extends BaseBlock implements CodeBlockInitializer, CodeNodeItemWithGenericParent<BaseType, BaseBlockInitializer> {

  private final BaseType parent;

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
   * @param parent the {@link #getParent() parent}.
   */
  public BaseBlockInitializer(BaseBlock template, BaseType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  public BaseBlockInitializer copy() {

    return copy(this.parent);
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    return this.parent.getFields().get(name);
  }

  @Override
  public BaseBlockInitializer copy(BaseType newParent) {

    return new BaseBlockInitializer(this, newParent);
  }

}
