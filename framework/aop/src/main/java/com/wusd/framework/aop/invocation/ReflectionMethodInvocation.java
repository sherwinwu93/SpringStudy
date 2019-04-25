package com.wusd.framework.aop.invocation;

import com.wusd.framework.aop.interceptor.AopMethodInterceptor;
import com.wusd.framework.aop.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectionMethodInvocation implements ProxyMethodInvocation {
    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected Object[] arguments = new Object[0];
    //用于保存所有的拦截器,需要递归的去增加拦截器.当处理完了所有拦截器之后,才会真正调用调用被增强的方法.
    protected final List<AopMethodInterceptor> interceptorList;
    private int currentInterceptorIndex = -1;

    public ReflectionMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                                      List<AopMethodInterceptor> interceptorList) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.interceptorList = interceptorList;
    }

    @Override
    public Object getProxy() {
        return proxy;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        if (currentInterceptorIndex == this.interceptorList.size() - 1) {
            return invokeOriginal();
        }

        AopMethodInterceptor interceptor = interceptorList.get(++currentInterceptorIndex);
        return interceptor.invoke(this);
    }

    protected Object invokeOriginal() throws Throwable {
        return ReflectionUtils.invokeMethodUseReflection(target, method, arguments);
    }
}
