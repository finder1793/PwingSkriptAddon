package com.pwing.pwingskriptaddon.effects.misc;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffSYaml extends Effect {

    private Expression<String> yamlFile;
    private Expression<String> key;
    private Expression<String> value;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        yamlFile = (Expression<String>) exprs[0];
        key = (Expression<String>) exprs[1];
        value = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "set yaml " + yamlFile.toString(e, debug) + " key " + key.toString(e, debug) + " to " + value.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String file = yamlFile.getSingle(e);
        String yamlKey = key.getSingle(e);
        String yamlValue = value.getSingle(e);
        if (file != null && yamlKey != null && yamlValue != null) {
            try {
                java.io.File yamlFile = new java.io.File(file);
                java.util.Map<String, Object> yamlData = new java.util.HashMap<>();

                // Load existing YAML if file exists
                if (yamlFile.exists()) {
                    try (java.io.FileReader reader = new java.io.FileReader(yamlFile)) {
                        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
                        Object loaded = yaml.load(reader);
                        if (loaded instanceof java.util.Map) {
                            @SuppressWarnings("unchecked")
                            java.util.Map<String, Object> temp = (java.util.Map<String, Object>) loaded;
                            yamlData = temp;
                        }
                    } catch (Exception loadEx) {
                        // If loading fails, start with empty map
                        yamlData = new java.util.HashMap<>();
                    }
                }

                // Set the value (supporting nested keys with dots)
                setNestedValue(yamlData, yamlKey, yamlValue);

                // Save the YAML file
                try (java.io.FileWriter writer = new java.io.FileWriter(yamlFile)) {
                    org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
                    yaml.dump(yamlData, writer);
                }

                System.out.println("Successfully set YAML value: " + yamlKey + " = " + yamlValue);
            } catch (Exception ex) {
                System.out.println("Failed to set YAML value: " + ex.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setNestedValue(java.util.Map<String, Object> map, String key, Object value) {
        String[] parts = key.split("\\.");
        java.util.Map<String, Object> current = map;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.containsKey(part) || !(current.get(part) instanceof java.util.Map)) {
                current.put(part, new java.util.HashMap<String, Object>());
            }
            current = (java.util.Map<String, Object>) current.get(part);
        }

        current.put(parts[parts.length - 1], value);
    }
}
