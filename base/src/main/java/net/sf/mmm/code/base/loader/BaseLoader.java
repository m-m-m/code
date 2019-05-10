/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import net.sf.mmm.code.api.CodeLoader;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * {@link CodeLoader} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseLoader extends CodeLoader {

    @Override
    BaseContext getContext();

    @Override
    BaseSource getSource();

    @Override
    BaseType getType(String qualifiedName);

    @Override
    BaseType getType(CodeName qualifiedName);

    /**
     * @param clazz
     *            the {@link Class} to get as {@link CodeGenericType}.
     * @return the existing or otherwise newly created {@link CodeGenericType}. Typically a {@link CodeType}
     *         but may also be a {@link CodeType#createArray() array type} in case an {@link Class#isArray()
     *         array} was given.
     */
    @Override
    BaseGenericType getType(Class<?> clazz);

    /**
     * @return current class loader
     */
    ClassLoader getClassLoader();

}
