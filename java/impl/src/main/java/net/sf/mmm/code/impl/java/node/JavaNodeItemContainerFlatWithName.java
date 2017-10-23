/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.node.CodeNodeItemContainerFlatWithName;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerFlatWithName} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerFlatWithName<I extends JavaItem> extends JavaNodeItemContainerFlat<I>
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
  public JavaNodeItemContainerFlatWithName(JavaNodeItemContainerFlatWithName<I> template) {

    super(template);
  }

  @Override
  public I get(String name) {

    return get(name, true);
  }

  /**
   * @param name the {@link net.sf.mmm.code.api.item.CodeItemWithName#getName() name} of the requested
   *        {@link JavaItem}.
   * @param init {@code true} to ensure this container is {@link #initialize() initialized}, {@code false}
   *        otherwise.
   * @return the {@link JavaItem} from {@link #getAll() all items} with the given {@code name} or {@code null}
   *         if no such item exists.
   */
  protected I get(String name, boolean init) {

    initialize(init);
    return getByName(name);
  }

  @Override
  public abstract I add(String name);

}
