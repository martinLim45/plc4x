/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

package org.apache.plc4x.language.java;

import org.apache.commons.text.WordUtils;
import org.apache.plc4x.plugins.codegenerator.protocol.freemarker.FreemarkerLanguageTemplateHelper;
import org.apache.plc4x.plugins.codegenerator.types.definitions.ComplexTypeDefinition;
import org.apache.plc4x.plugins.codegenerator.types.definitions.DiscriminatedComplexTypeDefinition;
import org.apache.plc4x.plugins.codegenerator.types.definitions.TypeDefinition;
import org.apache.plc4x.plugins.codegenerator.types.fields.*;
import org.apache.plc4x.plugins.codegenerator.types.references.ComplexTypeReference;
import org.apache.plc4x.plugins.codegenerator.types.references.SimpleTypeReference;
import org.apache.plc4x.plugins.codegenerator.types.references.TypeReference;
import org.apache.plc4x.plugins.codegenerator.types.terms.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaLanguageTemplateHelper implements FreemarkerLanguageTemplateHelper {

    private final Map<String, ComplexTypeDefinition> types;

    public JavaLanguageTemplateHelper(Map<String, ComplexTypeDefinition> types) {
        this.types = types;
    }

    public String getLanguageTypeNameForField(TypedField field) {
        boolean optional = field instanceof OptionalField;
        return getLanguageTypeNameForField(!optional, field);
    }

    public String getNonPrimitiveLanguageTypeNameForField(TypedField field) {
        return getLanguageTypeNameForField(false, field);
    }

    private String getLanguageTypeNameForField(boolean allowPrimitive, TypedField field) {
        TypeReference typeReference = field.getType();
        if(typeReference instanceof SimpleTypeReference) {
            SimpleTypeReference simpleTypeReference = (SimpleTypeReference) typeReference;
            switch (simpleTypeReference.getBaseType()) {
                case BIT: {
                    return allowPrimitive ? "boolean" : "Boolean";
                }
                case UINT: {
                    if (simpleTypeReference.getSize() <= 4) {
                        return allowPrimitive ? "byte" : "Byte";
                    }
                    if (simpleTypeReference.getSize() <= 8) {
                        return allowPrimitive ? "short" : "Short";
                    }
                    if (simpleTypeReference.getSize() <= 16) {
                        return allowPrimitive ? "int" : "Integer";
                    }
                    if (simpleTypeReference.getSize() <= 32) {
                        return allowPrimitive ? "long" : "Long";
                    }
                    return "BigInteger";
                }
                case INT: {
                    if (simpleTypeReference.getSize() <= 8) {
                        return allowPrimitive ? "byte" : "Byte";
                    }
                    if (simpleTypeReference.getSize() <= 16) {
                        return allowPrimitive ? "short" : "Short";
                    }
                    if (simpleTypeReference.getSize() <= 32) {
                        return allowPrimitive ? "int" : "Integer";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return allowPrimitive ? "long" : "Long";
                    }
                    return "BigInteger";
                }
                case FLOAT: {
                    if (simpleTypeReference.getSize() <= 32) {
                        return allowPrimitive ? "float" : "Float";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return allowPrimitive ? "double" : "Double";
                    }
                    return "BigDecimal";
                }
                case STRING: {
                    return "String";
                }
            }
            return "Hurz";
        } else {
            return ((ComplexTypeReference) typeReference).getName();
        }
    }

    public String getLanguageTypeNameForSpecType(TypeReference typeReference) {
        if(typeReference instanceof SimpleTypeReference) {
            SimpleTypeReference simpleTypeReference = (SimpleTypeReference) typeReference;
            switch (simpleTypeReference.getBaseType()) {
                case BIT: {
                    return "boolean";
                }
                case UINT: {
                    if (simpleTypeReference.getSize() <= 4) {
                        return "byte";
                    }
                    if (simpleTypeReference.getSize() <= 8) {
                        return "short";
                    }
                    if (simpleTypeReference.getSize() <= 16) {
                        return "int";
                    }
                    if (simpleTypeReference.getSize() <= 32) {
                        return "long";
                    }
                    return "BigInteger";
                }
                case INT: {
                    if (simpleTypeReference.getSize() <= 8) {
                        return "byte";
                    }
                    if (simpleTypeReference.getSize() <= 16) {
                        return "short";
                    }
                    if (simpleTypeReference.getSize() <= 32) {
                        return "int";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return "long";
                    }
                    return "BigInteger";
                }
                case FLOAT: {
                    if (simpleTypeReference.getSize() <= 32) {
                        return "float";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return "double";
                    }
                    return "BigDecimal";
                }
                case STRING: {
                    return "String";
                }
            }
            return "Hurz";
        } else {
            return ((ComplexTypeReference) typeReference).getName();
        }
    }

    public String getNullValueForType(TypeReference typeReference) {
        if(typeReference instanceof SimpleTypeReference) {
            SimpleTypeReference simpleTypeReference = (SimpleTypeReference) typeReference;
            switch (simpleTypeReference.getBaseType()) {
                case BIT: {
                    return "false";
                }
                case UINT: {
                    if (simpleTypeReference.getSize() <= 16) {
                        return "0";
                    }
                    if (simpleTypeReference.getSize() <= 32) {
                        return "0l";
                    }
                    return "null";
                }
                case INT: {
                    if (simpleTypeReference.getSize() <= 32) {
                        return "0";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return "0l";
                    }
                    return "null";
                }
                case FLOAT: {
                    if (simpleTypeReference.getSize() <= 32) {
                        return "0.0f";
                    }
                    if (simpleTypeReference.getSize() <= 64) {
                        return "0.0";
                    }
                    return "null";
                }
                case STRING: {
                    return "null";
                }
            }
            return "Hurz";
        } else {
            return "null";
        }
    }

    public String getArgumentType(TypeReference typeReference, int index) {
        if(typeReference instanceof ComplexTypeReference) {
            ComplexTypeReference complexTypeReference = (ComplexTypeReference) typeReference;
            if(!types.containsKey(complexTypeReference.getName())) {
                throw new RuntimeException("Could not find definition of complex type " + complexTypeReference.getName());
            }
            ComplexTypeDefinition complexTypeDefinition = types.get(complexTypeReference.getName());
            if(complexTypeDefinition.getParserArguments().length <= index) {
                throw new RuntimeException("Type " + complexTypeReference.getName() + " specifies too few parser arguments");
            }
            return getLanguageTypeNameForSpecType(complexTypeDefinition.getParserArguments()[index].getType());
        }
        return "Hurz";
    }

    public String getReadBufferReadMethodCall(SimpleTypeReference simpleTypeReference) {
        switch (simpleTypeReference.getBaseType()) {
            case BIT: {
                return "readBit()";
            }
            case UINT: {
                if (simpleTypeReference.getSize() <= 4) {
                    return "readUnsignedByte(" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 8) {
                    return "readUnsignedShort(" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 16) {
                    return "readUnsignedInt(" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 32) {
                    return "readUnsignedLong(" + simpleTypeReference.getSize() + ")";
                }
                return "readUnsignedBigInteger" + simpleTypeReference.getSize() + ")";
            }
            case INT: {
                if (simpleTypeReference.getSize() <= 8) {
                    return "readByte" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 16) {
                    return "readShort" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 32) {
                    return "readInt" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 64) {
                    return "readLong" + simpleTypeReference.getSize() + ")";
                }
                return "readBigInteger" + simpleTypeReference.getSize() + ")";
            }
            case FLOAT: {
                if (simpleTypeReference.getSize() <= 32) {
                    return "readFloat" + simpleTypeReference.getSize() + ")";
                }
                if (simpleTypeReference.getSize() <= 64) {
                    return "readDouble" + simpleTypeReference.getSize() + ")";
                }
                return "readBigDecimal" + simpleTypeReference.getSize() + ")";
            }
            case STRING: {
                return "readString" + simpleTypeReference.getSize() + ")";
            }
        }
        return "Hurz";
    }

    public String getWriteBufferReadMethodCall(SimpleTypeReference simpleTypeReference, String fieldName) {
        switch (simpleTypeReference.getBaseType()) {
            case BIT: {
                return "writeBit((boolean) " + fieldName + ")";
            }
            case UINT: {
                if (simpleTypeReference.getSize() <= 4) {
                    return "writeUnsignedByte(" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").byteValue())";
                }
                if (simpleTypeReference.getSize() <= 8) {
                    return "writeUnsignedShort(" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").shortValue())";
                }
                if (simpleTypeReference.getSize() <= 16) {
                    return "writeUnsignedInt(" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").intValue())";
                }
                if (simpleTypeReference.getSize() <= 32) {
                    return "writeUnsignedLong(" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").longValue()";
                }
                return "writeUnsignedBigInteger" + simpleTypeReference.getSize() + ", (BigInteger) " + fieldName + ")";
            }
            case INT: {
                if (simpleTypeReference.getSize() <= 8) {
                    return "writeByte" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").byteValue())";
                }
                if (simpleTypeReference.getSize() <= 16) {
                    return "writeShort" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").shortValue())";
                }
                if (simpleTypeReference.getSize() <= 32) {
                    return "writeInt" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").intValue())";
                }
                if (simpleTypeReference.getSize() <= 64) {
                    return "writeLong" + simpleTypeReference.getSize() + ", ((Number) " + fieldName + ").longValue())";
                }
                return "writeBigInteger" + simpleTypeReference.getSize() + ", (BigInteger) " + fieldName + ")";
            }
            case FLOAT: {
                if (simpleTypeReference.getSize() <= 32) {
                    return "writeFloat" + simpleTypeReference.getSize() + ", (float) " + fieldName + ")";
                }
                if (simpleTypeReference.getSize() <= 64) {
                    return "writeDouble" + simpleTypeReference.getSize() + ", (double) " + fieldName + ")";
                }
                return "writeBigDecimal" + simpleTypeReference.getSize() + ", (BigDecimal) " + fieldName + ")";
            }
            case STRING: {
                return "writeString" + simpleTypeReference.getSize() + ", (String) " + fieldName + ")";
            }
        }
        return "Hurz";
    }

    public String getReadMethodName(SimpleTypeReference simpleTypeReference) {
        String languageTypeName = getLanguageTypeNameForSpecType(simpleTypeReference);
        languageTypeName = languageTypeName.substring(0, 1).toUpperCase() + languageTypeName.substring(1);
        if(simpleTypeReference.getBaseType().equals(SimpleTypeReference.SimpleBaseType.UINT)) {
            return "readUnsigned" + languageTypeName;
        } else {
            return "read" + languageTypeName;

        }
    }

    public Collection<ComplexTypeReference> getComplexTypes(ComplexTypeDefinition complexTypeDefinition) {
        Map<String, ComplexTypeReference> types = new HashMap<>();
        for (Field field : complexTypeDefinition.getFields()) {
            if(field instanceof TypedField) {
                TypedField typedField = (TypedField) field;
                if(typedField.getType() instanceof ComplexTypeReference) {
                    ComplexTypeReference complexTypeReference = (ComplexTypeReference) typedField.getType();
                    types.put(complexTypeReference.getName(),  complexTypeReference);
                }
            } else if(field instanceof SwitchField) {
                SwitchField switchField = (SwitchField) field;
                for (DiscriminatedComplexTypeDefinition cas : switchField.getCases()) {
                    types.put(cas.getName(), new ComplexTypeReference() {
                        @Override
                        public String getName() {
                            return cas.getName();
                        }
                    });
                }
            }
        }
        return types.values();
    }

    public boolean isSimpleType(TypeReference typeReference) {
        return typeReference instanceof SimpleTypeReference;
    }

    public boolean isDiscriminatedType(TypeDefinition typeDefinition) {
        return typeDefinition instanceof DiscriminatedComplexTypeDefinition;
    }

    public boolean isCountArray(ArrayField arrayField) {
        return arrayField.getLengthType() == ArrayField.LengthType.COUNT;
    }

    public String toSwitchExpression(String expression) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("([^\\.]*)\\.([a-zA-Z\\d]+)(.*)");
        Matcher matcher;
        while ((matcher = pattern.matcher(expression)).matches()) {
            String prefix = matcher.group(1);
            String middle = matcher.group(2);
            sb.append(prefix).append(".get").append(WordUtils.capitalize(middle)).append("()");
            expression = matcher.group(3);
        }
        sb.append(expression);
        return sb.toString();
    }

    public String toDeserializationExpression(Term term) {
        return toExpression(term, this::toVariableDeserializationExpression);
    }

    public String toSerializationExpression(Term term) {
        return toExpression(term, this::toVariableSerializationExpression);
    }

    private String toExpression(Term term, Function<Term, String> variableExpressionGenerator) {
        if(term == null) {
            return "";
        }
        if(term instanceof Literal) {
            if(term instanceof NullLiteral) {
                return "null";
            } else if(term instanceof BooleanLiteral) {
                return Boolean.toString(((BooleanLiteral) term).getValue());
            } else if(term instanceof NumericLiteral) {
                return ((NumericLiteral) term).getNumber().toString();
            } else if(term instanceof StringLiteral) {
                return "\"" + ((StringLiteral) term).getValue() + "\"";
            } else if(term instanceof VariableLiteral) {
                return variableExpressionGenerator.apply(term);
            } else {
                throw new RuntimeException("Unsupported Literal type " + term.getClass().getName());
            }
        } else if (term instanceof UnaryTerm) {
            UnaryTerm ut = (UnaryTerm) term;
            Term a = ut.getA();
            switch(ut.getOperation()) {
                case "!":
                    return "!(" + toExpression(a, variableExpressionGenerator) + ")";
                case "-":
                    return "-(" + toExpression(a, variableExpressionGenerator) + ")";
                case "()":
                    return "(" + toExpression(a, variableExpressionGenerator) + ")";
                default:
                    throw new RuntimeException("Unsupported unary operation type " + ut.getOperation());
            }
        } else if (term instanceof BinaryTerm) {
            BinaryTerm bt = (BinaryTerm) term;
            Term a = bt.getA();
            Term b = bt.getB();
            String operation = bt.getOperation();
            return "(" + toExpression(a, variableExpressionGenerator) + ") " + operation + " (" + toExpression(b, variableExpressionGenerator) + ")";
        } else if (term instanceof TernaryTerm) {
            TernaryTerm tt = (TernaryTerm) term;
            if("if".equals(tt.getOperation())) {
                Term a = tt.getA();
                Term b = tt.getB();
                Term c = tt.getC();
                return "((" +  toExpression(a, variableExpressionGenerator) + ") ? " + toExpression(b, variableExpressionGenerator) + " : " + toExpression(c, variableExpressionGenerator) + ")";
            } else {
                throw new RuntimeException("Unsupported ternary operation type " + tt.getOperation());
            }
        } else {
            throw new RuntimeException("Unsupported Term type " + term.getClass().getName());
        }
    }

    private String toVariableDeserializationExpression(Term term) {
        VariableLiteral vl = (VariableLiteral) term;
        // CAST expressions are special as we need to add a ".class" to the second parameter in Java.
        if("CAST".equals(vl.getName())) {
            StringBuilder sb = new StringBuilder(vl.getName());
            if((vl.getArgs() == null) || (vl.getArgs().size() != 2)) {
                throw new RuntimeException("A CAST expression expects exactly two arguments.");
            }
            sb.append("(").append(toVariableDeserializationExpression(vl.getArgs().get(0)))
                .append(", ").append(((VariableLiteral) vl.getArgs().get(1)).getName()).append(".class)");
            return sb.toString() + ((vl.getChild() != null) ? "." + toVariableExpressionRest(vl.getChild()) : "");
        }
        // All uppercase names are not fields, but utility methods.
        else if(vl.getName().equals(vl.getName().toUpperCase())) {
            StringBuilder sb = new StringBuilder(vl.getName());
            if(vl.getArgs() != null) {
                sb.append("(");
                boolean firstArg = true;
                for(Term arg : vl.getArgs()) {
                    if(!firstArg) {
                        sb.append(", ");
                    }
                    sb.append(toVariableDeserializationExpression(arg));
                    firstArg = false;
                }
                sb.append(")");
            }
            return sb.toString() + ((vl.getChild() != null) ? "." + toVariableExpressionRest(vl.getChild()) : "");
        }
        return vl.getName() + ((vl.getChild() != null) ? "." + toVariableExpressionRest(vl.getChild()) : "");
    }

    private String toVariableSerializationExpression(Term term) {
        VariableLiteral vl = (VariableLiteral) term;
        if("STATIC_CALL".equals(vl.getName())) {
            StringBuilder sb = new StringBuilder();
            if(!(vl.getArgs().get(0) instanceof StringLiteral)) {
                throw new RuntimeException("Expecting the first argument of a 'STATIC_CALL' to be a StringLiteral");
            }
            String methodName = ((StringLiteral) vl.getArgs().get(0)).getValue();
            methodName = methodName.substring(1, methodName.length() - 1);
            sb.append(methodName).append("(");
            for(int i = 1; i < vl.getArgs().size(); i++) {
                Term arg = vl.getArgs().get(i);
                if(i > 1) {
                    sb.append(", ");
                }
                if(arg instanceof VariableLiteral) {
                    sb.append(toVariableSerializationExpression(arg));
                } else if(arg instanceof StringLiteral) {
                    sb.append(((StringLiteral) arg).getValue());
                }
            }
            sb.append(")");
            return sb.toString();
        }
        // All uppercase names are not fields, but utility methods.
        else if(vl.getName().equals(vl.getName().toUpperCase())) {
            StringBuilder sb = new StringBuilder(vl.getName());
            if(vl.getArgs() != null) {
                sb.append("(");
                boolean firstArg = true;
                for(Term arg : vl.getArgs()) {
                    if(!firstArg) {
                        sb.append(", ");
                    }
                    if(arg instanceof VariableLiteral) {
                        sb.append(toVariableSerializationExpression(arg));
                    } else if(arg instanceof StringLiteral) {
                        sb.append(((StringLiteral) arg).getValue());
                    }
                    firstArg = false;
                }
                sb.append(")");
            }
            return sb.toString();
        }
        return "value." + toVariableExpressionRest(vl);
    }

    private String toVariableExpressionRest(VariableLiteral vl) {
        return "get" + WordUtils.capitalize(vl.getName()) + "()" + ((vl.isIndexed() ? "[" + vl.getIndex() + "]" : "") +
            ((vl.getChild() != null) ? "." + toVariableExpressionRest(vl.getChild()) : ""));
    }

}
