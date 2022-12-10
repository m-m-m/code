/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.modifier;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.code.api.member.CodeMethod;

/**
 * Represents the visibility of a {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeVisibility implements Comparable<CodeVisibility> {

  private static final Map<String, CodeVisibility> VISIBILITY_MAP = new HashMap<>();

  /** {@link CodeVisibility} for public access. */
  public static final CodeVisibility PUBLIC = new CodeVisibility("public", 0);

  /** {@link CodeVisibility} for private access. */
  public static final CodeVisibility PRIVATE = new CodeVisibility("private", 100);

  /** {@link CodeVisibility} for protected access. */
  public static final CodeVisibility PROTECTED = new CodeVisibility("protected", 10);

  /** {@link CodeVisibility} for default access (no/empty modifier). */
  public static final CodeVisibility DEFAULT = new CodeVisibility("", 20);

  private final String value;

  private final int order;

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param value the visibility as {@link String}.
   * @param order the {@link #getOrder() order}.
   */
  public CodeVisibility(String value, int order) {

    super();
    assert (value != null);
    this.value = value;
    this.order = order;
    if (VISIBILITY_MAP.containsKey(value)) {
      throw new DuplicateObjectException(this, value, VISIBILITY_MAP.get(value));
    }
    VISIBILITY_MAP.put(value, this);
  }

  /**
   * @return the order
   */
  public int getOrder() {

    return this.order;
  }

  /**
   * @param visibility the {@link CodeVisibility} to compare to.
   * @return {@code true} if this {@link CodeVisibility} is stronger or {@link #equals(Object) equal} to the given
   *         {@code visibility}, {@code false} otherwise. E.g. {@link #PRIVATE} is stronger than {@link #PUBLIC}.
   */
  public boolean isStrongerOrEqualTo(CodeVisibility visibility) {

    if (visibility == null) {
      return false;
    }
    return this.order >= visibility.order;
  }

  /**
   * @param visibility the {@link CodeVisibility} to compare to.
   * @return {@code true} if this {@link CodeVisibility} is weaker or {@link #equals(Object) equal} to the given
   *         {@code visibility}, {@code false} otherwise. E.g. {@link #PUBLIC} is weaker than {@link #PRIVATE}.
   */
  public boolean isWeakerOrEqualTo(CodeVisibility visibility) {

    if (visibility == null) {
      return false;
    }
    return this.order <= visibility.order;
  }

  @Override
  public int compareTo(CodeVisibility visibility) {

    if (visibility == null) {
      return -1;
    }
    return this.order - visibility.order;
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

  /**
   * @param visiblity the {@link #toString() string representation} of the potential {@link CodeVisibility}
   * @return the according {@link CodeVisibility} or {@code null} if not known.
   */
  public static CodeVisibility of(String visiblity) {

    return VISIBILITY_MAP.get(visiblity);
  }

}
