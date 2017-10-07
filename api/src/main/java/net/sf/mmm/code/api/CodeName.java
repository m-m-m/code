/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.Objects;

import net.sf.mmm.code.api.item.CodeMutableItemWithQualifiedName;

/**
 * Represents a parsed hierarchical name like a {@link CodeMutableItemWithQualifiedName#getQualifiedName()
 * qualified name} or a part of it (e.g. "code.api.CodeName" as part of "net.sf.mmm.code.api.CodeName").
 *
 * @see CodeProvider#parseName(String)
 */
public final class CodeName {

  private final char separator;

  private final String simpleName;

  private final String fullName;

  private final int lastSeparatorIndex;

  private CodeName parent;

  /**
   * The constructor.
   *
   * @param qualifiedName the qualified name.
   * @param separator the package separator.
   */
  public CodeName(String qualifiedName, char separator) {

    super();
    this.separator = separator;
    Objects.requireNonNull(qualifiedName, "qualifiedName");
    this.fullName = qualifiedName;
    this.lastSeparatorIndex = qualifiedName.lastIndexOf(separator);
    if (this.lastSeparatorIndex > 0) {
      this.simpleName = qualifiedName.substring(this.lastSeparatorIndex + 1);
    } else {
      this.simpleName = qualifiedName;
    }
  }

  /**
   * @return the {@link CodeMutableItemWithQualifiedName#getSimpleName() simple name}.
   */
  public String getSimpleName() {

    return this.simpleName;
  }

  /**
   * @return the full name as the full name of the potential {@link #getParent() parent} with the
   *         {@link #getSimpleName() simple name} appended separated by the separator char.
   */
  public String getFullName() {

    return this.fullName;
  }

  /**
   * @return the parent {@link CodeName} or {@code null} if this is the root.
   */
  public CodeName getParent() {

    if (this.parent == null) {
      if (this.lastSeparatorIndex > 0) {
        String parentName = this.fullName.substring(0, this.lastSeparatorIndex);
        this.parent = new CodeName(parentName, this.separator);
      }
    }
    return this.parent;
  }

  @Override
  public String toString() {

    return this.fullName;
  }
}
