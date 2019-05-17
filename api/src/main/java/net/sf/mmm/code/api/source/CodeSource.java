/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.source;

import java.io.File;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodePathElements;
import net.sf.mmm.code.api.CodeProvider;
import net.sf.mmm.code.api.node.CodeContainer;
import net.sf.mmm.code.api.node.CodeNodeWithFileWriting;
import net.sf.mmm.code.api.object.CodeMutable;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * A {@link CodeSource} represents a {@link #getId() physical location} where {@link CodePackage}s and {@link CodeFile}s
 * are retrieved from.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeSource extends CodeProvider, CodeContainer, CodeNodeWithFileWriting, CodeMutable {

  /**
   * @return the root {@link CodePackage package} of this source. Will typically be lazy initialized and
   *         {@link CodePackage#getChildren() traversal} (especially {@link CodePathElements#getDeclared()}) can be
   *         expensive. Results will however be cached to speed up subsequent calls.
   */
  CodePackage getRootPackage();

  /**
   * @return the {@link CodeSourceDependencies} containing {@link CodeSource}s that are the parents of this source.
   *         These can be considered of the dependencies of this source (like in Maven, Gradle, Yarn, etc.)
   */
  CodeSourceDependencies<?> getDependencies();

  /**
   * @return the first parent from {@link #getDependencies()} or {@code null} if the {@link #getDependencies() parents}
   *         are empty. In the latter case this is the root source (in Java from system classloader with the JDK code).
   */
  @Override
  CodeSource getParent();

  /**
   * @return the unique ID of this source. May be the {@link File#toString() string representation} of the
   *         {@link #getByteCodeLocation() byte} or {@link #getSourceCodeLocation() source code location}.
   */
  String getId();

  /**
   * @return the {@link File} to the physical byte-code or {@code null} if not available. Can point to a directory,
   *         archive (JAR, WAR, EAR, etc.), or whatever format may be used for the according language. Therefore it is
   *         recommended to manually operate on this information.
   */
  File getByteCodeLocation();

  /**
   * @return the {@link File} to the physical source-code or {@code null} if not available. Can point to a directory,
   *         archive (ZIP, sources.jar, etc.), or whatever format may be used for the according language. Therefore it
   *         is not recommended to manually operate on this information.
   */
  File getSourceCodeLocation();

  /**
   * @return the {@link CodeSourceDescriptor} of this source.
   */
  CodeSourceDescriptor getDescriptor();

  /**
   * @return see {@link net.sf.mmm.code.api.item.CodeMutableItem#getReflectiveObject()}.
   */
  java.security.CodeSource getReflectiveObject();

  /**
   * <b>ATTENTION:</b><br>
   * When calling {@code getType} methods on {@link CodeSource} you will only be able to retrieve {@link CodeType}s from
   * exactly this source. This should only be used in very specific situations (e.g. if you only want to retrieve code
   * from main or test locations of a maven project). In most cases you want to use
   * {@link net.sf.mmm.code.api.CodeContext#getType(String)} instead.
   *
   * {@inheritDoc}
   */
  @Override
  CodeType getType(String qualifiedName);

  /**
   * <b>ATTENTION:</b><br>
   * When calling {@code getType} methods on {@link CodeSource} you will only be able to retrieve {@link CodeType}s from
   * exactly this source. This should only be used in very specific situations (e.g. if you only want to retrieve code
   * from main or test locations of a maven project). In most cases you want to use
   * {@link net.sf.mmm.code.api.CodeContext#getType(Class)} instead.
   *
   * {@inheritDoc}
   */
  @Override
  CodeGenericType getType(Class<?> clazz);

  /**
   * <b>ATTENTION:</b><br>
   * When calling {@code getType} methods on {@link CodeSource} you will only be able to retrieve {@link CodeType}s from
   * exactly this source. This should only be used in very specific situations (e.g. if you only want to retrieve code
   * from main or test locations of a maven project). In most cases you want to use
   * {@link net.sf.mmm.code.api.CodeContext#getType(CodeName)} instead.
   *
   * {@inheritDoc}
   */
  @Override
  CodeType getType(CodeName qualifiedName);

}
