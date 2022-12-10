/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.supplier;

import java.util.function.Supplier;

/**
 * Implementation of {@link Supplier} that will remember the {@link #get() provided object} and therefore
 * prevents expensive (lazy) evaluation from being executed more than once.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <T> type of the supplied object to {@link #get() get}.
 * @since 1.0.0
 * @deprecated do not use externally. Will most probably be moved (maybe even to mmm-util-*).
 */
@Deprecated
public class SupplierAdapter<T> implements Supplier<T> {

  private T object;

  private Supplier<T> supplier;

  /**
   * The constructor.
   *
   * @param supplier the raw {@link Supplier} to adapt.
   */
  public SupplierAdapter(Supplier<T> supplier) {

    super();
    this.supplier = supplier;
  }

  /**
   * The constructor.
   *
   * @param object the actual object to wrap.
   */
  public SupplierAdapter(T object) {

    super();
    this.object = object;
  }

  @Override
  public T get() {

    if ((this.object == null) && (this.supplier != null)) {
      this.object = this.supplier.get();
      this.supplier = null;
    }
    return this.object;
  }
}
