/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.Objects;

/**
 * Represents the {@link CodeType#getCategory() type} of a {@link CodeType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class CodeTypeCategory {

  /** {@link CodeTypeCategory} for a regular {@link Class}. */
  public static final CodeTypeCategory CLASS = new CodeTypeCategory("class");

  /** {@link CodeTypeCategory} for an {@link Class#isInterface()} interface}. */
  public static final CodeTypeCategory INTERFACE = new CodeTypeCategory("interface");

  /** {@link CodeTypeCategory} for an {@link Class#isEnum() enumeration}. */
  public static final CodeTypeCategory ENUMERAION = new CodeTypeCategory("enum");

  /** {@link CodeTypeCategory} for an {@link Class#isAnnotation() annotation}. */
  public static final CodeTypeCategory ANNOTATION = new CodeTypeCategory("annotation");

  private final String value;

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param value the visibility as {@link String}.
   */
  public CodeTypeCategory(String value) {

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
    CodeTypeCategory other = (CodeTypeCategory) obj;
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
