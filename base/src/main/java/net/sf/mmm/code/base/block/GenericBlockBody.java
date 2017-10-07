/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.node.CodeFunction;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Generic implementation of {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericBlockBody extends GenericBlock implements CodeBlockBody, CodeNodeItemWithGenericParent<CodeFunction, GenericBlockBody> {

  private final CodeFunction parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public GenericBlockBody(CodeFunction parent) {

    this(parent, new ArrayList<>());
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockBody(CodeFunction parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockBody(CodeFunction parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link GenericBlock} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public GenericBlockBody(GenericBlock template, CodeFunction parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public CodeFunction getParent() {

    return this.parent;
  }

  @Override
  public GenericBlockBody copy() {

    return copy(this.parent);
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    return this.parent.getVariable(name);
  }

  @Override
  public GenericBlockBody copy(CodeFunction newParent) {

    return new GenericBlockBody(this, newParent);
  }

}
