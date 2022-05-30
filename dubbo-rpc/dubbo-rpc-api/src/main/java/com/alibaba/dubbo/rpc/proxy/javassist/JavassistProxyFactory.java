/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.proxy.javassist;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.bytecode.Proxy;
import com.alibaba.dubbo.common.bytecode.Wrapper;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyFactory;
import com.alibaba.dubbo.rpc.proxy.AbstractProxyInvoker;
import com.alibaba.dubbo.rpc.proxy.InvokerInvocationHandler;

/**
 * JavaassistRpcProxyFactory
 */
public class JavassistProxyFactory extends AbstractProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        // 生成 Proxy 子类（Proxy 是抽象类）。并调用 Proxy 子类的 newInstance 方法创建 Proxy 实例
        // InvokerInvocationHandler 实现 JDK 的 InvocationHandler 接口，具体的用途是拦截接口类调用
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
         /*
            接口代理类生成代码形如：
            public class proxy0 implements org.apache.dubbo.demo.DemoService {

                public static java.lang.reflect.Method[] methods;

                private java.lang.reflect.InvocationHandler handler;

                public proxy0() {
                }

                public proxy0(java.lang.reflect.InvocationHandler arg0) {
                    handler = $1;
                }

                public java.lang.String sayHello(java.lang.String arg0) {
                    Object[] args = new Object[1];
                    args[0] = ($w) $1;
                    Object ret = handler.invoke(this, methods[0], args);
                    return (java.lang.String) ret;
                }
            }
            Proxy 子类生成代码形如：
            public class Proxy1 extends com.alibaba.dubbo.common.bytecode.Proxy {

                public Proxy1() {
                }

                 public Object newInstance(java.lang.reflect.InvocationHandler h) {
                    return new org.apache.dubbo.proxy0($1);
                }
            }

            最终，invoker放入InvokerInvocationHandler -> 通过Proxy1#newInstance放入接口代理类proxy0 -> 调用proxy0方法时调用InvokerInvocationHandler#invoker -> 实际调用了invoker#invoke
            实现的效果为，调用RPC接口时，看似调用了本地接口DemoService，实则是DemoService的代理类proxy0，proxy0内部则调用了invoker#invoke，实现了对调用方隐藏invoker逻辑
       */
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper cannot handle this scenario correctly: the classname contains '$'
        // 生成包装类，然后再返回Invoker，这样实现类就统一包装为Invoker
        /*
            Invoker 是实体域，它是 Dubbo 的核心模型，其它模型都向它靠扰，或转换成它，它代表一个可执行体，可向它发起 invoke 调用，
            它有可能是一个本地的实现，也可能是一个远程的实现，也可能一个集群实现。
         */
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

}
