/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Implementation of {@link SourceCodeProvider} for lazy instantiation.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class SourceCodeProviderProxy implements SourceCodeProvider {

  private Supplier<SourceCodeProvider> supplier;

  private SourceCodeProvider delegate;

  /**
   * The constructor.
   *
   * @param supplier the {@link Supplier} for the actual {@link SourceCodeProvider}.
   */
  public SourceCodeProviderProxy(Supplier<SourceCodeProvider> supplier) {

    super();
    Objects.requireNonNull(supplier, "supplier");
    this.supplier = supplier;
  }

  /**
   * @return the delegate
   */
  public SourceCodeProvider getDelegate() {

    if (this.delegate == null) {
      if (this.supplier != null) {
        this.delegate = this.supplier.get();
        this.supplier = null;
      }
      if (this.delegate == null) {
        return SourceCodeProviderNone.INSTANCE; // closed or supplier returned null
      }
    }
    return this.delegate;
  }

  @Override
  public Reader openType(String qualifiedName) throws IOException {

    return getDelegate().openType(qualifiedName);
  }

  @Override
  public Reader openPackage(String qualifiedName) throws IOException {

    return getDelegate().openPackage(qualifiedName);
  }

  @Override
  public List<String> scanPackage(String qualifiedName) throws IOException {

    return getDelegate().scanPackage(qualifiedName);
  }

  @Override
  public void close() throws Exception {

    if (this.delegate != null) {
      this.delegate.close();
    }
    this.delegate = null;
    this.supplier = null;
  }

}
