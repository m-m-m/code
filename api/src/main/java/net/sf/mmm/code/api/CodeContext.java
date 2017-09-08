/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.type.CodeType;

/**
 * A {@link CodeContext} is the main entry point of this API. It allows to retrieve and create instances of
 * {@link CodePackage}, {@link CodeFile}, and {@link CodeType}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeContext extends CodeProvider {

  @Override
  default CodeName getHierarchicalName(String qualifiedName) {

    return new CodeName(qualifiedName, getPackageSeparator());
  }

  /**
   * @return the package separator character.
   */
  default char getPackageSeparator() {

    return '.';
  }

  @Override
  CodeContext getParent();

  /**
   * @return the root type (for Java it represents {@link Object} for TypeScript {@code any}).
   */
  CodeType getRootType();

  /**
   * @return the {@link CodeType#isVoid() void} {@link CodeType} for a default
   *         {@link net.sf.mmm.code.api.arg.CodeReturn return}.
   */
  CodeType getVoidType();

  /**
   * @return the top-level {@link CodeType#isException() exception} {@link CodeType}. All sub-types are
   *         considered as {@link CodeType#isException() exceptions}. For Java this is {@link Throwable}.
   */
  CodeType getRootExceptionType();

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType} to resolve.
   * @param owningType the owning {@link CodeType} where the {@code simpleName} origins from used as context
   *        for resolution.
   * @param omitStandardPackages {@code true} to omit standard package(s) (for
   *        {@link #getQualifiedNameForStandardType(String, boolean) standard types}), {@code false} otherwise
   *        (to enforce {@link CodeType#getQualifiedName() qualified name} also for standard types).
   * @return the resolved {@link CodeType#getQualifiedName() qualified name} corresponding to the given
   *         {@code simpleName}.
   */
  default String getQualifiedName(String simpleName, CodeType owningType, boolean omitStandardPackages) {

    if (owningType.getSimpleName().equals(simpleName)) {
      return owningType.getQualifiedName();
    }
    char separator = getPackageSeparator();
    String suffix = separator + simpleName;
    for (CodeImport imp : owningType.getFile().getImports()) {
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
    String pkgName = owningType.getParentPackage().getQualifiedName();
    if (pkgName.isEmpty()) {
      return simpleName;
    }
    return pkgName + suffix;

  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType}.
   * @param omitStandardPackages {@code true} to omit standard package(s) (for
   *        {@link #getQualifiedNameForStandardType(String, boolean) standard types}), {@code false} otherwise
   *        (to enforce {@link CodeType#getQualifiedName() qualified name} also for standard types).
   * @return the corresponding {@link CodeType#getQualifiedName() qualified name} or {@code null} if no
   *         standard type (import is required).
   */
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages);

}
