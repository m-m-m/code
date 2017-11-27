/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockDoWhile;
import net.sf.mmm.code.api.block.CodeBlockWhileDo;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.statement.CodeStatement;

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
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link BaseBlockStatementWithCondition} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockWhileDo(BaseBlockStatementWithCondition template, BaseBlock parent, CodeCopyMapper mapper) {

    super(template, parent, mapper);
  }

  @Override
  public BaseBlockWhileDo copy() {

    return copy(getParent());
  }

  @Override
  public BaseBlockWhileDo copy(CodeBlock newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseBlockWhileDo copy(CodeBlock newParent, CodeCopyMapper mapper) {

    return new BaseBlockWhileDo(this, (BaseBlock) newParent, mapper);
  }

  @Override
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append("while (");
    getCondition().write(sink, newline, defaultIndent, currentIndent);
    sink.append(") ");
  }

}
