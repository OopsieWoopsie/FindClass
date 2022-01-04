package findclass;

import java.net.URL;

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

        sender.sendMessage("--- Finding class: " + className);

        int foundCount = 0;
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            ClassLoader classLoader = plugin.getClass().getClassLoader();

            if (!plugin.isEnabled()) {
                sender.sendMessage(plugin.getName() + " > Skipping (disabled)");
                continue;
            }

            URL cls = classLoader.getResource(className.replace('.', '/') + ".class");

            if (cls != null) {
                foundCount++;
                sender.sendMessage(plugin.getName() + " > Class found!");
            }
        }

        sender.sendMessage("--- " + foundCount + " found");

        return true;
    }
}
