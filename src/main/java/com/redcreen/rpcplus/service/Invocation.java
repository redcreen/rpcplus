package com.redcreen.rpcplus.service;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Invocation implements Serializable{

    private static final long serialVersionUID = -4355285085441097045L;

    private String              methodName;

    private Class<?>[]          parameterTypes;

    private Object[]            arguments;

    private Map<String, String> attachments;

    public Invocation() {
    }

    public Invocation(Method method, Object[] arguments) {
        this(method.getName(), method.getParameterTypes(), arguments, null);
    }

    public Invocation(Method method, Object[] arguments, Map<String, String> attachment) {
        this(method.getName(), method.getParameterTypes(), arguments, attachment);
    }

    public Invocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        this(methodName, parameterTypes, arguments, null);
    }

    public Invocation(String methodName, Class<?>[] parameterTypes, Object[] arguments, Map<String, String> attachments) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
        this.arguments = arguments == null ? new Object[0] : arguments;
        this.attachments = attachments == null ? new HashMap<String, String>() : attachments;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Map<String, String> getAttachments() {
        return attachments;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes == null ? new Class<?>[0] : parameterTypes;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments == null ? new Object[0] : arguments;
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments = attachments == null ? new HashMap<String, String>() : attachments;
    }
    
    public void setAttachment(String key, String value) {
        if (attachments == null) {
            attachments = new HashMap<String, String>();
        }
        attachments.put(key, value);
    }

    public String getAttachment(String key) {
        if (attachments == null) {
            return null;
        }
        return attachments.get(key);
    }
    
    public String getAttachment(String key, String defaultValue) {
        if (attachments == null) {
            return defaultValue;
        }
        String value = attachments.get(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public String toString() {
        return "RpcInvocation [methodName=" + methodName + ", parameterTypes="
                + Arrays.toString(parameterTypes) + ", arguments=" + Arrays.toString(arguments)
                + ", attachments=" + attachments + "]";
    }

}
