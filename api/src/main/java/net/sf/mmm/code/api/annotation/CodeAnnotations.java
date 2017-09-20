/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.annotation;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainerHierarchical} containing {@link CodeAnnotation}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <A> the type of the contained {@link CodeAnnotation}s.
 * @since 1.0.0
 */
public interface CodeAnnotations<A extends CodeAnnotation> extends CodeNodeItemContainerHierarchical<A> {

  @Override
  CodeElement getParent();

  /**
   * <b>Attention:</b><br>
   * This method can be expensive as it has to traverse inherited elements. However, due to the usage of
   * {@link Iterable} implementations can lazily traverse.
   *
   * @see net.sf.mmm.code.api.member.CodeMethod#getParentMethod()
   */
  @Override
  Iterable<? extends A> getAll();

  /**
   * @param type the {@link CodeAnnotation#getType() type} reflecting the {@link CodeType#isAnnotation()
   *        annotation}.
   * @return the new {@link CodeAnnotation} that has been added.
   */
  A add(CodeType type);

  /**
   * @param type the {@link CodeAnnotation#getType() type} reflecting the {@link CodeType#isAnnotation()
   *        annotation}.
   * @return the {@link CodeAnnotation} from {@link #getDeclared()} {@link CodeAnnotation#getType() with} the
   *         give {@link CodeType} or {@code null} if not found.
   */
  A getDeclared(CodeType type);

  /**
   * @param type the {@link CodeAnnotation#getType() type} reflecting the {@link CodeType#isAnnotation()
   *        annotation}.
   * @return the {@link CodeAnnotation} from {@link #getDeclared()} {@link CodeAnnotation#getType() with} the
   *         give {@link CodeType} or a new {@link #add(CodeType) added} one.
   */
  default A getDeclaredOrAdd(CodeType type) {

    A annotation = getDeclared(type);
    if (annotation == null) {
      annotation = add(type);
    }
    return annotation;
  }

  @Override
  CodeAnnotations<A> copy();

}
