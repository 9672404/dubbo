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
package com.alibaba.dubbo.common.extensionloader.ext6_inject;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * No default
 */
@SPI
public interface Ext6 {
    @Adaptive
    String echo(URL url, String s);

    /*
     * package com.alibaba.dubbo.common.extensionloader.ext6_inject;
     *     import com.alibaba.dubbo.common.extension.ExtensionLoader;
     *
     *     public class Ext6$Adaptive implements com.alibaba.dubbo.common.extensionloader.ext6_inject.Ext6 {
     *         public java.lang.String echo(com.alibaba.dubbo.common.URL arg0, java.lang.String arg1) {
     *             if (arg0 == null) throw new IllegalArgumentException("url == null");
     *             com.alibaba.dubbo.common.URL url = arg0;
     *             String extName = url.getParameter("ext6");
     *             if (extName == null)
     *                 throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.common.extensionloader.ext6_inject.Ext6) name from url(" + url.toString() + ") use keys([ext6])");
     *             com.alibaba.dubbo.common.extensionloader.ext6_inject.Ext6 extension = (com.alibaba.dubbo.common.extensionloader.ext6_inject.Ext6) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.extensionloader.ext6_inject.Ext6.class).getExtension(extName);
     *             return extension.echo(arg0, arg1);
     *         }
     *     }
     */
}