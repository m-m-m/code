/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.item;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperDefault;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeMutableItem;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Base implementation of {@link CodeMutableItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseMutableItem extends BaseItem implements CodeMutableItem {

  private static final Logger LOG = LoggerFactory.getLogger(BaseMutableItem.class);

  private static Charset defaultEncoding;

  private boolean immutable;

  private int initialized;

  /**
   * The constructor.
   */
  public BaseMutableItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMutableItem} to copy.
   */
  public BaseMutableItem(BaseMutableItem template) {

    super();
    template.initialize();
    // immutable flag is not copied on purpose
    LOG.trace("Copying item {}", template);
  }

  /**
   * Initializes this node item.
   */
  protected final void initialize() {

    if (this.initialized == 0) {
      this.initialized++;
      doInitialize();
      boolean systemImmutable = isSystemImmutable();
      if (systemImmutable) {
        setImmutable();
      }
      this.initialized++;
      doneInitialize();
    }
  }

  /**
   * @return {@code true} if the {@link #initialize() initialization} of this item has started but is not yet
   *         {@link #isInitialized() complete}, {@code false} otherwise.
   */
  protected final boolean isInitializing() {

    return (this.initialized == 1);
  }

  /**
   * @return {@code true} if the {@link #initialize() initialization} of this item has been completed, {@code false}
   *         otherwise.
   */
  protected final boolean isInitialized() {

    return (this.initialized >= 2);
  }

  /**
   * Called from {@link #initialize()} on first invocation. May be overridden but never be called from anywhere else.
   */
  protected void doInitialize() {

    if (this.initialized != 1) {
      throw new IllegalStateException("Already initialized!");
    }
  }

  /**
   * Called from {@link #initialize()} on first invocation after {@link #doInitialize()} is complete. May be overridden
   * but never be called from anywhere else.
   */
  protected void doneInitialize() {

    if (this.initialized != 2) {
      throw new IllegalStateException("Not initialized!");
    }
  }

  /**
   * @param init {@code true} to call {@link #initialize()}, {@code false} to do nothing.
   */
  protected final void initialize(boolean init) {

    if (init) {
      initialize();
    }
  }

  @Override
  public boolean isImmutable() {

    if (this.immutable) {
      return true;
    }
    if (isSystemImmutable()) {
      return true;
    }
    return false;
  }

  @Override
  public Object getReflectiveObject() {

    return null;
  }

  /**
   * @return the optional internal {@link CodeItem} representing the source-code (to merge). Otherwise {@code null}.
   *         This is an internal API. Do not use or rely on it from outside.
   */
  public CodeItem getSourceCodeObject() {

    return null;
  }

  /**
   * @return {@code true} if this is a system internal node item that is considered to be {@link #isImmutable()
   *         immutable} but is technically {@link #setImmutable() set to immutable} during (lazy) {@link #initialize()
   *         initialization}. Otherwise {@code false}.
   */
  protected boolean isSystemImmutable() {

    if (getReflectiveObject() != null) {
      return true;
    }
    return false;
  }

  /**
   * @param item the {@link BaseMutableItem}.
   * @return the result of {@link #isSystemImmutable()}.
   */
  protected static boolean isSystemImmutable(BaseMutableItem item) {

    if (item == null) {
      return false;
    }
    return item.isSystemImmutable();
  }

  /**
   * Calls {@link #setImmutable()} but only if not {@link #isSystemImmutable() system immutable}. Use this method for
   * implementations of {@link #doSetImmutable()} to propagate immutable-flag to children in order to prevent eager
   * initialization.
   */
  public void setImmutableIfNotSystemImmutable() {

    if (!isSystemImmutable()) {
      setImmutable();
    }
  }

  /**
   * Makes this item {@link #isImmutable() immutable}.
   */
  @Override
  public void setImmutable() {

    if (this.immutable) {
      return;
    }
    initialize();
    doSetImmutable();
    this.immutable = true;
  }

  /**
   * Called on the first call of {@link #setImmutable()}. Has to be overridden to update {@link java.util.Collection}s,
   * make child items immutable, etc.
   */
  protected void doSetImmutable() {

    initialize();
  }

  /**
   * Verifies that this item is not {@link #isImmutable() immutable}. Call this method from any edit-method (setter,
   * etc.).
   *
   * @throws ReadOnlyException if this item is immutable.
   */
  protected void verifyMutalbe() {

    if (isImmutable()) {
      String detail = toString();
      String object = getClass().getSimpleName();
      if (!detail.isEmpty()) {
        object = object + ":" + detail;
      }
      throw new ReadOnlyException(object);
    }
  }

  /**
   * @param <T> the type of the {@link List} elements.
   * @param list the {@link List} to make immutable.
   * @return an immutable copy of the {@link List}.
   */
  protected <T extends CodeItem> List<T> makeImmutable(List<T> list) {

    return makeImmutable(list, true);
  }

  /**
   * @param <T> the type of the {@link List} elements.
   * @param list the {@link List} to make immutable.
   * @param disconnect - {@code true} to disconnect the returned, immutable {@link List} from the given {@link List},
   *        {@code false} otherwise (to make it an immutable view on it).
   * @return an immutable copy of the {@link List}.
   */
  @SuppressWarnings("unchecked")
  protected <T extends CodeItem> List<T> makeImmutable(List<T> list, boolean disconnect) {

    if (list.isEmpty()) {
      if (disconnect) {
        return Collections.emptyList();
      }
    } else {
      T item = list.get(0);
      if (item instanceof CodeNodeItem) {
        for (CodeNodeItem element : (List<? extends CodeNodeItem>) list) {
          element.setImmutable();
        }
      }
    }
    List<T> delegate = list;
    if (disconnect) {
      delegate = new ArrayList<>(list);
    }
    return Collections.unmodifiableList(delegate);
  }

  /**
   * @return the default implementation of {@link CodeCopyMapper}.
   */
  protected CodeCopyMapper getDefaultCopyMapper() {

    return new CodeCopyMapperDefault();
  }

  /**
   * @param <N> type of the {@link CodeNodeItemCopyable}.
   * @param list the {@link List} to {@link CodeNodeItem#copy(CodeCopyMapper) copy}.
   * @param mapper the {@link CodeCopyMapper}.
   * @param type the {@link CodeCopyType}.
   * @return an mutable deep-copy of the {@link List}.
   */
  protected <N extends CodeNode> List<N> doMapList(List<N> list, CodeCopyMapper mapper, CodeCopyType type) {

    if (type == null) {
      return new ArrayList<>(list);
    }
    List<N> copy = new ArrayList<>(list.size());
    for (N node : list) {
      N mappedNode = mapper.map(node, type);
      if (mappedNode != null) {
        copy.add(mappedNode);
      }
    }
    return copy;
  }

  /**
   * @param <N> type of the {@link CodeNodeItem} to copy.
   * @param node the {@link CodeNodeItem} to copy.
   * @param parent the new {@link CodeNodeItemCopyable#getParent() parent}.
   * @return the copy.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <N extends CodeNodeItem> N doCopyNodeUnsafe(N node, CodeNodeItem parent) {

    return (N) doCopyNode((CodeNodeItemCopyable) node, parent);
  }

  /**
   * @param <P> type of the {@link CodeNodeItemCopyable#getParent() parent}.
   * @param <N> type of the {@link CodeNodeItem} to copy.
   * @param node the {@link CodeNodeItem} to copy.
   * @param parent the new {@link CodeNodeItemCopyable#getParent() parent}.
   * @return the copy.
   */
  protected <P extends CodeNodeItem, N extends CodeNodeItemCopyable<P, N>> N doCopyNode(N node, P parent) {

    CodeCopyMapperDefault mapper = new CodeCopyMapperDefault();
    mapper.registerMapping(node.getParent(), parent);
    return node.copy(mapper);
  }

  /**
   * @see net.sf.mmm.code.api.node.CodeNodeWithFileWriting#write(Path)
   *
   * @param item the {@link CodeItem} to {@link CodeItem#write(Appendable) write} to a new or existing file.
   * @param targetFolder the {@link Path} pointing to the existing folder where to write to.
   * @param filename the name of the file to write.
   * @param encoding the {@link Charset} to use (typically UTF-8).
   */
  protected void writeItem(CodeItem item, Path targetFolder, String filename, Charset encoding) {

    try {
      Path itemPath = targetFolder.resolve(filename);
      try (OutputStream out = Files.newOutputStream(itemPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
        Writer writer = new OutputStreamWriter(out, encoding);
        item.write(writer);
        writer.close();
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.WRITE);
    }
  }

  /**
   * @return the default {@link Charset} used by {@link net.sf.mmm.code.api.node.CodeNodeWithFileWriting#write(Path)}
   */
  protected static Charset getDefaultEncoding() {

    if (defaultEncoding == null) {
      try {
        defaultEncoding = Charset.forName("UTF-8");
      } catch (Exception e) {
        defaultEncoding = Charset.defaultCharset();
      }
    }
    return defaultEncoding;
  }

}
