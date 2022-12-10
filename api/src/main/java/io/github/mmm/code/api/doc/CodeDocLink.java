/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.doc;

/**
 * Represents a parsed link of a {@link CodeDoc}.
 *
 * @see CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeDocLink {

  /**
   * @return the {@link Class#getSimpleName() simple name} of the linked type.
   */
  String getSimpleName();

  /**
   * @return the {@link Class#getName() qualified name} of the linked type. May be {@code null} if unqualified
   *         name was given. In such case it has to be looked up via
   *         {@link io.github.mmm.code.api.CodeFile#getImports() imports}.
   */
  String getQualifiedName();

  /**
   * @return the optional linked anchor or {@code null} for none.
   */
  String getAnchor();

  /**
   * @return the optional {@link CodeDocMethodLink} if the {@link #getAnchor() anchor} is a method reference
   *         or {@code null} for none.
   */
  CodeDocMethodLink getMethodLink();

  /**
   * @return the text of the link.
   */
  String getText();

  /**
   * @return the resolved value or {@code null} if no value referenced or resolving failed. Should be only
   *         used for {@link CodeDoc#TAG_VALUE value} tags.
   */
  Object getLinkedValue();

  /**
   * @return the {@link #getLinkedValue() linked value} as {@link String} with fallback to
   *         {@link #getSimpleName()} with optional {@link #getAnchor() anchor}. Therefore never {@code null}.
   */
  default String getLinkedValueAsString() {

    Object value = getLinkedValue();
    String result;
    if (value == null) {
      String anchor = getAnchor();
      String simpleName = getSimpleName();
      if (anchor == null) {
        result = simpleName;
      } else {
        result = simpleName + "." + anchor;
      }
    } else {
      result = value.toString();
    }
    return result;
  }

  /**
   * @return the resolved URL of this link.
   */
  String getLinkUrl();

}
