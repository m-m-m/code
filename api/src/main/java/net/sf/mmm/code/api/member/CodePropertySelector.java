/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.util.Objects;

/**
 * Represents the scope of a {@link CodeField} or {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class CodePropertySelector {

  /** {@link CodePropertySelector} for private {@link CodeField}s. */
  public static final CodePropertySelector PRIVATE_FIELDS = new CodePropertySelector("private fields");

  /** {@link CodePropertySelector} for public {@link CodeField}s. */
  public static final CodePropertySelector PUBLIC_FIELDS = new CodePropertySelector("public fields");

  /**
   * {@link CodePropertySelector} for {@link CodeField}s visible to the class (private declared and for
   * inherited only if visible).
   */
  public static final CodePropertySelector FIELDS = new CodePropertySelector("fields");

  /** {@link CodePropertySelector} for {@link CodeField}s visible to the class. */
  public static final CodePropertySelector PUBLIC_METHODS = new CodePropertySelector("public methods");

  private final String value;

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param value the visibility as {@link String}.
   */
  public CodePropertySelector(String value) {

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
    CodePropertySelector other = (CodePropertySelector) obj;
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
