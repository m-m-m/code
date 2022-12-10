/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.doc;

import java.util.Map;

/**
 * A {@link Tag} of HTML or XML.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface Tag {

  /**
   * @return the name of this tag ("a", "img", "code", etc.)
   */
  String getName();

  /**
   * @param tagName the {@link #getName() tag name} to check for.
   * @return {@code true} if the given {@code tagName} is {@link String#equalsIgnoreCase(String) equal
   *         ignoring case} to the {@link #getName() name} of this {@link Tag}.
   */
  default boolean hasNameIgnoreCase(String tagName) {

    return tagName.equalsIgnoreCase(getName());
  }

  /**
   * @return {@code true} if this is an opening tag (e.g. "{@literal <img ...>}" or "{@literal <img .../>}"),
   *         {@code false} otherwise.
   */
  boolean isOpening();

  /**
   * @return {@code true} if this is a closing tag (e.g. "{@literal </img>}" or "{@literal <img .../>}"),
   *         {@code false} otherwise.
   */
  boolean isClosing();

  /**
   * @return the attributes as a single string (e.g. "src='./image.png' border='1'"). May be
   *         {@link String#isEmpty()}.
   */
  String getAttributes();

  /**
   * @return the {@link #getAttributes() attributes} parsed as {@link Map}.
   */
  Map<String, String> getAttributesAsMap();

  /**
   * @return the {@link #isOpening() opening} parent {@link Tag} or {@code null} if this is the top-most
   *         {@link Tag}.
   */
  Tag getParent();

}
