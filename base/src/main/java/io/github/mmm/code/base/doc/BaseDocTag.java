/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.doc;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.code.api.doc.CodeDoc;

/**
 * Simple container for the {@link JavaDoc} of a tagged child element (e.g. "bla bla" from "&#64;param foo bla
 * bla") as plain {@link String}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
class BaseDocTag {

  private String line;

  private List<String> extraLines;

  BaseDocTag(String line) {

    super();
    this.line = line;
  }

  void add(String line2add) {

    if (this.extraLines == null) {
      this.extraLines = new ArrayList<>();
    }
    this.extraLines.add(line2add);
  }

  void put(CodeDoc doc) {

    List<String> lines = doc.getLines();
    if ((this.line != null) && !this.line.isEmpty()) {
      lines.add(this.line);
    }
    if (this.extraLines != null) {
      lines.addAll(this.extraLines);
    }
  }

}
