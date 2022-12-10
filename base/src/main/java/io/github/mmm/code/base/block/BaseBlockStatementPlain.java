/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.block;

import java.util.List;

import io.github.mmm.code.api.block.CodeBlockStatement;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockStatementPlain extends BaseBlockStatement implements CodeNodeItemCopyable<BaseBlock, BaseBlockStatementPlain> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatementPlain(BaseBlock parent, CodeStatement... statements) {

    super(parent, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatementPlain(BaseBlock parent, List<CodeStatement> statements) {

    super(parent, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlockStatementPlain} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockStatementPlain(BaseBlockStatementPlain template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  public BaseBlockStatementPlain copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseBlockStatementPlain copy(CodeCopyMapper mapper) {

    return new BaseBlockStatementPlain(this, mapper);
  }

}
