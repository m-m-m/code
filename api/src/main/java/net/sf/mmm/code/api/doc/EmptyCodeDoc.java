/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of {@link CodeDoc} as fallback for empty documentation.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class EmptyCodeDoc implements CodeDoc {

  /** The signleton instance. */
  public static final CodeDoc INSTANCE = new EmptyCodeDoc();

  private EmptyCodeDoc() {

    super();
  }

  @Override
  public String get(CodeDocFormat format) {

    return "";
  }

  @Override
  public boolean isImmutable() {

    return true;
  }

  @Override
  public List<String> getLines() {

    return Collections.emptyList();
  }

  @Override
  public boolean isEmpty() {

    return true;
  }

  @Override
  public void write(Appendable sink, String indent, String currentIndent) {

    // nothing to do...
  }

  @Override
  public String toString() {

    return "<empty doc>";
  }

}
