/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.doc;

import java.util.regex.Pattern;

import net.sf.mmm.code.base.doc.AbstractCodeDoc;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaElement;

/**
 * Implementation of {@link net.sf.mmm.code.api.doc.CodeDoc} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class JavaDoc extends AbstractCodeDoc<JavaContext> {

  private static final Pattern PATTERN_JAVADOC_TAG = Pattern.compile("\\{@([a-zA-Z]+) ([^}]*)\\}");

  /**
   * The constructor.
   *
   * @param element the owning {@link #getElement() element}.
   */
  public JavaDoc(JavaElement element) {

    super(element.getContext(), element);
  }

  @Override
  protected Pattern getTagPattern() {

    return PATTERN_JAVADOC_TAG;
  }

}
