/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.Map;

/**
 * TODO: this class ...
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