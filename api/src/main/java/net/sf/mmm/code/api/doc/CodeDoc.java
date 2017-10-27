/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.List;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItem;

/**
 * {@link CodeItem} representing API documentation (e.g. JavaDoc or JSDoc).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeDoc extends CodeNodeItem {

  /**
   * Tag for the documentation of the parameter of a function. This tag will be invisible via this API as the
   * according doc is accessed via the according {@link net.sf.mmm.code.api.arg.CodeParameter}.
   */
  String TAG_PARAM = "param";

  /**
   * Tag for the documentation of an exception. This tag will be invisible via this API as the according doc
   * is accessed via the according {@link net.sf.mmm.code.api.arg.CodeException}.
   */
  String TAG_THROWS = "throws";

  /**
   * Tag for the documentation of returned result of a function. This tag will be invisible via this API as
   * the according doc is accessed via the according {@link net.sf.mmm.code.api.arg.CodeReturn}.
   */
  String TAG_RETURN = "return";

  /** {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a link. */
  String TAG_LINK = "link";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a plain
   * link.
   */
  String TAG_LINKPLAIN = "linkplain";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a code
   * format.
   */
  String TAG_CODE = "code";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for an
   * un-escaped literal.
   */
  String TAG_LITERAL = "literal";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a value
   * reference.
   */
  String TAG_VALUE = "value";

  @Override
  CodeElement getParent();

  /**
   * @param format the requested {@link CodeDocFormat}.
   * @return this documentation as {@link String} in the given {@link CodeDocFormat}. Will be the
   *         {@link String#isEmpty() empty} {@link String} if not available and therefore never
   *         <code>null</code>.
   */
  default String getFormatted(CodeDocFormat format) {

    return getFormatted(format, DEFAULT_NEWLINE);
  }

  /**
   * @param format the requested {@link CodeDocFormat}.
   * @param newline the newline {@link String}.
   * @return this documentation as {@link String} in the given {@link CodeDocFormat}. Will be the
   *         {@link String#isEmpty() empty} {@link String} if not available and therefore never
   *         <code>null</code>.
   */
  String getFormatted(CodeDocFormat format, String newline);

  /**
   * @return the {@link List} with the raw lines of documentation without leading format prefix ("/**", "*",
   *         "/*").
   */
  List<String> getLines();

  /**
   * @return {@code true} if this documentation is empty.
   */
  boolean isEmpty();

  @Override
  CodeDoc copy();

}
