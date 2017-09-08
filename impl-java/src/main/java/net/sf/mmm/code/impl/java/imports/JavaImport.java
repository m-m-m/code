/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.imports;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.imports.CodeImportItem;
import net.sf.mmm.code.base.AbstractCodeItem;

/**
 * Implementation of {@link CodeImport}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaImport extends AbstractCodeItem implements CodeImport, Comparable<JavaImport> {

  private final String reference;

  private final boolean staticFlag;

  /**
   * The constructor.
   *
   * @param reference the {@link #getReference() reference} to import.
   * @param staticFlag the {@link #isStatic() static} flag.
   */
  public JavaImport(String reference, boolean staticFlag) {

    super();
    this.reference = reference;
    this.staticFlag = staticFlag;
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.reference, this.staticFlag ? Integer.valueOf(1) : Integer.valueOf(0));
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    JavaImport other = (JavaImport) obj;
    if (!Objects.equals(this.reference, other.reference)) {
      return false;
    }
    if (this.staticFlag != other.staticFlag) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(JavaImport other) {

    if (other == null) {
      return -1;
    }
    if (this.staticFlag != other.staticFlag) {
      if (this.staticFlag) {
        return -1;
      } else {
        return 1;
      }
    }
    return this.reference.compareTo(other.reference);
  }

  @Override
  public String getReference() {

    return this.reference;
  }

  @Override
  public boolean isStatic() {

    return this.staticFlag;
  }

  @Override
  public List<CodeImportItem> getItems() {

    return Collections.emptyList();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String indent, String currentIndent) throws IOException {

    sink.append(currentIndent); // indendation pointless...
    sink.append("import ");
    if (this.staticFlag) {
      sink.append("static ");
    }
    sink.append(this.reference);
    sink.append(';');
    sink.append(newline);
  }

}
