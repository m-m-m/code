package io.github.mmm.code.impl.java;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.member.BaseField;
import io.github.mmm.code.base.member.BaseMethod;
import io.github.mmm.code.base.type.BaseType;

/**
 * Test of writing Java source code.
 */
public class JavaCodeWriterTest extends Assertions {

  @Test
  public void testWriteRecord() {

    JavaRootContext context = JavaRootContext.get();
    BaseType object = context.getRootType();
    BaseType record = new BaseType(new BaseFile(object.getParentPackage(), "MyRecord"), "MyRecord", null);
    record.setCategory(CodeTypeCategory.RECORD);
    BaseField name = record.getFields().add("name");
    name.setType(context.getType(String.class));
    BaseField foo = record.getFields().add("foo");
    foo.setType(object);
    String code = record.write();
    assertThat(code).isEqualTo("public record MyRecord(String name, Object foo) {\n" //
        + "}\n");
  }

  @Test
  public void testWriteClass() {

    JavaRootContext context = JavaRootContext.get();
    BaseType object = context.getRootType();
    BaseType type = new BaseType(new BaseFile(object.getParentPackage(), "MyClass"), "MyClass", null);
    type.getDoc().add("{@link MyClass} is the coolest class in the universe.");
    BaseField name = type.getFields().add("name");
    name.setType(context.getType(String.class));
    BaseField foo = type.getFields().add("foo");
    foo.setType(object);
    CodeConstructor constructor = type.getConstructors().add();
    constructor.getBody().addText("super();");
    constructor.getDoc().add("The constructor.");
    type.createGettersAndSetters();
    String code = type.write();
    assertThat(code).isEqualTo("/** {@link MyClass} is the coolest class in the universe. */\n" //
        + "public class MyClass {\n" //
        + "\n" //
        + "  private String name;\n" //
        + "\n" //
        + "  private Object foo;\n" //
        + "\n" //
        + "  /**\n" //
        + "   * The constructor.\n" //
        + "   */\n" //
        + "  public MyClass() {\n" //
        + "    super();\n" //
        + "  }\n" //
        + "\n" //
        + "  public String getName() {\n" //
        + "    return this.name;\n" //
        + "  }\n" //
        + "\n" //
        + "  public void setName(String name) {\n" //
        + "    this.name = name;\n" //
        + "  }\n" //
        + "\n" //
        + "  public Object getFoo() {\n" //
        + "    return this.foo;\n" //
        + "  }\n" //
        + "\n" //
        + "  public void setFoo(Object foo) {\n" //
        + "    this.foo = foo;\n" //
        + "  }\n" //
        + "}\n");
  }

  @Test
  public void testWriteInterface() {

    JavaRootContext context = JavaRootContext.get();
    BaseType object = context.getRootType();
    BaseType type = new BaseType(new BaseFile(object.getParentPackage(), "MyInterface"), "MyInterface", null);
    type.setCategory(CodeTypeCategory.INTERFACE);
    type.getDoc().add("This interface gives access to the universe.");
    BaseMethod destroy = type.getMethods().add("destroy");
    destroy.getDoc().add("Destroys the universe.");
    destroy.getDoc().add("You should never call this method or everybody will be in great danger!");
    CodeParameter parameter = destroy.getParameters().add("masterKey");
    parameter.setType(context.getType(String.class));
    parameter.getDoc().add("the master key to the universe.");
    parameter.getDoc().add("See {@link God} for details.");
    type.createGettersAndSetters();
    String code = type.write();
    assertThat(code).isEqualTo("/** This interface gives access to the universe. */\n" //
        + "public interface MyInterface {\n" //
        + "\n" //
        + "  /**\n" //
        + "   * Destroys the universe.\n" //
        + "   * You should never call this method or everybody will be in great danger!\n" //
        + "   * @param masterKey the master key to the universe.\n" //
        + "   *        See {@link God} for details.\n" //
        + "   */\n" //
        + "  void destroy(String masterKey);\n" //
        + "}\n");
  }

}
