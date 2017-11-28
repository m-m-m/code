/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.merge.CodeMergeStrategyDecider;
import net.sf.mmm.code.api.type.CodeNestedTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.node.BaseNodeItemContainerHierarchicalWithName;

/**
 * Base implementation of {@link CodeNestedTypes}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseNestedTypes extends BaseNodeItemContainerHierarchicalWithName<CodeType> implements CodeNestedTypes {

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNestedTypes(BaseNestedTypes template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    CodeNestedTypes sourceNestedTypes = getSourceCodeObject();
    if (sourceNestedTypes != null) {
      BaseContext context = getContext();
      for (CodeType sourceNestedType : sourceNestedTypes.getDeclared()) {
        context.getType(sourceNestedType.getQualifiedName());
      }
    }
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  public List<? extends CodeType> getAll() {

    List<CodeType> list = new ArrayList<>(getList());
    collectNestedTypes(list);
    return list;
  }

  private void collectNestedTypes(List<CodeType> list) {

    for (CodeType nested : getDeclared()) {
      BaseNestedTypes nestedContainer = (BaseNestedTypes) nested.getNestedTypes();
      list.addAll(nestedContainer.getDeclared());
      nestedContainer.collectNestedTypes(list);
    }
  }

  @Override
  protected CodeType get(String name, boolean init) {

    CodeType nestedType = getDeclared(name, init);
    if (nestedType != null) {
      return nestedType;
    }
    for (CodeType nested : getList()) {
      nestedType = nested.getNestedTypes().get(name);
      if (nestedType != null) {
        return nestedType;
      }
    }
    return null;
  }

  @Override
  public BaseType add(String name) {

    BaseType nestedType = new BaseType(this.parent.getFile(), name, this.parent, null);
    add(nestedType);
    return nestedType;
  }

  @Override
  protected CodeType ensureParent(CodeType item) {

    if (item.getParent() != this.parent) {
      return doCopyNode(item, this.parent);
    }
    return item;
  }

  @Override
  protected String getKey(CodeType item) {

    return item.getSimpleName();
  }

  @Override
  protected void add(CodeType item) {

    super.add(item);
  }

  @Override
  protected void rename(CodeType child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  public CodeNestedTypes getSourceCodeObject() {

    CodeType sourceType = this.parent.getSourceCodeObject();
    if (sourceType != null) {
      return sourceType.getNestedTypes();
    }
    return null;
  }

  @Override
  public CodeNestedTypes merge(CodeNestedTypes o, CodeMergeStrategyDecider decider, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseNestedTypes other = (BaseNestedTypes) o;
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeType otherNestedType : other) {
        add(doCopyNode(otherNestedType, this.parent));
      }
    } else {
      for (CodeType otherNestedType : other) {
        String simpleName = otherNestedType.getSimpleName();
        CodeType myNestedType = get(simpleName);
        if (myNestedType == null) {
          add(doCopyNode(otherNestedType, this.parent));
        } else {
          myNestedType.merge(otherNestedType, decider, strategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseNestedTypes copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseNestedTypes copy(CodeCopyMapper mapper) {

    return new BaseNestedTypes(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    List<? extends CodeType> nestedTypes = getDeclared();
    if (nestedTypes.isEmpty()) {
      return;
    }
    String childIndent = currentIndent + defaultIndent;
    for (CodeType nestedType : nestedTypes) {
      sink.append(newline);
      nestedType.write(sink, newline, defaultIndent, childIndent);
    }
  }

}
