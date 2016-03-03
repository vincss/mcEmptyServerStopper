package ess.emptyserverstopper;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class EmptyServerStopper extends JavaPlugin implements Listener {

    private final boolean Debug = false;
    int m_ShutdownTimeInMinutes = 60;
    boolean m_ShutdownAtStart = false;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        LoadConfig();
        Init();
    }

    @Override
    public void onDisable() {
        //getLogger().info("EmptyServerStopper : Disabled.");
    }

    private void LoadConfig() {
        WriteDebugLog("Begin");

        try {
            //Init config
            File ESSConfigFile = new File(getDataFolder(), "config.yml");
            FileConfiguration config = getConfig();

            if (!config.contains("ShutdownTime")) {
                config.set("ShutdownTime", m_ShutdownTimeInMinutes);
            }
            if (!config.contains("ShutdownAtStart")) {
                config.set("ShutdownAtStart", m_ShutdownAtStart);
            }

            m_ShutdownTimeInMinutes = config.getInt("ShutdownTime");
            m_ShutdownAtStart = config.getBoolean("ShutdownAtStart");

            WriteDebugLog("Shutdowntime:" + m_ShutdownTimeInMinutes + " ShutdownAtStart:" + m_ShutdownAtStart);

            config.save(ESSConfigFile);
        } catch (Exception ex) {
            getLogger().info("EmptyServerStopper : LoadConfig exception : " + ex);
        }

        WriteDebugLog("End");
    }

    private void Init() {
        WriteDebugLog("Begin");

        if (m_ShutdownAtStart) {
            CheckPlayerNumber();
        }

        WriteDebugLog("End");
    }

    private int GetPlayerNumber() {
    	return  getServer().getOnlinePlayers().size();
    }

    private void CheckPlayerNumber() {
        WriteDebugLog("Begin");

        if (GetPlayerNumber() <= 1) {
            getLogger().info("Server empty -> shutdown in " + m_ShutdownTimeInMinutes + " minute(s).");

            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                public void run() {
                    CheckAndStop();
                }
            }, m_ShutdownTimeInMinutes * 1200);
        }
    }

    //Player Exit Handler    
    private void CheckAndStop() {
        if (GetPlayerNumber() <= 0) {
            getServer().broadcastMessage("[EmptyServerStopper]Server empty -> shutingdown.");
            getServer().shutdown();
        } else {
            getLogger().info("Shutdown abort someone is connected.");
        }
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent _Event) {
        CheckPlayerNumber();
    }
    /*
     @EventHandler
     public void onPlayerEnter(PlayerLoginEvent _event) {
     int PlayerNumber = GetPlayerNumber();
     getLogger().info("Nbr Player " + PlayerNumber);
     }
     */

    private void WriteDebugLog(String _Msg) {
        if (Debug) {
            getLogger().info(Thread.currentThread().getStackTrace()[2].getMethodName() + " - " + _Msg);
        }
    }
}
