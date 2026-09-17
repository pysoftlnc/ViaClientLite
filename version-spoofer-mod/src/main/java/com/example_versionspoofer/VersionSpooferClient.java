package com.example_versionspoofer;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionSpooferClient implements ClientModInitializer {
    public static final String MOD_ID = "version-spoofer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Список версий для спуфинга
    public static final String[] SPOOF_VERSIONS = new String[] {
        "1.7.10",
        "1.8.9",
        "1.9.4",
        "1.10.2",
        "1.11.2",
        "1.12.2",
        "1.13.2",
        "1.14.4",
        "1.15.2",
        "1.16.5",
        "1.17.1",
        "1.18.2",
        "1.19",
        "1.19.1",
        "1.19.2",
        "1.19.3",
        "1.19.4",
        "1.20",
        "1.20.1",
        "1.20.2",
        "1.20.3",
        "1.20.4",
        "1.20.5",
        "1.20.6",
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
        "26.1",
        "26.1.1",
        "26.1.2",
        "26.2",
        "26.3"
    };
    
    // Текущая выбранная версия для спуфинга (по умолчанию - текущая версия клиента)
    public static String currentSpoofVersion = null;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Version Spoofer initialized!");
        LOGGER.info("Available spoof versions: {}", String.join(", ", SPOOF_VERSIONS));
    }
}
