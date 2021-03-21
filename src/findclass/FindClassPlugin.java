package findclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FindClassPlugin extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }

        String className = args[0];

        Method findClass;

        try {
            findClass = getClassLoader().getClass().getDeclaredMethod("findClass", String.class, boolean.class);
            findClass.setAccessible(true);
        } catch (NoSuchMethodException e) {
            sender.sendMessage("Failed to get findClass() method: " + e.getMessage());
            return true;
        }

        sender.sendMessage("--- Finding class: " + className);

        int foundCount = 0;
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            ClassLoader classLoader = plugin.getClass().getClassLoader();

            try {
                findClass.invoke(classLoader, className, false);
                foundCount++;
                sender.sendMessage("Found on plugin: " + plugin.getName());
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    if (e.getCause() instanceof ClassNotFoundException) {
                        continue; // this is fine
                    }
                }

                sender.sendMessage("Failed to run findClass() : " + e.getMessage());
                e.printStackTrace();
                return true;
            }
        }

        sender.sendMessage("--- " + foundCount + " found");

        return true;
    }
}
