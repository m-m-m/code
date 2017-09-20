/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import java.util.List;

import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerHierarchical} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerHierarchical<I extends JavaItem> extends JavaNodeItemContainer<I> implements CodeNodeItemContainerHierarchical<I> {

  /**
   * The constructor.
   */
  protected JavaNodeItemContainerHierarchical() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItemContainerHierarchical} to copy.
   */
  public JavaNodeItemContainerHierarchical(JavaNodeItemContainerHierarchical<I> template) {

    super(template);
  }

  @Override
  public final List<? extends I> getDeclared() {

    initialize();
    return getList();
  }

}
