/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.copy.AbstractCodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.language.JavaLanguage;
import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.node.CodeNodeItem;
import io.github.mmm.code.api.source.CodeSource;
import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.item.BaseMutableItem;
import io.github.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link CodeNodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseNodeItem extends BaseMutableItem implements CodeNodeItem {

  private static final Logger LOG = LoggerFactory.getLogger(BaseNodeItem.class);

  /**
   * The constructor.
   */
  public BaseNodeItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItem} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItem(BaseNodeItem template, CodeCopyMapper mapper) {

    super(template);
    if (mapper instanceof AbstractCodeCopyMapper) {
      ((AbstractCodeCopyMapper) mapper).registerMapping(template, this);
    }
  }

  /**
   * Called from {@link #initialize()} on first invocation. May be overridden but never be called from anywhere else.
   */
  @Override
  protected void doInitialize() {

    if (LOG.isTraceEnabled()) {
      LOG.trace("Initializing {}", toPathString());
    }
    super.doInitialize();
  }

  @Override
  public BaseContext getContext() {

    CodeNode parent = getParent();
    if (parent != null) {
      return (BaseContext) parent.getContext();
    }
    return null;
  }

  @Override
  public BaseSource getSource() {

    CodeNode parent = getParent();
    if (parent != null) {
      return (BaseSource) parent.getSource();
    }
    return null;
  }

  @Override
  public CodeLanguage getLanguage() {

    BaseContext context = getContext();
    if (context == null) {
      return JavaLanguage.get(); // only during initialization (e.g. debugging in constructor)
    }
    return context.getLanguage();
  }

  /**
   * @return a {@link String} representation of the entire node tree to this node.
   */
  public String toPathString() {

    StringBuilder buffer = new StringBuilder();
    toPathString(buffer);
    return buffer.toString();
  }

  private void toPathString(StringBuilder buffer) {

    CodeNode parent = getParent();
    if (parent == null) {
      LOG.debug("Node item {} without parent.", this);
    } else if (parent instanceof CodeSource) {
      buffer.append(parent.toString());
    } else if (parent instanceof BaseNodeItem) {
      ((BaseNodeItem) parent).toPathString(buffer);
    } else {
      buffer.append(parent.toString());
    }
    buffer.append('/');
    buffer.append(getClass().getSimpleName());
    buffer.append(':');
    try {
      doWrite(buffer, "", null, null, getLanguage());
    } catch (Exception e) {
      LOG.debug("{}.toString() failed!", getClass().getSimpleName(), e);
    }
  }

  /**
   * @param <I> type of the {@link CodeItem}.
   * @param container the {@link BaseNodeItemContainerWithName}.
   * @param name - see {@link BaseNodeItemContainerWithName#get(String, boolean)}.
   * @param init - see {@link BaseNodeItemContainerWithName#get(String, boolean)}.
   * @return see {@link BaseNodeItemContainerWithName#get(String, boolean)}.
   */
  protected static <I extends CodeItem> I getContainerItem(BaseNodeItemContainerWithName<I> container, String name, boolean init) {

    return container.get(name, init);
  }

  /**
   * @param <I> type of the {@link CodeItem}.
   * @param container the {@link BaseNodeItemContainerWithName}.
   * @param name - see {@link BaseNodeItemContainerWithName#getDeclared(String, boolean)}.
   * @param init - see {@link BaseNodeItemContainerWithName#getDeclared(String, boolean)}.
   * @return see {@link BaseNodeItemContainerWithName#getDeclared(String, boolean)}.
   */
  protected static <I extends CodeItem> I getContainerItemDeclared(BaseNodeItemContainerWithName<I> container, String name, boolean init) {

    return container.getDeclared(name, init);
  }

}
