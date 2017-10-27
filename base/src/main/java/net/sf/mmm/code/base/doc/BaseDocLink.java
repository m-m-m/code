/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.util.function.Function;

import net.sf.mmm.code.api.doc.CodeDocLink;
import net.sf.mmm.code.api.doc.CodeDocMethodLink;

/**
 * Base implementation of {@link CodeDocLink}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseDocLink implements CodeDocLink {

  private final String simpleName;

  private final String qualifiedName;

  private final String anchor;

  private final String text;

  private boolean methodLinkSet;

  private final Function<CodeDocLink, String> urlProvider;

  private final Function<CodeDocLink, Object> valueProvider;

  private CodeDocMethodLink methodLink;

  /**
   * The constructor.
   *
   * @param value the content of the link tag ("&#64;{link «value»}").
   * @param separator the package separator character.
   * @param owningType the (qualified) name of the owning type.
   * @param urlProvider the {@link Function} to resolve the {@link #getLinkUrl() URL}.
   * @param valueProvider the {@link Function} to resolve the {@link #getLinkedValue() link value}.
   */
  public BaseDocLink(String value, char separator, String owningType, Function<CodeDocLink, String> urlProvider, Function<CodeDocLink, Object> valueProvider) {

    super();
    this.urlProvider = urlProvider;
    this.valueProvider = valueProvider;
    int spaceIndex = value.indexOf(' ');
    String link;
    String txt;
    if (spaceIndex > 0) {
      link = value.substring(0, spaceIndex);
      txt = value.substring(spaceIndex + 1);
    } else {
      link = value;
      txt = null;
    }
    int hashIndex = link.indexOf('#');
    String typeName;
    if (hashIndex >= 0) {
      this.anchor = link.substring(hashIndex + 1);
      if (hashIndex > 0) {
        typeName = link.substring(0, hashIndex);
      } else {
        typeName = owningType;
      }
    } else {
      this.anchor = null;
      typeName = link;
    }
    int separatorIndex = typeName.lastIndexOf(separator);
    if (separatorIndex > 0) {
      this.simpleName = typeName.substring(separatorIndex + 1);
      this.qualifiedName = typeName.substring(0, separatorIndex);
    } else {
      this.simpleName = typeName;
      this.qualifiedName = null;
    }
    if (txt == null) {
      txt = this.simpleName;
      if (this.anchor != null) {
        txt = txt + '.' + this.anchor;
      }
    }
    this.text = txt;
  }

  /**
   * @return the {@link Class#getSimpleName() simple name} of the linked type.
   */
  @Override
  public String getSimpleName() {

    return this.simpleName;
  }

  /**
   * @return the {@link Class#getName() qualified name} of the linked type. May be {@code null} if unqualified
   *         name was given. In such case it has to be looked up via
   *         {@link net.sf.mmm.code.api.CodeFile#getImports() imports}.
   */
  @Override
  public String getQualifiedName() {

    return this.qualifiedName;
  }

  /**
   * @return the optional linked anchor or {@code null} for none.
   */
  @Override
  public String getAnchor() {

    return this.anchor;
  }

  /**
   * @return the optional {@link BaseDocMethodLink} if the {@link #getAnchor() anchor} is a method reference
   *         or {@code null} for none.
   */
  @Override
  public CodeDocMethodLink getMethodLink() {

    if (!this.methodLinkSet) {
      this.methodLink = BaseDocMethodLink.of(this.anchor);
      this.methodLinkSet = true;
    }
    return this.methodLink;
  }

  @Override
  public Object getLinkedValue() {

    return this.valueProvider.apply(this);
  }

  @Override
  public String getLinkUrl() {

    return this.urlProvider.apply(this);
  }

  /**
   * @return the text of the link.
   */
  @Override
  public String getText() {

    return this.text;
  }

  @Override
  public String toString() {

    StringBuilder buffer = new StringBuilder(this.text.length() + 16);
    buffer.append("{@link ");
    buffer.append(this.simpleName);
    if (this.anchor != null) {
      buffer.append('#');
      buffer.append(this.anchor);
    }
    buffer.append(' ');
    buffer.append(this.text);
    buffer.append('}');
    return buffer.toString();
  }

}
