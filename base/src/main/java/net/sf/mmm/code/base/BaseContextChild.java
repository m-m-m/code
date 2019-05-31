package net.sf.mmm.code.base;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoaderRootPackage;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceDescriptorBean;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

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
  public BaseContext getParent() {

    return parent;
  }

  @Override
  public BaseGenericType getType(Type type, CodeElementWithDeclaringType declaringElement) {

    return parent.getType(type, declaringElement);
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    BaseType type = getSource().getLoader().getType(qualifiedName);
    if (type == null) {
      type = parent.getType(qualifiedName);
    }
    return type;
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    return parent.getType(clazz);
  }

  @Override
  public ClassLoader getClassLoader() {

    return parent.getClassLoader();
  }

}
