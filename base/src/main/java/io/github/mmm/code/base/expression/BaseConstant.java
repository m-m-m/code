package io.github.mmm.code.base.expression;

import java.io.IOException;

import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.language.CodeLanguage;

/**
 * Base or dummy implementation of {@link CodeConstant}.
 *
 * @since 1.0.0
 */
public class BaseConstant extends BaseExpression implements CodeConstant {

  private final String value;

  /**
   * The constructor.
   *
   * @param value the raw {@link #getValue() value} as source code string (e.g. {@code "1L"} or {@code "\"text\""}).
   */
  public BaseConstant(String value) {

    super();
    this.value = value;
  }

  @Override
  public Object getValue() {

    return this.value;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language)
      throws IOException {

    sink.append(this.value);
  }

}
