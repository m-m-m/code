/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

/**
 * A link of a {@link CodeDocImpl doc} tag.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeDocLink {

  private final String simpleName;

  private final String qualifiedName;

  private final String anchor;

  private final String text;

  /**
   * The constructor.
   *
   * @param simpleName - see {@link #getSimpleName()}.
   * @param qualifiedName - see {@link #getQualifiedName()}.
   * @param anchor - see {@link #getAnchor()}.
   * @param text - see {@link #getText()}.
   */
  public CodeDocLink(String simpleName, String qualifiedName, String anchor, String text) {

    super();
    this.simpleName = simpleName;
    this.qualifiedName = qualifiedName;
    this.anchor = anchor;
    this.text = text;
  }

  /**
   * @return the {@link Class#getSimpleName() simple name} of the linked type.
   */
  public String getSimpleName() {

    return this.simpleName;
  }

  /**
   * @return the {@link Class#getName() qualified name} of the linked type.
   */
  public String getQualifiedName() {

    return this.qualifiedName;
  }

  /**
   * @return the optional linked anchor or <code>null</code> for none.
   */
  public String getAnchor() {

    return this.anchor;
  }

  /**
   * @return the text of the link.
   */
  public String getText() {

    return this.text;
  }

}
