/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.CodeLanguageJava;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.item.BaseMutableItem;
import net.sf.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link BaseNodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseNodeItemImpl extends BaseMutableItem implements BaseNodeItem {

  private static final Logger LOG = LoggerFactory.getLogger(BaseNodeItemImpl.class);

  /**
   * The constructor.
   */
  public BaseNodeItemImpl() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItemImpl} to copy.
   */
  public BaseNodeItemImpl(BaseNodeItemImpl template) {

    super(template);
  }

  /**
   * Called from {@link #initialize()} on first invocation. May be overridden but never be called from
   * anywhere else.
   */
  @Override
  protected void doInitialize() {

    if (LOG.isTraceEnabled()) {
      LOG.trace("Initializing {}", toPathString());
    }
    super.doInitialize();
  }

  /**
   * @param <P> type of the parent.
   * @param <N> type of the {@link List} elements.
   * @param list the {@link List} to {@link CodeNodeItemWithGenericParent#copy(CodeNode) copy}.
   * @param newParent the new {@link CodeNodeItemWithGenericParent#getParent() parent}.
   * @return an mutable deep-copy of the {@link List}.
   */
  protected <P extends CodeNode, N extends CodeNodeItemWithGenericParent<P, N>> List<N> doCopy(List<N> list, P newParent) {

    List<N> copy = new ArrayList<>(list.size());
    for (N node : list) {
      copy.add(node.copy(newParent));
    }
    return copy;
  }

  /**
   * @param <T> type of the resulting {@link CodeNodeItem}.
   * @param <N> type of the {@link CodeNodeItem} to copy.
   * @param node the {@link CodeNodeItem} to copy.
   * @param newParent the new {@link #getParent() parent node}.
   * @return the copy.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected <N extends CodeNodeItem, T extends N> T doCopy(N node, CodeNode newParent) {

    return (T) doCopyNodeItemWithParent((CodeNodeItemWithGenericParent) node, newParent);
  }

  /**
   * @param <P> type of the parent.
   * @param <N> type of the {@link CodeNodeItemWithGenericParent} to copy.
   * @param node the {@link CodeNodeItemWithGenericParent} to copy.
   * @param newParent the new {@link #getParent() parent node}.
   * @return the copy.
   */
  protected <P extends CodeNode, N extends CodeNodeItemWithGenericParent<P, N>> N doCopyNodeItemWithParent(N node, P newParent) {

    return node.copy(newParent);
  }

  @Override
  public BaseContext getContext() {

    BaseNode parent = getParent();
    if (parent != null) {
      return parent.getContext();
    }
    return null;
  }

  @Override
  public BaseSource getSource() {

    BaseNode parent = getParent();
    if (parent != null) {
      return parent.getSource();
    }
    return null;
  }

  @Override
  public CodeLanguage getLanguage() {

    BaseContext context = getContext();
    if (context == null) {
      return CodeLanguageJava.INSTANCE; // only during initialization (e.g. debugging in constructor)
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
    } else if (parent instanceof BaseNodeItemImpl) {
      ((BaseNodeItemImpl) parent).toPathString(buffer);
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
