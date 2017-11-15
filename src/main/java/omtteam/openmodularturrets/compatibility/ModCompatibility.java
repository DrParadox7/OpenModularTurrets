package omtteam.openmodularturrets.compatibility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import omtteam.openmodularturrets.OpenModularTurrets;
import omtteam.openmodularturrets.compatibility.computercraft.CCPeripheralProvider;
import omtteam.openmodularturrets.compatibility.opencomputers.DriverTurretBase;
import omtteam.openmodularturrets.reference.Reference;

import static omtteam.omlib.compatibility.ModCompatibility.ComputerCraftLoaded;
import static omtteam.omlib.compatibility.ModCompatibility.OpenComputersLoaded;

/**
 * Created by Keridos on 23/01/2015. This Class
 */
public class ModCompatibility {
    public static boolean IGWModLoaded = false;
    public static boolean ThermalExpansionLoaded = false;
    public static boolean EnderIOLoaded = false;
    public static boolean MekanismLoaded = false;
    public static boolean ThaumcraftLoaded = false;
    public static boolean ValkyrienWarfareLoaded = false;
    @SuppressWarnings("unused")

    public static void checkForMods() {
        ThermalExpansionLoaded = Loader.isModLoaded(omtteam.omlib.compatibility.ModCompatibility.TEModID);
        if (ThermalExpansionLoaded) {
            OpenModularTurrets.getLogger().info("Hi there, dV=V0B(t1-t0)! (Found ThermalExpansion)");
        }

        EnderIOLoaded = Loader.isModLoaded(omtteam.omlib.compatibility.ModCompatibility.EIOModID);
        if (EnderIOLoaded) {
            OpenModularTurrets.getLogger().info("Not sure if iron ingot, or electrical steel ingot... (Found EnderIO)");
        }

        MekanismLoaded = Loader.isModLoaded(omtteam.omlib.compatibility.ModCompatibility.MekModID);
        if (MekanismLoaded) {
            OpenModularTurrets.getLogger().info("Mur omsimu, plz. (Found Mekanism)");
        }

        ThaumcraftLoaded = Loader.isModLoaded(omtteam.omlib.compatibility.ModCompatibility.TCModID);
        if (ThaumcraftLoaded) {
            OpenModularTurrets.getLogger().info("Afrikaners is plesierig. (Found Thaumcraft)");
        }

        if (OpenComputersLoaded || ComputerCraftLoaded) {
            OpenModularTurrets.getLogger().info("Enabling LUA integration. (Found OpenComputers/ComputerCraft)");
        }

        ValkyrienWarfareLoaded = Loader.isModLoaded("valkyrienwarfare");
        if (ValkyrienWarfareLoaded) {
            OpenModularTurrets.getLogger().info("Valkyrien Warfare Found! You have a good taste in mods");
        }

        IGWModLoaded = Loader.isModLoaded("igwmod");
    }

    private static void addVersionCheckerInfo() {
        NBTTagCompound versionchecker = new NBTTagCompound();
        versionchecker.setString("curseProjectName", "openmodularturrets");
        versionchecker.setString("curseFilenameParser", "OpenModularTurrets-1.12.2-[].jar");
        versionchecker.setString("modDisplayName", "OpenModularTurrets");
        versionchecker.setString("oldVersion", Reference.VERSION);
        FMLInterModComms.sendRuntimeMessage("openmodularturrets", "versionChecker", "addCurseCheck", versionchecker);
    }

    public static void init() {
        FMLInterModComms.sendMessage("waila", "register",
                "omtteam.openmodularturrets.compatibility.hwyla.WailaDataProvider.register");

        addVersionCheckerInfo();
        if (ComputerCraftLoaded) {
            registerCCCompat();
        }
        if (OpenComputersLoaded) {
            registerOCCompat();
        }
    }

    public static void preinit() {

    }

    private static void registerOCCompat() {
        DriverTurretBase.getInstance().registerWrapper();
    }

    private static void registerCCCompat() {
        CCPeripheralProvider.getInstance().registerWrapper();
    }
}
