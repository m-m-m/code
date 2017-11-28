/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeComposedType;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.BaseContext;

/**
 * Base implementation of {@link CodeComposedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseComposedType extends BaseGenericType implements CodeComposedType {

  private final BaseTypePlaceholder parent;

  private Type[] bounds;

  private List<BaseGenericType> types;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseComposedType(BaseTypePlaceholder parent) {

    this(parent, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param bounds the optional bounds if created from reflective object, otherwise {@code null}.
   */
  public BaseComposedType(BaseTypePlaceholder parent, Type[] bounds) {

    super();
    this.parent = parent;
    this.bounds = bounds;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseComposedType} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseComposedType(BaseComposedType template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.bounds = null;
    this.types = doMapList(template.types, mapper, CodeCopyType.REFERENCE);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = Collections.unmodifiableList(this.types);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getTypes();
  }

  @Override
  public BaseTypePlaceholder getParent() {

    return this.parent;
  }

  /**
   * @return the {@link TypeVariable} or {@link WildcardType} declaring this composed type via its bounds.
   */
  @Override
  public Type getReflectiveObject() {

    return this.parent.getReflectiveObject();
  }

  @Override
  public BaseType getDeclaringType() {

    return null;
  }

  @Override
  public List<? extends CodeGenericType> getTypes() {

    if (this.types == null) {
      if (this.bounds == null) {
        this.types = new ArrayList<>();
      } else {
        assert (this.bounds.length > 1);
        this.types = new ArrayList<>(this.bounds.length);
        BaseContext context = getContext();
        for (Type bound : this.bounds) {
          BaseGenericType type = context.getType((Class<?>) bound);
          this.types.add(type);
        }
        this.bounds = null;
      }
    }
    return this.types;
  }

  @Override
  public void add(CodeGenericType type) {

    verifyMutalbe();
    getTypes();
    this.types.add((BaseGenericType) type);
  }

  @Override
  public BaseType asType() {

    if (this.types.isEmpty()) {
      return getContext().getRootType();
    }
    return this.types.get(0).asType();
  }

  @Override
  public BaseGenericType resolve(CodeGenericType context) {

    // can not resolve any further...
    return this;
  }

  @Override
  public String getSimpleName() {

    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (CodeGenericType type : getTypes()) {
      sb.append(prefix);
      sb.append(type.getSimpleName());
      prefix = " & ";
    }
    return sb.toString();
  }

  @Override
  public String getQualifiedName() {

    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (CodeGenericType type : getTypes()) {
      sb.append(prefix);
      sb.append(type.getQualifiedName());
      prefix = " & ";
    }
    return sb.toString();
  }

  @Override
  public BaseComposedType copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseComposedType copy(CodeCopyMapper mapper) {

    return new BaseComposedType(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, null, "", language);
    writeReference(sink, true);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    String prefix = "";
    for (CodeGenericType type : this.types) {
      sink.append(prefix);
      type.writeReference(sink, declaration, qualified);
      prefix = " & ";
    }
  }

}
