/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.CodeLanguageJava;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;
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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemImpl(BaseNodeItemImpl template, CodeCopyMapper mapper) {

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
   * @param <N> type of the {@link CodeNodeItemCopyable}.
   * @param <E> type of the actual {@link List} elements.
   * @param list the {@link List} to {@link CodeNodeItemCopyable#copy(CodeNode) copy}.
   * @param newParent the new {@link CodeNodeItemCopyable#getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   * @return an mutable deep-copy of the {@link List}.
   */
  @SuppressWarnings("unchecked")
  protected <P extends CodeNode, N extends CodeNodeItemCopyable<P, N>, E extends N> List<E> doCopyList(List<E> list, P newParent, CodeCopyMapper mapper) {

    List<E> copy = new ArrayList<>(list.size());
    for (E node : list) {
      copy.add((E) node.copy(newParent, mapper));
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

    return (T) doCopyNodeItemWithParent((CodeNodeItemCopyable) node, newParent);
  }

  /**
   * @param <P> type of the parent.
   * @param <N> type of the {@link CodeNodeItemCopyable} to copy.
   * @param node the {@link CodeNodeItemCopyable} to copy.
   * @param newParent the new {@link #getParent() parent node}.
   * @return the copy.
   */
  protected <P extends CodeNode, N extends CodeNodeItemCopyable<P, N>> N doCopyNodeItemWithParent(N node, P newParent) {

    return node.copy(newParent);
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
