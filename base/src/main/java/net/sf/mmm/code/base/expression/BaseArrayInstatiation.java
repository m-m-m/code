/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.mmm.code.api.expression.CodeArrayInstatiation;
import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.language.CodeLanguage;

/**
 * Base implementation of CodeArrayInstatiatio{@link HashMap}-
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseArrayInstatiation extends BaseExpression implements CodeArrayInstatiation {

  private final List<CodeExpression> values;

  /**
   * The constructor.
   *
   * @param values the {@link #getValues() values}.
   */
  public BaseArrayInstatiation(List<CodeExpression> values) {

    super();
    this.values = Collections.unmodifiableList(values);
  }

  @Override
  public CodeConstant evaluate() {

    // can only be implemented in language specific sub-class
    return null;
  }

  @Override
  public List<CodeExpression> getValues() {

    return this.values;
  }

  @Override
  @SuppressWarnings({})
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append('{');
    if (!this.values.isEmpty()) {
      String prefix = " ";
      for (CodeExpression v : this.values) {
        sink.append(prefix);
        v.write(sink, newline, defaultIndent, currentIndent, language);
        prefix = ", ";
      }
      sink.append(' ');
    }
    sink.append('}');
  }

}
