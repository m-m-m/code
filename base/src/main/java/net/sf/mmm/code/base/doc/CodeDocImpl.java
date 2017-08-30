/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.doc.CodeDocFormat;
import net.sf.mmm.code.base.AbstractCodeItem;

/**
 * Abstract base implementation of {@link CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeDocImpl extends AbstractCodeItem implements CodeDoc {

  private final CodeDocContext context;

  private List<String> lines;

  /**
   * The constructor.
   *
   * @param context the {@link CodeDocContext} for the owning {@link java.lang.reflect.Type}.
   */
  public CodeDocImpl(CodeDocContext context) {

    super();
    this.lines = new ArrayList<>();
    this.context = context;
  }

  @Override
  public String get(CodeDocFormat format) {

    if (CodeDocFormat.RAW.equals(format) || (format == null)) {
      StringBuilder buffer = new StringBuilder();
      for (String line : this.lines) {
        buffer.append(line);
        writeNewline(buffer);
      }
      return buffer.toString();
    } else {
      return this.context.getConverted(this, format);
    }
  }

  @Override
  public List<String> getLines() {

    return this.lines;
  }

  @Override
  public boolean isEmpty() {

    return this.lines.isEmpty();
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    int size = this.lines.size();
    if (size == 0) {
      return;
    }
    sink.append(currentIndent);
    sink.append("/**");
    if (size == 1) {
      sink.append(' ');
      sink.append(this.lines.get(0).trim());
    } else {
      writeNewline(sink);
      for (String line : this.lines) {
        sink.append(currentIndent);
        sink.append(" * ");
        sink.append(line.trim());
        writeNewline(sink);
      }
      sink.append(currentIndent);
    }
    sink.append(" */");
    writeNewline(sink);
  }

}
