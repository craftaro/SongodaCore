package com.songoda.core.nms;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.v1_19_R3.NmsImplementationsImpl;

public class Nms {
    protected static NmsImplementations impl;

    /**
     * @return The implementations for the current server version
     */
    public static NmsImplementations getImplementations() throws UnsupportedServerVersionException {
        if (impl == null) {
            try {
                if (ServerVersion.isFolia()) {
                    impl = new NmsImplementationsImpl();
                    return impl;
                }
                switch (ServerVersion.getServerVersionString()) {
                    case "v1_19_R2":
                        impl = new com.songoda.core.nms.v1_19_R2.NmsImplementationsImpl();
                        break;
                }
                impl = (NmsImplementations) Class.forName("com.songoda.core.nms." + ServerVersion.getServerVersionString() + ".NmsImplementationsImpl").getConstructors()[0].newInstance();
            } catch (Exception ex) {
                throw new UnsupportedServerVersionException(ex);
            }
        }

        return impl;
    }
}
