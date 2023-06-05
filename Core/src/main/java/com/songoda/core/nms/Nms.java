package com.songoda.core.nms;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.v1_19_R2.NmsImplementationsImpl;

public class Nms {
    protected static NmsImplementations impl;

    /**
     * @return The implementations for the current server version
     */
    public static NmsImplementations getImplementations() throws UnsupportedServerVersionException {
        if (impl == null) {
            try {
                //impl = (NmsImplementations) Class.forName("com.songoda.core.nms." + ServerVersion.getServerVersionString() + ".NmsImplementationsImpl").getConstructors()[0].newInstance();
                impl = new NmsImplementationsImpl();
            } catch (Exception ex) {
                throw new UnsupportedServerVersionException(ex);
            }
        }

        return impl;
    }
}
