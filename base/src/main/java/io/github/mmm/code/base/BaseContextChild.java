package io.github.mmm.code.base;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.api.element.CodeElementWithDeclaringType;
import io.github.mmm.code.base.loader.BaseSourceLoader;
import io.github.mmm.code.base.loader.BaseSourceLoaderRootPackage;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.source.BaseSourceDescriptorBean;
import io.github.mmm.code.base.source.BaseSourceImpl;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseType;

/**
 * Implementation of {@link AbstractBaseContext} for {@link #createChildContext() child context}.
 *
 * @since 1.0.0
 */
public class BaseContextChild extends AbstractBaseContext {

  private final AbstractBaseContext parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseContextChild(AbstractBaseContext parent) {

    this(parent, createChildSource(parent));
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param source the {@link #getSource() source}.
   */
  protected BaseContextChild(AbstractBaseContext parent, BaseSourceImpl source) {

    super(source);
    this.parent = parent;
  }

  private static BaseSourceImpl createChildSource(AbstractBaseContext parent) {

    BaseSource source = parent.getSource();
    BaseSourceDescriptorBean descriptor = new BaseSourceDescriptorBean(source.getDescriptor());
    descriptor.setArtifactId(descriptor.getArtifactId() + "-child");
    descriptor.setVersion("SNAPSHOT");
    List<BaseSource> dependencies = Arrays.asList(source);
    BaseSourceLoader loader = new BaseSourceLoaderRootPackage();
    return new BaseSourceImpl(null, null, null, source.getId() + "-child", descriptor, dependencies, loader, false);
  }

  @Override
  public AbstractBaseContext getParent() {

    return this.parent;
  }

  @Override
  public BaseGenericType getType(Type type, CodeElementWithDeclaringType declaringElement) {

    return this.parent.getType(type, declaringElement);
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    BaseType type = getSource().getLoader().getType(qualifiedName);
    if (type == null) {
      type = this.parent.getType(qualifiedName);
    }
    return type;
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    return this.parent.getType(clazz);
  }

}
