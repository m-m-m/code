/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.doc;

import java.util.regex.Pattern;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.base.doc.AbstractCodeDoc;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.node.JavaNode;

/**
 * Implementation of {@link net.sf.mmm.code.api.doc.CodeDoc} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public final class JavaDoc extends AbstractCodeDoc implements JavaNode, CodeNodeItemWithGenericParent<JavaElement, JavaDoc> {

  private static final Pattern PATTERN_JAVADOC_TAG = Pattern.compile("\\{@([a-zA-Z]+) ([^}]*)\\}");

  private final JavaElement parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent element}.
   */
  public JavaDoc(JavaElement parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeDoc} to copy.
   * @param parent the new {@link #getParent() parent element}.
   */
  public JavaDoc(JavaDoc template, JavaElement parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaElement getParent() {

    return this.parent;
  }

  @Override
  protected Pattern getTagPattern() {

    return PATTERN_JAVADOC_TAG;
  }

  @Override
  public JavaDoc copy() {

    return copy(this.parent);
  }

  @Override
  public JavaDoc copy(JavaElement newParent) {

    return new JavaDoc(this, newParent);
  }

}
