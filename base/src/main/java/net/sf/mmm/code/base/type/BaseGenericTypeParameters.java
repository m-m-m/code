/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericTypeParameters;
import net.sf.mmm.code.base.node.BaseNodeItemContainerFlat;

/**
 * Base implementation of {@link CodeGenericTypeParameters}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained {@link BaseGenericType}s.
 * @since 1.0.0
 */
public abstract class BaseGenericTypeParameters<P extends BaseGenericType> extends BaseNodeItemContainerFlat<P>
    implements CodeGenericTypeParameters<P> {

  /**
   * The constructor.
   */
  public BaseGenericTypeParameters() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseGenericTypeParameters} to copy.
   */
  public BaseGenericTypeParameters(BaseGenericTypeParameters<P> template) {

    super(template);
  }

  @Override
  public void add(P item) {

    super.add(item);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    writeReference(sink, newline, true);
  }

  void writeReference(Appendable sink, String newline, boolean declaration) throws IOException {

    List<? extends P> typeParameters = getDeclared();
    if (!typeParameters.isEmpty()) {
      String prefix = "<";
      for (P parameter : typeParameters) {
        sink.append(prefix);
        parameter.write(sink, newline, null, "");
        prefix = ", ";
      }
      sink.append('>');
    }
  }

}
