/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.type.BaseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

    private final JavaContext parent;

    private final JavaClassLoader loader;

    private static final Logger LOG = LoggerFactory.getLogger(JavaExtendedContext.class);

    /**
     * The constructor.
     *
     * @param source
     *            the {@link #getSource() source}.
     * @param sourceProvider
     *            the {@link BaseSourceProvider}.
     * @param mvnClassLoader
     */
    public JavaExtendedContext(BaseSourceImpl source, BaseSourceProvider sourceProvider, ClassLoader mvnClassLoader) {

        this(JavaRootContext.get(), source, sourceProvider, mvnClassLoader);
    }

    /**
     * The constructor.
     *
     * @param parent
     *            the {@link #getParent() parent context}.
     * @param source
     *            the {@link #getSource() source}.
     * @param sourceProvider
     *            the {@link BaseSourceProvider}.
     * @param mvnClassLoader
     */
    public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider,
        ClassLoader mvnClassLoader) {

        super(source, sourceProvider);

        this.parent = parent;

        if (mvnClassLoader == null) {
            loader = new JavaClassLoader();
        } else {
            loader = new JavaClassLoader(mvnClassLoader);
        }

    }

    /**
     * The constructor.
     *
     * @param parent
     *            the {@link #getParent() parent context}.
     * @param source
     *            the {@link #getSource() source}.
     * @param sourceProvider
     *            the {@link BaseSourceProvider}.
     */
    public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider,
        File mavenProjectLocation) {

        super(source, sourceProvider);
        this.parent = parent;
        loader = new JavaClassLoader();
    }

    @Override
    public BaseLoader getLoader() {

        return loader;
    }

    @Override
    public BaseType getRootExceptionType() {

        return parent.getRootExceptionType();
    }

    @Override
    public BaseType getNonPrimitiveType(BaseType javaType) {

        return parent.getNonPrimitiveType(javaType);
    }

    @Override
    public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

        return parent.getQualifiedNameForStandardType(simpleName, omitStandardPackages);
    }

    @Override
    public ClassLoader getClassLoader() {

        return loader.getClassLoader();
    }

}
