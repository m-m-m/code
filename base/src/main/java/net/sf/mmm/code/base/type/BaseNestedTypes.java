/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeNestedTypes;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.node.BaseNodeItemContainerHierarchicalWithName;

/**
 * Base implementation of {@link CodeNestedTypes}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseNestedTypes extends BaseNodeItemContainerHierarchicalWithName<BaseType>
    implements CodeNestedTypes<BaseType>, CodeNodeItemWithGenericParent<BaseType, BaseNestedTypes> {

  private final BaseType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseNestedTypes(BaseType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNestedTypes} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseNestedTypes(BaseNestedTypes template, BaseType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    BaseNestedTypes sourceNestedTypes = getSourceCodeObject();
    if (sourceNestedTypes != null) {
      BaseContext context = getContext();
      for (BaseType sourceNestedType : sourceNestedTypes.getDeclared()) {
        context.getType(sourceNestedType.getQualifiedName());
      }
    }
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  public BaseType getDeclaringType() {

    return this.parent;
  }

  @Override
  public List<? extends BaseType> getAll() {

    List<BaseType> list = new ArrayList<>(getList());
    collectNestedTypes(list);
    return list;
  }

  private void collectNestedTypes(List<BaseType> list) {

    for (BaseType nested : getDeclared()) {
      BaseNestedTypes nestedContainer = nested.getNestedTypes();
      list.addAll(nestedContainer.getDeclared());
      nestedContainer.collectNestedTypes(list);
    }
  }

  @Override
  protected BaseType get(String name, boolean init) {

    BaseType nestedType = getDeclared(name, init);
    if (nestedType != null) {
      return nestedType;
    }
    for (BaseType nested : getDeclared()) {
      nestedType = nested.getNestedTypes().get(name);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  @Override
  public BaseType add(String name) {

    BaseType nestedType = new BaseType(getDeclaringType().getFile(), name, getDeclaringType(), null);
    add(nestedType);
    return nestedType;
  }

  @Override
  protected void add(BaseType item) {

    super.add(item);
  }

  @Override
  protected void rename(BaseType child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  public BaseNestedTypes getSourceCodeObject() {

    BaseType sourceType = this.parent.getSourceCodeObject();
    if (sourceType != null) {
      return sourceType.getNestedTypes();
    }
    return null;
  }

  @Override
  public BaseNestedTypes copy() {

    return copy(this.parent);
  }

  @Override
  public BaseNestedTypes copy(BaseType newParent) {

    return new BaseNestedTypes(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    List<? extends BaseType> nestedTypes = getDeclared();
    if (nestedTypes.isEmpty()) {
      return;
    }
    String childIndent = currentIndent + defaultIndent;
    for (BaseType nestedType : nestedTypes) {
      sink.append(newline);
      nestedType.write(sink, newline, defaultIndent, childIndent);
    }
  }

}