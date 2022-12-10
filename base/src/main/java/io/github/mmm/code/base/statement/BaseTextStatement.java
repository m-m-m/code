/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.statement;

import java.io.IOException;

import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeStatement} that is unstructured and only contains the source-code as a
 * {@link #getCode() plain text}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTextStatement extends BaseAtomicStatement {

  private final String code;

  /**
   * The constructor.
   *
   * @param code the {@link #getCode() code}.
   */
  public BaseTextStatement(String code) {

    super();
    this.code = code;
  }

  /**
   * @return the line
   */
  public String getCode() {

    return this.code;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    sink.append(this.code);
    sink.append(newline);
  }

}
