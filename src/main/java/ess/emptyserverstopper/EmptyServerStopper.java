package ess.emptyserverstopper;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EmptyServerStopper extends JavaPlugin implements Listener {

    private final boolean Debug = true;
    int m_ShutdownTimeInMinutes = 60;
    boolean m_ShutdownAtStart = false;
    int m_currentTimer = 0;

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

            WriteDebugLog(String.format("ShutdownTime:%d ShutdownAtStart:%s", m_ShutdownTimeInMinutes, m_ShutdownAtStart));

            config.save(ESSConfigFile);
        } catch (Exception ex) {
            getLogger().info(String.format("EmptyServerStopper : LoadConfig exception : %s", ex));
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
        return getServer().getOnlinePlayers().size();
    }

    private void CheckPlayerNumber() {
        WriteDebugLog("Begin");

        if (GetPlayerNumber() <= 1) {
            getLogger().info(String.format("Server empty -> shutdown in %d minute(s).", m_ShutdownTimeInMinutes));

            CancelTimer();

            m_currentTimer = getServer().getScheduler().scheduleSyncDelayedTask(this, this::CheckAndStop, m_ShutdownTimeInMinutes * 1200L);
        }
    }

    //Player Exit Handler    
    private void CheckAndStop() {
        if (GetPlayerNumber() <= 0) {
            getServer().broadcastMessage("[EmptyServerStopper]Server empty -> Shutting Down.");
            getServer().shutdown();
        } else {
            getLogger().info("Shutdown abort someone is connected.");
        }
    }

    private void CancelTimer() {
        if (m_currentTimer > 0) {
            getLogger().info(String.format("Shutdown canceled. [%d]", m_currentTimer));
            getServer().getScheduler().cancelTask(m_currentTimer);
            m_currentTimer = 0;
        }
    }

    @EventHandler
    public void onPlayerExit() {
        WriteDebugLog("onPlayerExit");
        CheckPlayerNumber();
    }

    @EventHandler
    public void onPlayerEnter() {
        WriteDebugLog("onPlayerEnter");
        CancelTimer();
    }

    private void WriteDebugLog(String _Msg) {
        if (Debug) {
            getLogger().info(String.format("%s - %s", Thread.currentThread().getStackTrace()[2].getMethodName(), _Msg));
        }
    }
}
