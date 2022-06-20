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
        // InvokerInvocationHandler 实现 JDK 的 InvocationHandler 接口，具体的用途是代理接口类调用
        return (T) Proxy.getProxy(interfaces).newInstance(new InvokerInvocationHandler(invoker));
         /*
            接口代理类生成代码形如：
            public class proxyDemo implements org.apache.dubbo.demo.DemoService {

                public static java.lang.reflect.Method[] methods;

                private java.lang.reflect.InvocationHandler handler;

                public proxyDemo() {
                }

                public proxyDemo(java.lang.reflect.InvocationHandler arg0) {
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
                    return new org.apache.dubbo.proxyDemo($1);
                }
            }

            最终，invoker放入InvokerInvocationHandler -> 通过Proxy1#newInstance放入接口代理类proxyDemo -> 调用proxyDemo方法时调用InvokerInvocationHandler#invoker -> 实际调用了invoker#invoke
            实现的效果为，调用RPC接口时，看似调用了本地接口DemoService，实则是DemoService的代理类proxyDemo，proxyDemo内部则调用了invoker#invoke，实现了对调用方隐藏invoker逻辑
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
                // 根据方法名和参数描述符，匹配方法
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
        /*
        构造包装类，将原本的类转化为三个方法
        伪代码如下：
        public void setPropertyValue(Object o, String n, Object v){
            if ("aa" == n) {
                o.setAa(v)
            }
            if ("bb" == n) {
                o.setBb(v)
            }
        }
        public Object getPropertyValue(Object o, String n){
            if ("aa" == n) {
               return o.getAa(v)
            }
            if ("bb" == n) {
                return o.getBb(v)
            }
        }
        public Object invokeMethod(Object o, String n, Class[] p, Object[] v) {
            if ("setAa" == n) {
                 o.setAa(v)
            }
            if ("getBb" == n) {
                 return o.getBb(v)
            }
        }
     */
    }

}
