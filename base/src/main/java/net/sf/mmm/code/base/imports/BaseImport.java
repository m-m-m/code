/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.imports;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.imports.CodeImportItem;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.base.item.BaseItem;

/**
 * Base implementation of {@link CodeImport}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseImport extends BaseItem implements CodeImport, Comparable<BaseImport> {

  private final String reference;

  private final boolean staticFlag;

  private final List<CodeImportItem> items;

  /**
   * The constructor.
   *
   * @param reference the {@link #getReference() reference} to import.
   * @param staticFlag the {@link #isStatic() static} flag.
   */
  public BaseImport(String reference, boolean staticFlag) {

    this(reference, staticFlag, null);
  }

  /**
   * The constructor.
   *
   * @param reference the {@link #getReference() reference} to import.
   * @param staticFlag the {@link #isStatic() static} flag.
   * @param items the {@link #getItems() items}.
   */
  public BaseImport(String reference, boolean staticFlag, List<CodeImportItem> items) {

    super();
    Objects.requireNonNull(reference, "reference");
    this.reference = reference;
    this.staticFlag = staticFlag;
    if (items == null) {
      this.items = Collections.emptyList();
    } else {
      this.items = Collections.unmodifiableList(items);
    }
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
    BaseImport other = (BaseImport) obj;
    if (!Objects.equals(this.reference, other.reference)) {
      return false;
    }
    if (this.staticFlag != other.staticFlag) {
      return false;
    }
    if (!Objects.equals(this.items, other.items)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(BaseImport other) {

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

    return this.items;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent); // indendation pointless...
    sink.append("import ");
    if (this.staticFlag) {
      sink.append("static ");
    }
    boolean hasItems = !this.items.isEmpty();
    if (hasItems) {
      String prefix = "{ ";
      for (CodeImportItem item : this.items) {
        sink.append(prefix);
        item.write(sink, newline, defaultIndent, currentIndent, language);
        prefix = ", ";
      }
      sink.append("} from '");
    }
    sink.append(this.reference);
    if (hasItems) {
      sink.append('\'');
    }
    sink.append(';');
    sink.append(newline);
  }

}
