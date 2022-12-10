/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.annoation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.element.BaseElement;
import io.github.mmm.code.base.node.BaseNodeItemContainerHierarchical;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.base.type.InternalSuperTypeIterator;

/**
 * Base implementation of {@link CodeAnnotations}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseAnnotations extends BaseNodeItemContainerHierarchical<CodeAnnotation> implements CodeAnnotations {

  private final BaseElement parent;

  private CodeAnnotations sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseAnnotations(BaseElement parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseAnnotations} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseAnnotations(BaseAnnotations template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  public BaseElement getParent() {

    return this.parent;
  }

  @Override
  protected CodeAnnotation ensureParent(CodeAnnotation item) {

    if (item.getParent() != this) {
      if (item.isMutable() && item.getParent().getParent() instanceof CodeFile) {
        ((BaseAnnotation) item).setParent(this);
      } else {
        return doCopyNode(item, this);
      }
    }
    return item;
  }

  @Override
  protected boolean isSystemImmutable() {

    boolean systemImmutable = super.isSystemImmutable();
    if (!systemImmutable && (this.parent != null)) {
      systemImmutable = isSystemImmutable(this.parent);
    }
    return systemImmutable;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    if (this.parent == null) {
      return;
    }
    doInitByteCode();
    doInitSourceCode();
  }

  private void doInitByteCode() {

    Object reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject instanceof AnnotatedElement) {
      Annotation[] annotations = ((AnnotatedElement) reflectiveObject).getAnnotations();
      if (annotations.length == 0) {
        return;
      }
      for (Annotation annotation : annotations) {
        addInternal(new BaseAnnotation(this, annotation));
      }
    }
  }

  private void doInitSourceCode() {

    CodeAnnotations sourceAnnotations = getSourceCodeObject();
    if (sourceAnnotations == null) {
      return;
    }
    List<? extends CodeAnnotation> sourceList = sourceAnnotations.getDeclared();
    if (sourceList.isEmpty()) {
      return;
    }
    Set<String> annotationTypes = createAnnotationTypeNameSet();
    for (CodeAnnotation sourceAnnotation : sourceList) {
      String key = sourceAnnotation.getType().getQualifiedName();
      if (!annotationTypes.contains(key)) {
        addInternal(sourceAnnotation);
      }
    }
  }

  private Set<String> createAnnotationTypeNameSet() {

    Set<String> annotationTypes = null;
    List<? extends CodeAnnotation> declared = getDeclared();
    if (declared.isEmpty()) {
      annotationTypes = Collections.emptySet();
    } else {
      annotationTypes = new HashSet<>();
      for (CodeAnnotation annotation : declared) {
        annotationTypes.add(annotation.getType().getQualifiedName());
      }
    }
    return annotationTypes;
  }

  @Override
  public CodeAnnotation getDeclared(CodeType type) {

    for (CodeAnnotation annotation : getDeclared()) {
      if (annotation.getType().asType().equals(type)) {
        return annotation;
      }
    }
    return null;
  }

  @Override
  public CodeAnnotation add(CodeType type) {

    verifyMutalbe();
    CodeAnnotation annotation = createAnnoation(type);
    add(annotation);
    return annotation;
  }

  /**
   * @param type the {@link BaseAnnotation#getType() type} of the {@link BaseAnnotation} to create.
   * @return the new {@link BaseAnnotation} instance.
   */
  protected BaseAnnotation createAnnoation(CodeType type) {

    return new BaseAnnotation(this, type);
  }

  @Override
  public void add(CodeAnnotation item) {

    super.add(item);
  }

  @Override
  public CodeAnnotation getDeclaredOrAdd(CodeType type) {

    CodeAnnotation annotation = getDeclared(type);
    if (annotation == null) {
      annotation = add(type);
    }
    return annotation;
  }

  @Override
  public Iterable<? extends CodeAnnotation> getAll() {

    if (this.parent instanceof BaseType) {
      return () -> new TypeAnnotationIterator((BaseType) this.parent);
    } else if (this.parent instanceof CodeMethod) {
      return () -> new MethodAnnotationIterator((CodeMethod) this.parent);
    } else {
      return getDeclared();
    }
  }

  @Override
  public CodeAnnotations getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized() && (this.parent != null)) {
      CodeElement sourceElement = this.parent.getSourceCodeObject();
      if (sourceElement != null) {
        this.sourceCodeObject = sourceElement.getAnnotations();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeAnnotations merge(CodeAnnotations other, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      getList().addAll(other.getDeclared());
    } else if (strategy != CodeMergeStrategy.KEEP) {
      Set<String> annotationTypes = createAnnotationTypeNameSet();
      for (CodeAnnotation annotation : other.getDeclared()) {
        String key = annotation.getType().getQualifiedName();
        if (!annotationTypes.contains(key)) {
          add(annotation);
        }
      }
    }
    return this;
  }

  @Override
  public BaseAnnotations copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseAnnotations copy(CodeCopyMapper mapper) {

    return new BaseAnnotations(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    for (CodeAnnotation annotation : getDeclared()) {
      annotation.write(sink, newline, defaultIndent, currentIndent, language);
      if (defaultIndent == null) {
        sink.append(' ');
      }
    }
  }

  private abstract class AnnotationIterator implements Iterator<CodeAnnotation> {

    protected CodeAnnotation next;

    private boolean done;

    private final Set<CodeType> iteratedAnnotations;

    private Iterator<CodeAnnotation> currentIterator;

    protected AnnotationIterator() {

      super();
      this.iteratedAnnotations = new HashSet<>();
      this.currentIterator = getList().iterator();
    }

    @Override
    public final boolean hasNext() {

      if (this.next != null) {
        return true;
      }
      if (this.done) {
        return false;
      }
      this.next = findNext();
      if (this.next == null) {
        this.done = true;
      }
      return (!this.done);
    }

    @Override
    public final CodeAnnotation next() {

      if (this.next == null) {
        throw new NoSuchElementException();
      } else {
        CodeAnnotation result = this.next;
        this.next = null;
        return result;
      }
    }

    protected CodeAnnotation findNext() {

      while (this.currentIterator.hasNext()) {
        CodeAnnotation annotation = this.currentIterator.next();
        CodeType annotationType = annotation.getType().asType();
        boolean added = this.iteratedAnnotations.add(annotationType);
        if (added) {
          return annotation;
        }
      }
      this.currentIterator = nextParent();
      if (this.currentIterator == null) {
        return null;
      }
      return findNext();
    }

    protected abstract Iterator<CodeAnnotation> nextParent();
  }

  private class MethodAnnotationIterator extends AnnotationIterator {

    private CodeMethod method;

    private MethodAnnotationIterator(CodeMethod method) {

      super();
      this.method = method;
      this.next = findNext();
    }

    @Override
    protected Iterator<CodeAnnotation> nextParent() {

      this.method = this.method.getParentMethod();
      if (this.method == null) {
        return null;
      }
      return this.method.getAnnotations().iterator();
    }
  }

  private class TypeAnnotationIterator extends AnnotationIterator {

    private InternalSuperTypeIterator iterator;

    private TypeAnnotationIterator(BaseType type) {

      super();
      this.iterator = new InternalSuperTypeIterator(type);
      this.next = findNext();
    }

    @Override
    protected Iterator<CodeAnnotation> nextParent() {

      if (this.iterator.hasNext()) {
        this.iterator = this.iterator.next();
      } else {
        return null;
      }
      if (this.iterator == null) {
        return null;
      }
      return this.iterator.getType().asType().getAnnotations().iterator();
    }
  }

}
