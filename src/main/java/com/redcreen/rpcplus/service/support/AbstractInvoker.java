package com.redcreen.rpcplus.service.support;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redcreen.rpcplus.service.Invocation;
import com.redcreen.rpcplus.service.InvokerContext;
import com.redcreen.rpcplus.service.InvokeException;
import com.redcreen.rpcplus.service.InvokeResult;
import com.redcreen.rpcplus.service.Invoker;
import com.redcreen.rpcplus.support.URL;

public abstract class AbstractInvoker<T> implements Invoker<T> {
    protected final Logger   logger    = LoggerFactory.getLogger(getClass());

    private final Class<T>   type;

    private final URL        url;

    private final Map<String, String> attachment;

    private volatile boolean available = true;

    private volatile boolean destroyed = false;
    
    public AbstractInvoker(Class<T> type, URL url){
        this(type, url, (Map<String, String>) null);
    }
    
    public AbstractInvoker(Class<T> type, URL url, String[] keys) {
        this(type, url, convertAttachment(url, keys));
    }

    public AbstractInvoker(Class<T> type, URL url, Map<String, String> attachment) {
        if (type == null)
            throw new IllegalArgumentException("service type == null");
        if (url == null)
            throw new IllegalArgumentException("service url == null");
        this.type = type;
        this.url = url;
        this.attachment = attachment == null ? null : Collections.unmodifiableMap(attachment);
    }
    
    private static Map<String, String> convertAttachment(URL url, String[] keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        Map<String, String> attachment = new HashMap<String, String>();
        for (String key : keys) {
            String value = url.getParameter(key);
            if (value != null && value.length() > 0) {
                attachment.put(key, value);
            }
        }
        return attachment;
    }

    public Class<T> getInterface() {
        return type;
    }

    public URL getUrl() {
        return url;
    }

    public boolean isAvailable() {
        return available;
    }
    
    protected void setAvailable(boolean available) {
        this.available = available;
    }

    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        setAvailable(false);
    }

    public String toString() {
        return getInterface() + " -> " + getUrl()==null?" ":getUrl().toString();
    }

    public InvokeResult invoke(Invocation inv) throws InvokeException {
        if(destroyed) {
            throw new InvokeException("invoker has been destroyed .url + "+ url);
        }
        Invocation invocation = (Invocation) inv;
        Map<String, String> attachments = new HashMap<String, String>();
        if (attachment != null && attachment.size() > 0) {
            attachments.putAll(attachment);
        }
        Map<String, String> context = InvokerContext.getContext().getAttachments();
        if (context != null) {
            attachments.putAll(context);
        }
        if (invocation.getAttachments() != null) {
            attachments.putAll(invocation.getAttachments());
        }
        invocation.setAttachments(attachments);
        try {
            return doInvoke(invocation);
        } catch (InvocationTargetException e) { // biz exception
            Throwable te = e.getTargetException();
            if (te == null) {
                return new InvokeResult(e);
            } else {
                if (te instanceof InvokeException) {
                    ((InvokeException) te).setCode(InvokeException.BIZ_EXCEPTION);
                }
                return new InvokeResult(te);
            }
        } catch (InvokeException e) {
            if (e.isBiz()) {
                return new InvokeResult(e);
            } else {
                throw e;
            }
        } catch (Throwable e) {
            return new InvokeResult(e);
        }
    }

    protected abstract InvokeResult doInvoke(Invocation invocation) throws Throwable;
}
