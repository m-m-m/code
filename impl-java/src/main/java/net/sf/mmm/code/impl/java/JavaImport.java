/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.imports.CodeImportItem;
import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Implementation of {@link CodeImport}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaImport extends JavaItem implements CodeImport, Comparable<JavaImport> {

  private final String source;

  private final boolean staticFlag;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   * @param source the {@link #getSource() source} to import.
   * @param staticFlag the {@link #isStatic() static} flag.
   */
  public JavaImport(JavaContext context, String source, boolean staticFlag) {

    super(context);
    this.source = source;
    this.staticFlag = staticFlag;
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.source, this.staticFlag ? Integer.valueOf(1) : Integer.valueOf(0));
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
    if (!Objects.equals(this.source, other.source)) {
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
    return this.source.compareTo(other.source);
  }

  @Override
  public boolean isImmutable() {

    return true;
  }

  @Override
  public String getSource() {

    return this.source;
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
  protected void doWrite(Appendable sink, String indent, String currentIndent) throws IOException {

    sink.append(currentIndent); // indendation pointless...
    sink.append("import ");
    if (this.staticFlag) {
      sink.append("static ");
    }
    sink.append(this.source);
    sink.append(';');
    writeNewline(sink);
  }

}
