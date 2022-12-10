/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.parser;

import java.io.Reader;

import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.type.BaseType;

/**
 * Interface for a parser of Java source code.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface SourceCodeParser {

  /**
   * @param reader the {@link Reader} with the source-code (e.g. from {@code *.java} file).
   * @param file the {@link BaseFile} where to add the {@link io.github.mmm.code.api.element.CodeElement
   *        elements} from the parsed source-code. In case an {@link io.github.mmm.code.api.element.CodeElement
   *        element} already exists, it shall be merged with the information from the source-code. Otherwise
   *        it will be created.
   * @return the {@link BaseFile#getType() main type} (e.g. when {@code Foo.java} was parsed, this should be
   *         the top-level type {@code Foo}). The canonical {@link BaseType#getSimpleName() simple name} for
   *         that type can be determined via {@link BaseFile#getSimpleName()}. In case of "odd" Java
   *         source-code where no such canonical type exists, the first top-level {@link BaseType} should be
   *         returned. If the source-code is entirely odd and no (valid) type is present at all, then
   *         {@code null} may be returned.
   */
  BaseType parseType(Reader reader, BaseFile file);

  /**
   * @param reader the {@link Reader} with the source-code (e.g. from {@code package-info.java} file).
   * @param pkg the {@link BasePackage} where to add {@link BasePackage#getDoc() doc},
   *        {@link BasePackage#getAnnotations() annotations}, and {@link BasePackage#getComment() comments}
   *        from the source-code.
   */
  void parsePackage(Reader reader, BasePackage pkg);

}
