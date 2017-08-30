/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.modifier;

import java.lang.reflect.Modifier;
import java.util.Objects;

import net.sf.mmm.code.api.member.CodeMethod;

/**
 * Represents the visibility of a {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeVisibility {

  /** {@link CodeVisibility} for public access. */
  public static final CodeVisibility PUBLIC = new CodeVisibility("public");

  /** {@link CodeVisibility} for private access. */
  public static final CodeVisibility PRIVATE = new CodeVisibility("private");

  /** {@link CodeVisibility} for protected access. */
  public static final CodeVisibility PROTECTED = new CodeVisibility("protected");

  /** {@link CodeVisibility} for default access (no/empty modifier). */
  public static final CodeVisibility DEFAULT = new CodeVisibility("");

  private final String value;

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param value the visibility as {@link String}.
   */
  public CodeVisibility(String value) {

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
    CodeVisibility other = (CodeVisibility) obj;
    if (!Objects.equals(this.value, other.value)) {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * @param javaModifiers the Java {@link Modifier} mask.
   * @return the given {@link Modifier} mask as {@link CodeModifiers}.
   */
  public static CodeVisibility of(int javaModifiers) {

    if (Modifier.isPublic(javaModifiers)) {
      return PUBLIC;
    } else if (Modifier.isPrivate(javaModifiers)) {
      return PRIVATE;
    } else if (Modifier.isProtected(javaModifiers)) {
      return PROTECTED;
    } else {
      return DEFAULT;
    }
  }

}
