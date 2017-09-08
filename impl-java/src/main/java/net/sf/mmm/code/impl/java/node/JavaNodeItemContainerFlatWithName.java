/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlatWithName;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerFlatWithName} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <J> the type of the contained {@link JavaItem}.
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerFlatWithName<I extends CodeItem, J extends I> extends JavaNodeItemContainerFlat<I, J>
    implements CodeNodeItemContainerFlatWithName<I> {

  /**
   * The constructor.
   */
  protected JavaNodeItemContainerFlatWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItemContainerFlatWithName} to copy.
   */
  public JavaNodeItemContainerFlatWithName(JavaNodeItemContainerFlatWithName<I, J> template) {

    super(template);
  }

  @Override
  public J get(String name) {

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
  public J getOrCreate(String name) {

    J item = get(name);
    if (item == null) {
      item = add(name);
    }
    return item;
  }

}
