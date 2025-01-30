package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class EffectEditYaml extends BaseFileEffect {
    private Expression<String> filePath;
    private Expression<String> key;
    private Expression<String> value;
    private Expression<Integer> lineNumber;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        key = (Expression<String>) exprs[1];
        value = (Expression<String>) exprs[2];
        if (matchedPattern == 1) {
            lineNumber = (Expression<Integer>) exprs[3];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        File file = new File(filePath.getSingle(e));
        
        if (!validateFile(file, true)) {
            return;
        }

        try {
            if (lineNumber != null) {
                editLineInYaml(file, key.getSingle(e), value.getSingle(e), lineNumber.getSingle(e));
            } else {
                editYamlValue(file, key.getSingle(e), value.getSingle(e));
            }
        } catch (Exception ex) {
            logError("editing YAML file", ex);
        }
    }

    private void editYamlValue(File file, String key, String value) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            Yaml yaml = new Yaml(new Constructor(Map.class));
            Map<String, Object> data = yaml.load(fis);

            data.put(key, value);

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yamlWriter = new Yaml(options);

            try (FileWriter writer = new FileWriter(file)) {
                yamlWriter.dump(data, writer);
            }
        }
    }

    private void editLineInYaml(File file, String key, String value, int lineNumber) throws IOException {
        var lines = Files.readAllLines(file.toPath());
        if (lineNumber > 0 && lineNumber <= lines.size()) {
            lines.set(lineNumber - 1, key + ": " + value);
            Files.write(file.toPath(), lines);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "edit yaml " + filePath.toString(e, debug) + " set " + key.toString(e, debug) + " to " + value.toString(e, debug);
    }
}
