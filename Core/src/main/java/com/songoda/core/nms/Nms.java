package com.songoda.core.nms;

import com.songoda.core.compatibility.ServerVersion;

public class Nms {
    protected static NmsImplementations impl;

    /**
     * @return The implementations for the current server version
     */
    public static NmsImplementations getImplementations() throws UnsupportedServerVersionException {
        if (impl == null) {
            try {
                impl = (NmsImplementations) Class.forName("com.songoda.core.nms." + ServerVersion.getServerVersionString() + ".NmsImplementationsImpl").getConstructors()[0].newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new UnsupportedServerVersionException(ex);
            }
        }

        return impl;
    }
}
