/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericTypeParameters;
import net.sf.mmm.code.base.node.AbstractCodeNodeItemContainerFlat;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;

/**
 * Implementation of {@link CodeGenericTypeParameters} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained {@link JavaGenericType}s.
 * @since 1.0.0
 */
public abstract class JavaGenericTypeParameters<P extends JavaGenericType> extends AbstractCodeNodeItemContainerFlat<P>
    implements CodeGenericTypeParameters<P>, JavaNodeItem {

  /**
   * The constructor.
   */
  public JavaGenericTypeParameters() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaGenericTypeParameters} to copy.
   */
  public JavaGenericTypeParameters(JavaGenericTypeParameters<P> template) {

    super(template);
  }

  @Override
  public void add(P item) {

    super.add(item);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

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
