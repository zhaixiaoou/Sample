package com.zxo.finder.plugin;

import com.android.tools.r8.graph.S;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

/**
 * 转换类
 */
public class TransformUtil {

    /** The type of internal names. See {@link #appendDescriptor}. */
    public static final int INTERNAL_NAME = 0;

    /** The type of field descriptors. See {@link #appendDescriptor}. */
    public static final int FIELD_DESCRIPTOR = 1;

    /** The type of field signatures. See {@link #appendDescriptor}. */
    public static final int FIELD_SIGNATURE = 2;

    /** The type of method descriptors. See {@link #appendDescriptor}. */
    public static final int METHOD_DESCRIPTOR = 3;

    /** The type of method signatures. See {@link #appendDescriptor}. */
    public static final int METHOD_SIGNATURE = 4;

    /** The type of class signatures. See {@link #appendDescriptor}. */
    public static final int CLASS_SIGNATURE = 5;


    /** The names of the labels. */
    protected static Map<Label, String> labelNames;

    public static String transformAccess(final int accessFlags) {
        if ((accessFlags & Opcodes.ACC_PUBLIC) != 0) {
            return "public ";
        }
        if ((accessFlags & Opcodes.ACC_PRIVATE) != 0) {
            return "private ";
        }
        if ((accessFlags & Opcodes.ACC_PROTECTED) != 0) {
            return "protecte ";
        }
        if ((accessFlags & Opcodes.ACC_FINAL) != 0) {
            return "final ";
        }
        if ((accessFlags & Opcodes.ACC_STATIC) != 0) {
            return "static ";
        }
        if ((accessFlags & Opcodes.ACC_SYNCHRONIZED) != 0) {
            return "synchronzed ";
        }
        if ((accessFlags & Opcodes.ACC_VOLATILE) != 0) {
            return "volatile";
        }
        if ((accessFlags & Opcodes.ACC_TRANSIENT) != 0) {
            return "transien ";
        }
        if ((accessFlags & Opcodes.ACC_ABSTRACT) != 0) {
            return "abstract";
        }
        if ((accessFlags & Opcodes.ACC_STRICT) != 0) {
            return "strictfp";
        }
        if ((accessFlags & Opcodes.ACC_SYNTHETIC) != 0) {
            return "syntheti ";
        }
        if ((accessFlags & Opcodes.ACC_MANDATED) != 0) {
            return "mandated";
        }
        if ((accessFlags & Opcodes.ACC_ENUM) != 0) {
            return "enum ";
        }
        return "default";
    }

    public static String transformFrame(final int type,
                                        final int numLocal,
                                        final Object[] local,
                                        final int numStack,
                                        final Object[] stack) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FRAME ");
        switch (type) {
            case Opcodes.F_NEW:
            case Opcodes.F_FULL:
                stringBuilder.append("FULL [");
                stringBuilder.append(appendFrameTypes(numLocal, local));
                stringBuilder.append("] [");
                stringBuilder.append(appendFrameTypes(numStack, stack));
                stringBuilder.append(']');
                break;
            case Opcodes.F_APPEND:
                stringBuilder.append("APPEND [");
                stringBuilder.append(appendFrameTypes(numLocal, local));
                stringBuilder.append(']');
                break;
            case Opcodes.F_CHOP:
                stringBuilder.append("CHOP ").append(numLocal);
                break;
            case Opcodes.F_SAME:
                stringBuilder.append("SAME");
                break;
            case Opcodes.F_SAME1:
                stringBuilder.append("SAME1 ");
                stringBuilder.append(appendFrameTypes(1, stack));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return stringBuilder.toString();
    }

    private static String appendFrameTypes(final int numTypes, final Object[] frameTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numTypes; ++i) {
            if (i > 0) {
                stringBuilder.append(' ');
            }
            String appendDesc="";
            if (frameTypes[i] instanceof String) {
                String descriptor = (String) frameTypes[i];
                if (descriptor.charAt(0) == '[') {
                    appendDesc = appendDescriptor(FIELD_DESCRIPTOR, descriptor);
                } else {
                    appendDesc = appendDescriptor(INTERNAL_NAME, descriptor);
                }
            } else if (frameTypes[i] instanceof Integer) {
                switch (((Integer) frameTypes[i]).intValue()) {
                    case 0:
                        appendDesc =  appendDescriptor(FIELD_DESCRIPTOR, "T");
                        break;
                    case 1:
                        appendDesc = appendDescriptor(FIELD_DESCRIPTOR, "I");
                        break;
                    case 2:
                        appendDesc =  appendDescriptor(FIELD_DESCRIPTOR, "F");
                        break;
                    case 3:
                        appendDesc = appendDescriptor(FIELD_DESCRIPTOR, "D");
                        break;
                    case 4:
                        appendDesc = appendDescriptor(FIELD_DESCRIPTOR, "J");
                        break;
                    case 5:
                        appendDesc = appendDescriptor(FIELD_DESCRIPTOR, "N");
                        break;
                    case 6:
                        appendDesc = appendDescriptor(FIELD_DESCRIPTOR, "U");
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            } else {
                appendDesc = appendLabel((Label) frameTypes[i]);
            }
            stringBuilder.append(appendDesc);
        }
        labelNames = null;
        return stringBuilder.toString();
    }

    public static String appendDescriptor(final int type, final String value) {
        StringBuilder stringBuilder = new StringBuilder();
        if (type == CLASS_SIGNATURE || type == FIELD_SIGNATURE || type == METHOD_SIGNATURE) {
            if (value != null) {
                stringBuilder.append("// signature ").append(value).append('\n');
            }
        } else {
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }

    public   static  String  appendLabel(final Label label) {
        if (labelNames == null) {
            labelNames = new HashMap<Label, String>();
        }
        String name = labelNames.get(label);
        if (name == null) {
            name = "L" + labelNames.size();
            labelNames.put(label, name);
        }
        return name;
    }


}
