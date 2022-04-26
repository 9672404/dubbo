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
package com.alibaba.dubbo.common.extensionloader.ext1;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * Simple extension, has no wrapper
 */
@SPI("impl1")
public interface SimpleExt {
    // @Adaptive example, do not specify a explicit key.
    @Adaptive
    String echo(URL url, String s);

    @Adaptive({"key1", "key2"})
    String yell(URL url, String s);

    // no @Adaptive
    String bang(URL url, int i);
}

/**
package com.alibaba.dubbo.common.extensionloader.ext1;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

public class SimpleExt$Adaptive implements com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt {
    public java.lang.String bang(com.alibaba.dubbo.common.URL arg0, int arg1) {throw new UnsupportedOperationException("method public abstract java.lang.String com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt.bang(com.alibaba.dubbo.common.URL,int) of interface com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt is not adaptive method!");
    }
    public java.lang.String yell(com.alibaba.dubbo.common.URL arg0, java.lang.String arg1) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("key1", url.getParameter("key2", "impl1"));
        if(extName == null) throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt) name from url(" + url.toString() + ") use keys([key1, key2])");
        com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt extension = (com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt)ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt.class).getExtension(extName);
        return extension.yell(arg0, arg1);
    }

    public java.lang.String echo(com.alibaba.dubbo.common.URL arg0, java.lang.String arg1) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("simple.ext", "impl1");
        if(extName == null) throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt) name from url(" + url.toString() + ") use keys([simple.ext])");
        com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt extension = (com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt)ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt.class).getExtension(extName);
        return extension.echo(arg0, arg1);
    }
}*/
