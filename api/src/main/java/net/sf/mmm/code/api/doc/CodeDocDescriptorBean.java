/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

/**
 * Descriptor for URL mapping configuration to resolve links from {@link net.sf.mmm.code.api.doc.CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeDocDescriptorBean {

  private String packagePrefix;

  private String url;

  /**
   * The constructor.
   */
  public CodeDocDescriptorBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param packagePrefix - see {@link #getPackagePrefix()}.
   * @param url - see {@link #getUrl()}.
   */
  public CodeDocDescriptorBean(String packagePrefix, String url) {

    super();
    this.packagePrefix = packagePrefix;
    this.url = url;
  }

  /**
   * @return the prefix of the {@link Package} mapped to this {@link #getUrl() JavaDoc URL}.
   */
  public String getPackagePrefix() {

    if (this.packagePrefix == null) {
      return "";
    }
    return this.packagePrefix;
  }

  /**
   * @param packagePrefix is the packagePrefix to set
   */
  public void setPackagePrefix(String packagePrefix) {

    this.packagePrefix = packagePrefix;
  }

  /**
   * @return the base URL of the JavaDoc for this {@link #getPackagePrefix() package prefix}.
   */
  public String getUrl() {

    return this.url;
  }

  /**
   * @param url is the url to set
   */
  public void setUrl(String url) {

    this.url = url;
  }

  @Override
  public String toString() {

    return this.packagePrefix + " -> " + this.url;
  }

}
