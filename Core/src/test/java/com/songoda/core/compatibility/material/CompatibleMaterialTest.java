package com.songoda.core.compatibility.material;

import be.seeseemelk.mockbukkit.MockBukkit;
import com.songoda.core.compatibility.CompatibleMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompatibleMaterialTest {
    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void getMaterialForNull() {
        assertNull(CompatibleMaterial.getMaterial((Material) null));
        assertNull(CompatibleMaterial.getMaterial((ItemStack) null));
    }

    @Disabled("This test causes issues and is version dependent")
    @Test
    void getMaterialForAllBukkitMaterials() {
        Map<CompatibleMaterial, Material> returnedMaterials = new HashMap<>(Material.values().length);
        for (Material bukkitMaterial : Material.values()) {
            CompatibleMaterial compatibleMaterial = CompatibleMaterial.getMaterial(bukkitMaterial);

            if (bukkitMaterial.name().startsWith("LEGACY_")) {
                assertNull(compatibleMaterial);
                continue;
            }
            assertNotNull(compatibleMaterial, () -> "Could not get an CompatibleMaterial for Material." + bukkitMaterial.name());
            assertEquals(bukkitMaterial, compatibleMaterial.getMaterial());
            assertEquals(compatibleMaterial, CompatibleMaterial.getMaterial(bukkitMaterial.name()));

            assertFalse(returnedMaterials.containsKey(compatibleMaterial),
                    () -> String.format("Assertion failed when converting Material.%s to CompatibleMaterial.%s: " +
                                    "CompatibleMaterial.%1$s has already been returned for Material.%3$s previously",
                            bukkitMaterial.name(),
                            compatibleMaterial.name(),
                            returnedMaterials.get(compatibleMaterial).name()
                    ));

            assertEquals(bukkitMaterial.isAir(), compatibleMaterial.isAir(), getMaterialPropertyAssertionError(compatibleMaterial, "Air"));
            assertEquals(bukkitMaterial.isBlock(), compatibleMaterial.isBlock(), getMaterialPropertyAssertionError(compatibleMaterial, "Block"));
            assertEquals(bukkitMaterial.isBurnable(), compatibleMaterial.isBurnable(), getMaterialPropertyAssertionError(compatibleMaterial, "Burnable"));
            assertEquals(bukkitMaterial.isEdible(), compatibleMaterial.isEdible(), getMaterialPropertyAssertionError(compatibleMaterial, "Edible"));
            assertEquals(bukkitMaterial.isFlammable(), compatibleMaterial.isFlammable(), getMaterialPropertyAssertionError(compatibleMaterial, "Flammable"));
            assertEquals(bukkitMaterial.isFuel(), compatibleMaterial.isFuel(), getMaterialPropertyAssertionError(compatibleMaterial, "Fuel"));
            assertEquals(bukkitMaterial.isInteractable(), compatibleMaterial.isInteractable(), getMaterialPropertyAssertionError(compatibleMaterial, "Interactable"));
            assertEquals(bukkitMaterial.isItem(), compatibleMaterial.isItem(), getMaterialPropertyAssertionError(compatibleMaterial, "Item"));
            assertEquals(bukkitMaterial.isOccluding(), compatibleMaterial.isOccluding(), getMaterialPropertyAssertionError(compatibleMaterial, "Occluding"));
            assertEquals(bukkitMaterial.isSolid(), compatibleMaterial.isSolid(), getMaterialPropertyAssertionError(compatibleMaterial, "Solid"));
            assertEquals(bukkitMaterial.isTransparent(), compatibleMaterial.isTransparent(), getMaterialPropertyAssertionError(compatibleMaterial, "Transparent"));

            assertFalse(compatibleMaterial.usesCompatibility());
            assertFalse(compatibleMaterial.usesData());
            assertEquals(-1, compatibleMaterial.getData());

            ItemStack compatibleItem = compatibleMaterial.getItem();
            assertEquals(bukkitMaterial, compatibleItem.getType());
            assertEquals(compatibleMaterial, CompatibleMaterial.getMaterial(compatibleItem));

            returnedMaterials.put(compatibleMaterial, bukkitMaterial);
        }
    }

    private Supplier<String> getMaterialPropertyAssertionError(CompatibleMaterial compatibleMaterial, String propertyName) {
        return () -> String.format("Expected CompatibleMaterial.%s to be '%s'", compatibleMaterial.name(), propertyName);
    }
}
