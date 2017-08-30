/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.util.Objects;

/**
 * Represents a selector for {@link CodeField}s or {@link CodeMethod}s.
 *
 * @see net.sf.mmm.code.api.CodeType#getFields(CodeMemberSelector)
 * @see net.sf.mmm.code.api.CodeType#getMethods(CodeMemberSelector)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class CodeMemberSelector {

  /**
   * {@link CodeMemberSelector} for a {@link CodeMember} declared by a given
   * {@link net.sf.mmm.code.api.CodeType}.
   */
  public static final CodeMemberSelector DECLARED = new CodeMemberSelector("declared");

  /**
   * {@link CodeMemberSelector} for a {@link CodeMember} of a {@link net.sf.mmm.code.api.CodeType} inherited
   * from a {@link net.sf.mmm.code.api.CodeType#getSuperTypes() super-type}. Please note that a member is only
   * inherited, if it is actually visible (e.g. private methods are not inherited).
   */
  public static final CodeMemberSelector INHERITED = new CodeMemberSelector("inherited");

  /** {@link CodeMemberSelector} for a any {@link CodeMember}. */
  public static final CodeMemberSelector ALL = new CodeMemberSelector("all");

  private final String value;

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param value the visibility as {@link String}.
   */
  public CodeMemberSelector(String value) {

    super();
    assert (value != null);
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {

    if ((obj == null) || (obj.getClass() != getClass())) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    CodeMemberSelector other = (CodeMemberSelector) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {

    if (this.value == null) {
      return 0;
    } else {
      return this.value.hashCode();
    }
  }

  @Override
  public String toString() {

    return this.value;
  }

}
