/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.block;

import java.io.IOException;
import java.util.List;

import io.github.mmm.code.api.block.CodeBlockDoWhile;
import io.github.mmm.code.api.block.CodeBlockWhileDo;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.expression.CodeCondition;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockDoWhile}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockWhileDo extends BaseBlockStatementWithCondition implements CodeBlockWhileDo {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockWhileDo(BaseBlock parent, CodeCondition condition, CodeStatement... statements) {

    super(parent, condition, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockWhileDo(BaseBlock parent, CodeCondition condition, List<CodeStatement> statements) {

    super(parent, condition, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlockStatementWithCondition} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockWhileDo(BaseBlockStatementWithCondition template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  public BaseBlockWhileDo copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseBlockWhileDo copy(CodeCopyMapper mapper) {

    return new BaseBlockWhileDo(this, mapper);
  }

  @Override
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append("while (");
    getCondition().write(sink, newline, defaultIndent, currentIndent);
    sink.append(") ");
  }

}
