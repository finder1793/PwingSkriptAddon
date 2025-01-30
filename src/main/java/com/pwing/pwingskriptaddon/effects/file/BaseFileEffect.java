package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.effects.EffChange;
import com.pwing.pwingskriptaddon.PwingSkriptAddon;

import java.util.logging.Logger;
import java.io.File;

public abstract class BaseFileEffect extends EffChange {
    protected static final Logger logger = PwingSkriptAddon.getPluginLogger();

    protected boolean validateFile(File file, boolean shouldExist) {
        if (shouldExist && !file.exists()) {
            logger.warning("File not found: " + file.getPath());
            return false;
        }
        return true;
    }

    protected void logError(String operation, Exception e) {
        logger.severe("Error during " + operation + ": " + e.getMessage());
        e.printStackTrace();
    }
}
