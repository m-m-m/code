/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.node.BaseNodeItemContainerHierarchical;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Base implementation of {@link CodeSuperTypes}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSuperTypes extends BaseNodeItemContainerHierarchical<CodeGenericType> implements CodeSuperTypes {

  private static final Logger LOG = LoggerFactory.getLogger(BaseSuperTypes.class);

  private final BaseType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseSuperTypes(BaseType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseSuperTypes} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseSuperTypes(BaseSuperTypes template, BaseType parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject != null) {
      BaseContext context = getContext();
      Type superclass = reflectiveObject.getGenericSuperclass();
      if (superclass != null) {
        addInternal(context.getType(superclass, this.parent));
      }
      Type[] genericInterfaces = reflectiveObject.getGenericInterfaces();
      for (Type superInterface : genericInterfaces) {
        addInternal(context.getType(superInterface, this.parent));
      }
    }
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  public Iterable<? extends CodeGenericType> getAll() {

    return () -> new SuperTypeIterator(this.parent);
  }

  @Override
  public void add(CodeGenericType superType) {

    if (!(superType instanceof BaseGenericTypeProxy) && superType.asType().equals(this.parent)) {
      throw new IllegalStateException("Type " + this.parent.getQualifiedName() + " can not extend itself");
    }
    super.add(superType);
  }

  @Override
  public CodeGenericType getSuperClass() {

    CodeGenericType superClass = getSuperClassAsDeclared();
    if (superClass != null) {
      return superClass;
    }
    CodeType rootType = getContext().getRootType();
    if (this.parent != rootType) {
      return rootType;
    }
    return null;
  }

  private CodeGenericType getSuperClassAsDeclared() {

    if (this.parent.isInterface() || this.parent.isAnnotation()) {
      return null;
    }
    for (CodeGenericType type : getDeclared()) {
      if (type.isClass()) {
        return type;
      }
    }
    return null;
  }

  @Override
  public List<? extends CodeGenericType> getSuperInterfaces() {

    return getDeclared().stream().filter(x -> x.isInterface()).collect(Collectors.toList());
  }

  @Override
  public CodeSuperTypes getSourceCodeObject() {

    CodeType sourceType = this.parent.getSourceCodeObject();
    if (sourceType != null) {
      return sourceType.getSuperTypes();
    }
    return null;
  }

  @Override
  public CodeSuperTypes merge(CodeSuperTypes o, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseSuperTypes other = (BaseSuperTypes) o;
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeGenericType otherSuperType : other.getDeclared()) {
        add(otherSuperType /* .copy(this) */);
      }
    } else {
      Map<String, CodeGenericType> mySuperTypeMap = new HashMap<>();
      for (CodeGenericType mySuperType : getDeclared()) {
        mySuperTypeMap.put(mySuperType.getQualifiedName(), mySuperType);
      }
      for (CodeGenericType otherSuperType : other.getDeclared()) {
        CodeGenericType mySuperType = mySuperTypeMap.get(otherSuperType.getQualifiedName());
        if (mySuperType == null) {
          add(otherSuperType /* .copy(this) */);
        } else {
          // TODO mySuperType.doMerge(otherSuperType, strategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseSuperTypes copy() {

    return copy(this.parent);
  }

  @Override
  public BaseSuperTypes copy(CodeType newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseSuperTypes copy(CodeType newParent, CodeCopyMapper mapper) {

    return new BaseSuperTypes(this, (BaseType) newParent, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    String keywordExtends = language.getKeywordForExtends();
    String keywordInherit;
    if (this.parent.isInterface()) {
      keywordInherit = keywordExtends;
    } else {
      keywordInherit = language.getKeywordForImplements();
      CodeGenericType superClass = getSuperClassAsDeclared();
      if (superClass != null) {
        if (this.parent.isClass()) {
          sink.append(keywordExtends);
          superClass.writeReference(sink, false);
        } else {
          LOG.warn("Illegal {} {}: can not have super-class {}.", this.parent.getCategory(), this.parent.getSimpleName(), superClass.asType().getSimpleName());
        }
      }
    }
    String separator = keywordInherit;
    for (CodeGenericType superType : getDeclared()) {
      if (superType.isInterface()) {
        sink.append(separator);
        superType.writeReference(sink, false);
        separator = ", ";
      }
    }
  }

  private static class SuperTypeIterator extends AbstractIterator<CodeGenericType> {

    private final InternalSuperTypeIterator root;

    private InternalSuperTypeIterator iterator;

    /**
     * The constructor.
     *
     * @param type the initial {@link BaseType}. All its transitive super-types will be iterated.
     */
    public SuperTypeIterator(BaseType type) {

      super();
      this.root = new InternalSuperTypeIterator(type);
      this.iterator = this.root;
      findFirst();
    }

    @Override
    protected CodeGenericType findNext() {

      if (this.iterator.hasNext()) {
        this.iterator = this.iterator.next();
      } else {
        return null;
      }
      if (this.iterator == null) {
        return null;
      }
      return this.iterator.getType();
    }

  }

}
