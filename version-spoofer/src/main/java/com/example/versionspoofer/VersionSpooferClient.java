package com.example.versionspoofer;

import com.example.versionspoofer.gui.VersionSelectScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionSpooferClient implements ClientModInitializer {
    public static final String MOD_ID = "version-spoofer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Текущая выбранная версия для спойфинга
    public static String spoofedVersion = null;
    
    // Список поддерживаемых версий
    public static final String[] SUPPORTED_VERSIONS = {
        // Старые версии (до 1.19.2)
        "1.16.5",
        "1.17",
        "1.17.1",
        "1.18",
        "1.18.1",
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2",
        
        // Версии 1.20.x
        "1.20",
        "1.20.1",
        "1.20.2",
        "1.20.3",
        "1.20.4",
        "1.20.5",
        "1.20.6",
        
        // Версии 1.21.x
        "1.21",
        "1.21.1",
        "1.21.2",
        "1.21.3",
        "1.21.4",
        "1.21.5",
        "1.21.6",
        "1.21.7",
        "1.21.8",
        "1.21.9",
        "1.21.10",
        "1.21.11",
        
        // Будущие версии 26.x (2026 год)
        "26.1",
        "26.1.1",
        "26.1.2",
        "26.2",
        "26.3"
    };
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Version Spoofer initialized!");
        LOGGER.info("Поддерживаемые версии: от 1.16.5 до 26.3");
        
        // Регистрация событий подключения
        ClientLoginConnectionEvents.INIT.register((handler, client) -> {
            if (spoofedVersion != null) {
                LOGGER.info("Подключение с подменой версии: {}", spoofedVersion);
            }
        });
        
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (spoofedVersion != null) {
                LOGGER.info("Подключено к серверу с подменой версии на: {}", spoofedVersion);
            }
        });
    }
    
    /**
     * Получить протокол для указанной версии
     */
    public static int getProtocolVersion(String version) {
        return switch (version) {
            case "1.16.5" -> 754;
            case "1.17" -> 755;
            case "1.17.1" -> 756;
            case "1.18", "1.18.1", "1.18.2" -> 758;
            case "1.19", "1.19.1", "1.19.2" -> 760;
            case "1.19.3", "1.19.4" -> 761;
            case "1.20", "1.20.1" -> 763;
            case "1.20.2", "1.20.3", "1.20.4" -> 765;
            case "1.20.5", "1.20.6" -> 766;
            case "1.21", "1.21.1" -> 767;
            case "1.21.2", "1.21.3" -> 768;
            case "1.21.4" -> 769;
            case "1.21.5" -> 770;
            case "1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10", "1.21.11" -> 771;
            
            // Протоколы для будущих версий 26.x
            case "26.1", "26.1.1", "26.1.2" -> 800;
            case "26.2" -> 801;
            case "26.3" -> 802;
            
            default -> 760; // По умолчанию 1.19.2
        };
    }
}
