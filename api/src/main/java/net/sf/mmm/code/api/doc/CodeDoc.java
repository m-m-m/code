/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.Collection;
import java.util.List;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.merge.CodeSimpleMergeableItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItem} representing API documentation (e.g. JavaDoc or JSDoc). Please read documentation of
 * {@link #getLines()} before using.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeDoc extends CodeSimpleMergeableItem<CodeDoc>, CodeNodeItemCopyable<CodeElement, CodeDoc> {

  /**
   * Tag for the documentation of the parameter of a function. This tag will be invisible via this API as the according
   * doc is accessed via the according {@link net.sf.mmm.code.api.arg.CodeParameter}.
   */
  String TAG_PARAM = "param";

  /**
   * Tag for the documentation of an exception. This tag will be invisible via this API as the according doc is accessed
   * via the according {@link net.sf.mmm.code.api.arg.CodeException}.
   */
  String TAG_THROWS = "throws";

  /**
   * Tag for the documentation of returned result of a function. This tag will be invisible via this API as the
   * according doc is accessed via the according {@link net.sf.mmm.code.api.arg.CodeReturn}.
   */
  String TAG_RETURN = "return";

  /** {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a link. */
  String TAG_LINK = "link";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a plain link.
   */
  String TAG_LINKPLAIN = "linkplain";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a code format.
   */
  String TAG_CODE = "code";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for an un-escaped literal.
   */
  String TAG_LITERAL = "literal";

  /**
   * {@link CodeDocFormat#replaceDocTag(String, java.util.function.Supplier, String) Doc tag} for a value reference.
   */
  String TAG_VALUE = "value";

  /**
   * Empty {@link #getLinesAsArray() lines}.
   */
  String[] NO_LINES = new String[0];

  /**
   * @param format the requested {@link CodeDocFormat}.
   * @return this documentation as {@link String} in the given {@link CodeDocFormat}. Will be the
   *         {@link String#isEmpty() empty} {@link String} if not available and therefore never <code>null</code>.
   */
  default String getFormatted(CodeDocFormat format) {

    return getFormatted(format, DEFAULT_NEWLINE);
  }

  /**
   * @param format the requested {@link CodeDocFormat}.
   * @param newline the newline {@link String}.
   * @return this documentation as {@link String} in the given {@link CodeDocFormat}. Will be the
   *         {@link String#isEmpty() empty} {@link String} if not available and therefore never <code>null</code>.
   */
  String getFormatted(CodeDocFormat format, String newline);

  /**
   * <b>Attention:</b> Child elements such as {@link net.sf.mmm.code.api.arg.CodeParameter}s,
   * {@link net.sf.mmm.code.api.arg.CodeException}s, {@link net.sf.mmm.code.api.arg.CodeReturn}, and
   * {@link net.sf.mmm.code.api.type.CodeTypeVariable}s hold their specific {@link CodeDoc} that is not included in the
   * {@link CodeDoc} of their parent {@link net.sf.mmm.code.api.member.CodeOperation} or
   * {@link net.sf.mmm.code.api.type.CodeType}. Therefore you shall not see or {@link #add(String...) add} any lines
   * with tags such as "@param", "@throws", or "@return". These will only be produced on-the-fly when the
   * {@link #getSourceCode() source-code} is produced.
   *
   * @return the {@link List} with the raw lines of documentation without leading format prefix ("/**", "*", "/*").
   */
  List<String> getLines();

  /**
   * @return the {@link #getLines() documentation lines} as array.
   * @see #getLines()
   */
  default String[] getLinesAsArray() {

    List<String> lines = getLines();
    if (lines.isEmpty()) {
      return CodeDoc.NO_LINES;
    }
    return lines.toArray(new String[lines.size()]);
  }

  /**
   * Please read documentation of {@link #getLines()} before using.
   *
   * @param lines the textual documentation lines to add to {@link #getLines()}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void add(Collection<String> lines);

  /**
   * Please read documentation of {@link #getLines()} before using.
   *
   * @param lines the textual documentation lines to add to {@link #getLines()}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void add(String... lines);

  /**
   * @return {@code true} if this documentation is empty.
   */
  boolean isEmpty();

}
