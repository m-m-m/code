/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeWildcard;

/**
 * A {@link CodeContext} is the main entry point of this API. It allows to retrieve and create instances of
 * {@link CodePackage}, {@link CodeFile}, and {@link CodeType}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeContext extends CodeProvider {

  /**
   * @return the {@link CodeLanguage} for the current programming language.
   */
  CodeLanguage getLanguage();

  @Override
  CodeContext getParent();

  /**
   * @return the root type (for Java it represents {@link Object} for TypeScript {@code any}).
   */
  CodeType getRootType();

  /**
   * @return the unbounded instance of {@link CodeTypeWildcard} (that has no {@link CodeTypeWildcard#getBound() bound}).
   *         This is typically represented by the type "{@code ?}" (e.g. in {@code List<?>}).
   */
  CodeTypeWildcard getUnboundedWildcard();

  /**
   * @return the root {@link CodeType#isEnumeration() enumeration} type (for Java it represents {@link Enum} for
   *         TypeScript {@code any}).
   */
  CodeType getRootEnumerationType();

  /**
   * @return the {@link CodeType#isVoid() void} {@link CodeType} for a default {@link net.sf.mmm.code.api.arg.CodeReturn
   *         return}.
   */
  CodeType getVoidType();

  /**
   * @param primitive {@code true} for primitive boolean and {@code false} for {@link Boolean} object type.
   * @return the requested boolean type.
   */
  CodeType getBooleanType(boolean primitive);

  /**
   * @return the top-level {@link CodeType#isException() exception} {@link CodeType}. All sub-types are considered as
   *         {@link CodeType#isException() exceptions}. For Java this is {@link Throwable}.
   */
  CodeType getRootExceptionType();

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType} to resolve.
   * @param owningType the owning {@link CodeType} where the {@code simpleName} origins from. It is used as context for
   *        the resolution.
   * @param omitStandardPackages {@code true} to omit standard package(s) (for
   *        {@link #getQualifiedNameForStandardType(String, boolean) standard types}), {@code false} otherwise (to
   *        enforce {@link CodeType#getQualifiedName() qualified name} also for standard types).
   * @return the resolved {@link CodeType#getQualifiedName() qualified name} corresponding to the given
   *         {@code simpleName}.
   */
  default String getQualifiedName(String simpleName, CodeType owningType, boolean omitStandardPackages) {

    if (owningType.getSimpleName().equals(simpleName)) {
      return owningType.getQualifiedName();
    }
    CodeFile file = owningType.getFile();
    return getQualifiedName(simpleName, file, omitStandardPackages);
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType} to resolve.
   * @param file the owning {@link CodeFile} where the {@code simpleName} origins from. It is used as context for the
   *        resolution.
   * @param omitStandardPackages {@code true} to omit standard package(s) (for
   *        {@link #getQualifiedNameForStandardType(String, boolean) standard types}), {@code false} otherwise (to
   *        enforce {@link CodeType#getQualifiedName() qualified name} also for standard types).
   * @return the resolved {@link CodeType#getQualifiedName() qualified name} corresponding to the given
   *         {@code simpleName}.
   */
  default String getQualifiedName(String simpleName, CodeFile file, boolean omitStandardPackages) {

    char separator = getLanguage().getPackageSeparator();
    String suffix = separator + simpleName;
    for (CodeImport imp : file.getImports()) {
      if (!imp.isStatic()) {
        String reference = imp.getReference();
        if (reference.endsWith(suffix)) {
          return reference;
        }
      }
    }
    String qname = getQualifiedNameForStandardType(simpleName, omitStandardPackages);
    if (qname != null) {
      return qname;
    }
    String pkgName = file.getParentPackage().getQualifiedName();
    if (pkgName.isEmpty()) {
      return simpleName;
    }
    return pkgName + suffix;
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType}.
   * @param omitStandardPackages {@code true} to omit standard package(s) (for
   *        {@link #getQualifiedNameForStandardType(String, boolean) standard types}), {@code false} otherwise (to
   *        enforce {@link CodeType#getQualifiedName() qualified name} also for standard types).
   * @return the corresponding {@link CodeType#getQualifiedName() qualified name} or {@code null} if no standard type
   *         (import is required).
   */
  String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages);

  /**
   * @return the {@link CodeFactory} to create specific {@link net.sf.mmm.code.api.item.CodeItem}s.
   */
  CodeFactory getFactory();

}
