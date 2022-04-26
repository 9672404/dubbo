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
package com.alibaba.dubbo.common.extensionloader.ext2;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * Has no default
 */
@SPI
public interface Ext2 {
    // one of the properties of an argument is an instance of URL.
    @Adaptive
    String echo(UrlHolder holder, String s);

    String bang(URL url, int i);

/**
 * package com.alibaba.dubbo.common.extensionloader.ext2;
 * import com.alibaba.dubbo.common.extension.ExtensionLoader;
 *
 *     public class Ext2$Adaptive implements com.alibaba.dubbo.common.extensionloader.ext2.Ext2 {
 *         public java.lang.String echo(com.alibaba.dubbo.common.extensionloader.ext2.UrlHolder arg0, java.lang.String arg1) {
 *             if (arg0 == null)
 *                 throw new IllegalArgumentException("com.alibaba.dubbo.common.extensionloader.ext2.UrlHolder argument == null");
 *             if (arg0.getUrl() == null)
 *                 throw new IllegalArgumentException("com.alibaba.dubbo.common.extensionloader.ext2.UrlHolder argument getUrl() == null");
 *             com.alibaba.dubbo.common.URL url = arg0.getUrl();
 *             String extName = url.getParameter("ext2");
 *             if (extName == null)
 *                 throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.common.extensionloader.ext2.Ext2) name from url(" + url.toString() + ") use keys([ext2])");
 *             com.alibaba.dubbo.common.extensionloader.ext2.Ext2 extension = (com.alibaba.dubbo.common.extensionloader.ext2.Ext2) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.extensionloader.ext2.Ext2.class).getExtension(extName);
 *             return extension.echo(arg0, arg1);
 *         }
 *
 *         public java.lang.String bang(com.alibaba.dubbo.common.URL arg0, int arg1) {
 *             throw new UnsupportedOperationException("method public abstract java.lang.String com.alibaba.dubbo.common.extensionloader.ext2.Ext2.bang(com.alibaba.dubbo.common.URL,int) of interface com.alibaba.dubbo.common.extensionloader.ext2.Ext2 is not adaptive method!");
 *         }
 *     }
 */
}