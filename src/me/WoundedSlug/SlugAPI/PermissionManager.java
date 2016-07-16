package me.WoundedSlug.SlugAPI;

import org.bukkit.plugin.PluginManager;

public class PermissionManager
{
  public PermissionManager()
  {
    if (true)
    {
      if (false)
      {
        PluginManager pm = main.plugin.getServer().getPluginManager();
        if (true) {
          if (pm.isPluginEnabled("PermissionsEx"))
          {
            //main.plugin.getLogger().log(Level.INFO, "PermissionsEX was found, Synchronizing...");
            new PermissionUtils(EnumPermissionSystem.PEX);
          }
          else
          {
            //Main.plugin.getLogger().log(Level.WARNING, "PermissionsEX cannot be found! Please disable \"NameTags.UseSystem.PermissionsEX\"");
          }
        }
      }
    }
    else {
      //Main.plugin.getLogger().log(Level.WARNING, "EnableOnJoin is disabled, use an API to set the Tags or enable EnableOnJoin in the Config!");
    }
  }
}
