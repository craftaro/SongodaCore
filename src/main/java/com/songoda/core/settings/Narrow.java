package com.songoda.core.settings;

import java.util.*;

public class Narrow {

    protected final Map<String, FoundSetting> settings = new LinkedHashMap<>();

    public Narrow() {
    }

    public Narrow(Set<FoundSetting> settings) {
        for (FoundSetting setting : settings)
            this.settings.put(setting.getKey(), setting);
    }

    public Narrow narrow(String key) {
        Set<FoundSetting> settings = new HashSet<>();
        for (FoundSetting setting : this.settings.values()) {
            if (setting.getKey().startsWith(key))
                settings.add(setting);
        }
        return new Narrow(settings);
    }

    public Collection<Section> getSection() {
        Map<String, Section> sections = new HashMap<>();
        for (FoundSetting setting : settings.values()) {
            String section = setting.getKey().contains(".") ? setting.getKey().split("\\.")[0] : setting.getKey();
            if (!sections.containsKey(section))
                sections.put(section, new Section(section));
            sections.get(section).getNarrow().addSetting(setting);
        }
        return sections.values();
    }

    public void addSetting(FoundSetting setting) {
        this.settings.put(setting.getKey(), setting);
    }

    public List<FoundSetting> getSettings() {
        return new ArrayList<>(settings.values());
    }

    public Setting getSetting(String setting) {
        for (String string : settings.keySet())
            if (string.equalsIgnoreCase(setting))
                return settings.get(string);
        return new Setting();
    }

}
