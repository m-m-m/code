/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeNodeItemContainerHierarchicalWithName} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainerHierarchicalWithName<I extends JavaItem> extends JavaNodeItemContainerHierarchical<I>
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
  public JavaNodeItemContainerHierarchicalWithName(JavaNodeItemContainerHierarchicalWithName<I> template) {

    super(template);
  }

  @Override
  public final I getDeclared(String name) {

    return getDeclared(name, true);
  }

  /**
   * @param name the {@link net.sf.mmm.code.api.item.CodeItemWithName#getName() name} of the requested
   *        {@link JavaItem}.
   * @param init {@code true} to ensure this container is {@link #initialize() initialized}, {@code false}
   *        otherwise.
   * @return the {@link JavaItem} from the {@link #getDeclared() declared items} with the given {@code name}
   *         or {@code null} if no such item exists.
   */
  protected I getDeclared(String name, boolean init) {

    initialize(init);
    return getByName(name);
  }

  @Override
  public final I get(String name) {

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
  protected abstract I get(String name, boolean init);

  @Override
  public I getRequired(String name) {

    return super.getRequired(name);
  }

}
