/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.java.parser;

import java.io.Reader;

import net.sf.mmm.code.impl.java.JavaCodeLoader;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Interface for a parser of Java source code.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaSourceCodeParser {

  /**
   * @param reader the {@link Reader} with the source-code (e.g. from {@code *.java} file).
   * @param file the {@link JavaFile} where to add the {@link JavaElement elements} from the parsed
   *        source-code. In case an {@link JavaElement element} already exists, it shall be merged with the
   *        information from the source-code. Otherwise it will be created.
   * @return the {@link JavaFile#getType() main type} (e.g. when {@code Foo.java} was parsed, this should be
   *         the top-level type {@code Foo}). The canonical {@link JavaType#getSimpleName() simple name} for
   *         that type can be determined via {@link JavaFile#getSimpleName()}. In case of "odd" Java
   *         source-code where no such canonical type exists, the first top-level {@link JavaType} should be
   *         returned. If the source-code is entirely odd and no (valid) type is present at all, then
   *         {@code null} may be returned.
   */
  default JavaType parse(Reader reader, JavaFile file) {

    return parse(reader, file, null);
  }

  /**
   * @param reader the {@link Reader} with the source-code (e.g. from {@code *.java} file).
   * @param file the {@link JavaFile} where to add the {@link JavaElement elements} from the parsed
   *        source-code. In case an {@link JavaElement element} already exists, it shall be merged with the
   *        information from the source-code. Otherwise it will be created.
   * @param byteCodeLoader the optional {@link JavaCodeLoader} to load the according byte-code. May be
   *        {@code null}. Otherwise it has to be used to load all {@link JavaType}s instead of creating them
   *        manually in the given {@link JavaFile}.
   * @return the {@link JavaFile#getType() main type} (e.g. when {@code Foo.java} was parsed, this should be
   *         the top-level type {@code Foo}). The canonical {@link JavaType#getSimpleName() simple name} for
   *         that type can be determined via {@link JavaFile#getSimpleName()}. In case of "odd" Java
   *         source-code where no such canonical type exists, the first top-level {@link JavaType} should be
   *         returned. If the source-code is entirely odd and no (valid) type is present at all, then
   *         {@code null} may be returned.
   */
  JavaType parse(Reader reader, JavaFile file, JavaCodeLoader byteCodeLoader);

}
