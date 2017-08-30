/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.imports.CodeImportItem;

/**
 * Implementation of {@link CodeImport}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaImport extends JavaItem implements CodeImport {

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
