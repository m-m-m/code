/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

/**
 * Descriptor for URL mapping configuration to resolve links from {@link net.sf.mmm.code.api.doc.CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeDocDescriptor {

  /**
   * @return the prefix of the {@link Package} mapped to this {@link #getUrl() JavaDoc URL}.
   */
  String getPackagePrefix();

  /**
   * @return the base URL of the JavaDoc for this {@link #getPackagePrefix() package prefix}.
   */
  String getUrl();

}
