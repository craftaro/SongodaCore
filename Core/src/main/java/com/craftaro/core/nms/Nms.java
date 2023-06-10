package com.craftaro.core.nms;

import com.craftaro.core.nms.v1_8_R3.NmsImplementationsImpl;
import com.craftaro.core.compatibility.ServerVersion;

public class Nms {
    protected static NmsImplementations impl;

    /**
     * @return The implementations for the current server version
     */
    public static NmsImplementations getImplementations() throws UnsupportedServerVersionException {
        if (impl == null) {
            switch (ServerVersion.getServerVersionString()) {
                case "v1_8_R1":
                    impl = new com.craftaro.core.nms.v1_8_R1.NmsImplementationsImpl();
                    break;
                case "v1_8_R2":
                    impl = new com.craftaro.core.nms.v1_8_R2.NmsImplementationsImpl();
                    break;
                case "v1_8_R3":
                    impl = new NmsImplementationsImpl();
                    break;
                case "v1_9_R1":
                    impl = new com.craftaro.core.nms.v1_9_R1.NmsImplementationsImpl();
                    break;
                case "v1_9_R2":
                    impl = new com.craftaro.core.nms.v1_9_R2.NmsImplementationsImpl();
                    break;
                case "v1_10_R1":
                    impl = new com.craftaro.core.nms.v1_10_R1.NmsImplementationsImpl();
                    break;
                case "v1_11_R1":
                    impl = new com.craftaro.core.nms.v1_11_R1.NmsImplementationsImpl();
                    break;
                case "v1_12_R1":
                    impl = new com.craftaro.core.nms.v1_12_R1.NmsImplementationsImpl();
                    break;
                case "v1_13_R1":
                    impl = new com.craftaro.core.nms.v1_13_R1.NmsImplementationsImpl();
                    break;
                case "v1_13_R2":
                    impl = new com.craftaro.core.nms.v1_13_R2.NmsImplementationsImpl();
                    break;
                case "v1_14_R1":
                    impl = new com.craftaro.core.nms.v1_14_R1.NmsImplementationsImpl();
                    break;
                case "v1_15_R1":
                    impl = new com.craftaro.core.nms.v1_15_R1.NmsImplementationsImpl();
                    break;
                case "v1_16_R1":
                    impl = new com.craftaro.core.nms.v1_16_R1.NmsImplementationsImpl();
                    break;
                case "v1_16_R2":
                    impl = new com.craftaro.core.nms.v1_16_R2.NmsImplementationsImpl();
                    break;
                case "v1_16_R3":
                    impl = new com.craftaro.core.nms.v1_16_R3.NmsImplementationsImpl();
                    break;
                case "v1_17_R1":
                    impl = new com.craftaro.core.nms.v1_17_R1.NmsImplementationsImpl();
                    break;
                case "v1_18_R1":
                    impl = new com.craftaro.core.nms.v1_18_R1.NmsImplementationsImpl();
                    break;
                case "v1_18_R2":
                    impl = new com.craftaro.core.nms.v1_18_R2.NmsImplementationsImpl();
                    break;
                case "v1_19_0":
                    impl = new com.craftaro.core.nms.v1_19_0.NmsImplementationsImpl();
                    break;
                case "v1_19_R1":
                    impl = new com.craftaro.core.nms.v1_19_R1.NmsImplementationsImpl();
                    break;
                case "v1_19_R2":
                    impl = new com.craftaro.core.nms.v1_19_R2.NmsImplementationsImpl();
                    break;
                case "v1_19_R3":
                    impl = new com.craftaro.core.nms.v1_19_R3.NmsImplementationsImpl();
                    break;
                default:
                    throw new UnsupportedServerVersionException();
            }
        }

        return impl;
    }
}
