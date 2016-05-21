package org.estatio.dom.utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.isis.applib.util.TitleBufferException;

public class TitleBuilder {
    private static final String SPACE = " ";
    private static final String SEPARATOR = ":";
    private static final String DELIMITER = "[]";
    private static final String PARENT_SEPARATOR = " > ";

    public static final Class[] NO_PARAMETER_TYPES = new Class[0];
    public static final Object[] NO_ARGUMENTS = new Object[0];

    private final StringBuilder parentString;
    private final StringBuilder nameString;
    private final StringBuilder referenceString;

    public static boolean isEmpty(Object object) {
        String title = titleFor(object);
        return isEmpty(title);
    }

    private static String titleFor(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return object.toString();
        } else {
            try {
                Method e = object.getClass().getMethod("title", NO_PARAMETER_TYPES);
                return (String) e.invoke(object, NO_ARGUMENTS);
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException var2) {
                throw new TitleBufferException(var2);
            } catch (NoSuchMethodException var3) {
                return object.toString();
            }
        }
    }

    public static boolean isEmpty(String text) {
        return text == null || text.equals("");
    }

    private TitleBuilder() {
        this.referenceString = new StringBuilder();
        this.nameString = new StringBuilder();
        this.parentString = new StringBuilder();
    }

    public static TitleBuilder start() {
        return new TitleBuilder();
    }

    public TitleBuilder withParent(final Object object){
        parentString.append(titleFor(object));
        parentString.append(PARENT_SEPARATOR);
        return this;
    }

    public TitleBuilder withName(final String name){
        if (name != null && name.length() > 0 ) {
            if (nameString.length() > 0){
                nameString.append(SPACE);
            }
            nameString.append(name);
        }
        return this;
    }

    public TitleBuilder withReference(final String reference){
        referenceString.append(reference);
        return this;
    }

    public String toString() {
        return new StringBuilder()
                .append(parentString)
                .append(nameString)
                .append(referenceString.length() > 0 && nameString.length() > 0 ? SPACE : "")
                .append(!referenceString.toString().equals(nameString.toString()) ? delimited(referenceString) : "")
                .toString().trim();
    }

    private StringBuilder delimited(StringBuilder stringBuilder){
        return stringBuilder.length()>0 ? stringBuilder.insert(0,DELIMITER.charAt(0)).append(DELIMITER.charAt(1)) : stringBuilder;
    }

    public TitleBuilder withName(final Object object) {
        nameString.append(titleFor(object));
        return this;
    }

    public TitleBuilder withTupleElement(Object object) {
        return this;
    }
}
