/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.api.item.CodeItemWithQualifiedName;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * The top-level context used to retrieve existing {@link CodeElement}s or create new ones.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeContext {

  /**
   * @param qualifiedName the {@link CodeItemWithQualifiedName#getQualifiedName() qualified name} to parse.
   * @return the parsed {@link QualifiedName}.
   */
  default QualifiedName getQualifiedName(String qualifiedName) {

    return new QualifiedName(qualifiedName, getPackageSeparator());
  }

  /**
   * @return the package separator character.
   */
  default char getPackageSeparator() {

    return '.';
  }

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  default CodeType getType(String qualifiedName) {

    QualifiedName qname = getQualifiedName(qualifiedName);
    CodePackage pkg = getPackage(qname.getParent().getQualifiedName());
    if (pkg == null) {
      return null;
    }
    return getType(pkg, qname.getSimpleName());
  }

  /**
   * @param parentPackage the {@link CodeType#getParentPackage() parent package} of the requested
   *        {@link CodeType}.
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(CodePackage parentPackage, String simpleName);

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  default CodeType getRequiredType(String qualifiedName) {

    CodeType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

  /**
   * @param parentPackage the {@link CodeType#getParentPackage() parent package} of the new {@link CodeType}.
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the new {@link CodeType}.
   * @return a new {@link CodeType}.
   */
  CodeType createType(CodePackage parentPackage, String simpleName);

  /**
   * @param declaringType the {@link CodeType#getDeclaringType() declaring type}.
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the new {@link CodeType}.
   * @return a new {@link CodeType#isNested() nested} {@link CodeType}.
   */
  CodeType createType(CodeType declaringType, String simpleName);

  /**
   * @return the root {@link CodePackage}.
   */
  CodePackage getRootPackage();

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
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  default CodePackage getPackage(String qualifiedName) {

    return getPackage(getQualifiedName(qualifiedName));
  }

  /**
   * @param qualifiedName the {@link QualifiedName} of the requested {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  default CodePackage getPackage(QualifiedName qualifiedName) {

    CodePackage parentPackage;
    QualifiedName parent = qualifiedName.getParent();
    if (parent == null) {
      parentPackage = getRootPackage();
      if (qualifiedName.getSimpleName().isEmpty()) {
        return parentPackage;
      }
    } else {
      parentPackage = getPackage(parent);
    }
    return getPackage(parentPackage, qualifiedName.getSimpleName());
  }

  /**
   * @param parentPackage the {@link CodeType#getParentPackage() parent package} of the requested
   *        {@link CodePackage}.
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the requested {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(CodePackage parentPackage, String simpleName);

  /**
   * @param parentPackage the {@link CodeType#getParentPackage() parent package} of the new
   *        {@link CodePackage}.
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the new {@link CodePackage}.
   * @return a new {@link CodePackage}.
   */
  CodePackage createPackage(CodePackage parentPackage, String simpleName);

  /**
   * @param pkg the {@link CodePackage} to traverse.
   * @return the {@link List} with all direct child {@link CodePackage}s. May be {@link List#isEmpty() empty}
   *         but is never {@code null}.
   */
  List<? extends CodePackage> getChildPackages(CodePackage pkg);

  /**
   * @param pkg the {@link CodePackage} to traverse.
   * @return the {@link List} with all {@link CodeType}s contained in the given {@link CodePackage} (having
   *         the given {@link CodePackage} as {@link CodeType#getParentPackage() parent package}). May be
   *         {@link List#isEmpty() empty} but is never {@code null}.
   */
  List<? extends CodeType> getChildTypes(CodePackage pkg);

  /**
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  default CodePackage getRequiredPackage(String qualifiedName) {

    CodePackage pkg = getPackage(qualifiedName);
    if (pkg == null) {
      throw new ObjectNotFoundException(CodePackage.class, qualifiedName);
    }
    return pkg;
  }

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
    List<CodeImport> imports = owningType.getFile().getImports();
    char separator = getPackageSeparator();
    String suffix = separator + simpleName;
    for (CodeImport imp : imports) {
      if (!imp.isStatic()) {
        String source = imp.getSource();
        if (source.endsWith(suffix)) {
          return source;
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

  /**
   * Represents a parsed {@link CodeItemWithQualifiedName#getQualifiedName() qualified name}.
   *
   * @see CodeContext#getQualifiedName(String)
   */
  public static final class QualifiedName {

    private final char separator;

    private final String simpleName;

    private final String qualifiedName;

    private final int lastSeparatorIndex;

    private QualifiedName parent;

    /**
     * The constructor.
     *
     * @param qualifiedName the qualified name.
     * @param separator the package separator.
     */
    public QualifiedName(String qualifiedName, char separator) {

      super();
      this.separator = separator;
      Objects.requireNonNull(qualifiedName, "qualifiedName");
      this.qualifiedName = qualifiedName;
      this.lastSeparatorIndex = qualifiedName.lastIndexOf(separator);
      if (this.lastSeparatorIndex > 0) {
        this.simpleName = qualifiedName.substring(this.lastSeparatorIndex + 1);
      } else {
        this.simpleName = qualifiedName;
      }
    }

    /**
     * @return the {@link CodeItemWithQualifiedName#getSimpleName() simple name}.
     */
    public String getSimpleName() {

      return this.simpleName;
    }

    /**
     * @return the {@link CodeItemWithQualifiedName#getQualifiedName() qualified name}.
     */
    public String getQualifiedName() {

      return this.qualifiedName;
    }

    /**
     * @return the parent {@link QualifiedName} or {@code null} if this is the root.
     */
    public QualifiedName getParent() {

      if (this.parent == null) {
        if (this.lastSeparatorIndex > 0) {
          String parentName = this.qualifiedName.substring(0, this.lastSeparatorIndex);
          this.parent = new QualifiedName(parentName, this.separator);
        }
      }
      return this.parent;
    }
  }

  /**
   * @param type the {@link CodeType} to {@link CodeFile#getImports() import}.
   * @return a new canonical {@link CodeImport} for the given {@link CodeType}.
   */
  CodeImport createImport(CodeType type);

}
