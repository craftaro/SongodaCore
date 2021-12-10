package com.songoda.core.compatibility;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public enum MethodMapping {

    MC_ITEM_STACK__GET_TAG("getTag", "s"),
    MC_ITEM_STACK__SET_TAG("setTag", "c", ClassMapping.NBT_TAG_COMPOUND.getClazz()),

    MC_NBT_TAG_COMPOUND__SET("set", "a", String.class, ClassMapping.NBT_BASE.getClazz()),
    MC_NBT_TAG_COMPOUND__SET_SHORT("setShort", "a", String.class),
    MC_NBT_TAG_COMPOUND__SET_STRING("setString", "a", String.class, String.class),
    MC_NBT_TAG_COMPOUND__REMOVE("remove", "r", String.class, short.class),

    CB_ITEM_STACK__AS_NMS_COPY("asNMSCopy", ItemStack.class),
    CB_ITEM_STACK__AS_CRAFT_MIRROR("asCraftMirror", ClassMapping.ITEM_STACK.getClazz()),

    MC_NBT_TAG_LIST__ADD("a", "add", "add", ClassMapping.NBT_BASE.getClazz()),

    WORLD_BOARDER__SET_CENTER("setCenter", "setCenter", "c", double.class, double.class),
    WORLD_BOARDER__SET_SIZE("setSize", "setSize", "a", double.class),

    WORLD_BOARDER__SET_WARNING_TIME("setWarningTime", "setWarningTime", "b", int.class),
    WORLD_BOARDER__SET_WARNING_DISTANCE("setWarningDistance", "setWarningDistance", "c", int.class),
    WORLD_BOARDER__TRANSITION_SIZE_BETWEEN("transitionSizeBetween", "transitionSizeBetween", "a", double.class, double.class, long.class);

    private final String _1_14;
    private final String _1_17;
    private final String _1_18;
    private final Class<?>[] paramaters;

    MethodMapping(String _1_14, String _1_17, String _1_18, Class<?>... paramaters) {
        this._1_14 = _1_14;
        this._1_17 = _1_17;
        this._1_18 = _1_18;
        this.paramaters = paramaters;
    }

    MethodMapping(String _1_17, String _1_18, Class<?>... paramaters) {
        this._1_14 = null;
        this._1_17 = _1_17;
        this._1_18 = _1_18;
        this.paramaters = paramaters;
    }

    MethodMapping(String _1_18, Class<?>... paramaters) {
        this._1_14 = null;
        this._1_17 = null;
        this._1_18 = _1_18;
        this.paramaters = paramaters;
    }

    public Method getMethod(Class<?> clazz) {
        try {

            String methodName = _1_18;
            switch (ServerVersion.getServerVersion()) {
                case V1_14:
                    if (_1_14 != null)
                        methodName = _1_14;
                    break;
                case V1_17:
                    if (_1_17 != null)
                        methodName = _1_17;
                    break;
            }

            Method method = clazz.getDeclaredMethod(methodName, paramaters);
            method.setAccessible(true);

            return method;
        } catch (Exception e) {
            return null;
        }
    }
}
