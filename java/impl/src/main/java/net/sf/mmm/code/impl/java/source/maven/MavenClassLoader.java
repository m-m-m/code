package net.sf.mmm.code.impl.java.source.maven;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MavenClassLoader extends ClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(MavenClassLoader.class);

    private File byteCodeLocation;

    public MavenClassLoader(File byteCodeLocation) {
        super();
        this.byteCodeLocation = byteCodeLocation;
    }

    @Override
    public Class<?> findClass(String name) {
        byte[] bt = loadClassData(name);
        if (bt.length <= 0) {
            return null;
        }
        return defineClass(name, bt, 0, bt.length);
    }

    private byte[] loadClassData(String className) {
        // Let's try to find the class file
        File classFile = byteCodeLocation.toPath().resolve(className.replace(".", "/") + ".class").toFile();
        ByteArrayOutputStream byteSt = new ByteArrayOutputStream();
        // read class
        try (InputStream is = new FileInputStream(classFile);) {

            // write into byte
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    byteSt.write(len);
                }
            } catch (IOException e) {
                LOG.debug("Failed to write class {} into bytes.", className, e);
            }

            // convert into byte array
            return byteSt.toByteArray();
        } catch (FileNotFoundException e) {
            LOG.debug("Failed to find class file: {}", className, e);
        } catch (IOException e) {
            LOG.debug("Failed to automatically close the inputstream.", e);
        }
        return byteSt.toByteArray();
    }

}