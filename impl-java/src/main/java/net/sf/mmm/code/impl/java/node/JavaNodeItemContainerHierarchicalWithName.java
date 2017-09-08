/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerHierarchicalWithName} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <J> the type of the contained {@link JavaItem}.
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerHierarchicalWithName<I extends CodeItem, J extends I> extends JavaNodeItemContainerHierarchical<I, J>
    implements CodeNodeItemContainerHierarchicalWithName<I> {

  /**
   * The constructor.
   */
  protected JavaNodeItemContainerHierarchicalWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItemContainerHierarchicalWithName} to copy.
   */
  public JavaNodeItemContainerHierarchicalWithName(JavaNodeItemContainerHierarchicalWithName<I, J> template) {

    super(template);
  }

  @Override
  public abstract J get(String name);

  @Override
  public J getDeclared(String name) {

    initialize();
    return getByName(name);
  }

  @Override
  public J getRequired(String name) {

    return super.getRequired(name);
  }

  @Override
  public abstract J add(String name);

  @Override
  public J getDeclaredOrCreate(String name) {

    J item = getDeclared(name);
    if (item == null) {
      item = add(name);
    }
    return item;
  }

  @Override
  public J getOrCreate(String name) {

    J item = get(name);
    if (item == null) {
      item = add(name);
    }
    return item;
  }

}
