package me.WoundedSlug.SlugAPI;

import java.io.IOException;

import ru.tehkode.permissions.bukkit.PermissionsEx;

class PermissionUtils
{
  private PermissionsEx pex = (PermissionsEx)PermissionsEx.getPlugin();
  private static EnumPermissionSystem a;
  
  public PermissionUtils(EnumPermissionSystem a)
  {
    if (a.equals(EnumPermissionSystem.PEX)) {
      
    }
  }
  
  private boolean spex()
    throws IOException
  {
    
    return true;
  }
  
  public static EnumPermissionSystem getPermissionSystem()
  {
    return a;
  }
}