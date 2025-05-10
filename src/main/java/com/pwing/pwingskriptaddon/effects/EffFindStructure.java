package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.event.Event;

public class EffFindStructure extends Effect {

    private Expression<String> worldName;
    private Expression<String> structureType;
    private Expression<Number> x;
    private Expression<Number> z;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        worldName = (Expression<String>) exprs[0];
        structureType = (Expression<String>) exprs[1];
        x = (Expression<Number>) exprs[2];
        z = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String worldNameStr = worldName.getSingle(e);
        String structureTypeStr = structureType.getSingle(e);
        Number xCoord = x.getSingle(e);
        Number zCoord = z.getSingle(e);

        if (worldNameStr == null || structureTypeStr == null || xCoord == null || zCoord == null) {
            return;
        }

        World world = Bukkit.getWorld(worldNameStr);
        if (world == null) {
            return;
        }

        StructureType structure = StructureType.getStructureTypes().get(structureTypeStr.toLowerCase());
        if (structure == null) {
            return;
        }

        Location location = world.locateNearestStructure(new Location(world, xCoord.doubleValue(), 0, zCoord.doubleValue()), structure, 100, false);
        if (location != null) {
            // Example: Store the location in a variable or perform further actions
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "find structure " + structureType.toString(e, debug) + " near x " + x.toString(e, debug) + ", z " + z.toString(e, debug) + " in world " + worldName.toString(e, debug);
    }
}
