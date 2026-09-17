package com.example.versionspoofer.gui;

import com.example.versionspoofer.VersionSpooferClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

/**
 * Экран выбора версии для спойфинга
 */
public class VersionSelectScreen extends Screen {
    private final Screen parent;
    private ButtonWidget selectedButton;
    private int scrollOffset = 0;
    private final int maxScroll;
    
    public VersionSelectScreen(Screen parent) {
        super(Text.literal("Выберите версию"));
        this.parent = parent;
        this.maxScroll = Math.max(0, VersionSpooferClient.SUPPORTED_VERSIONS.length * 25 - (height - 100));
    }
    
    @Override
    protected void init() {
        super.init();
        
        int buttonWidth = 300;
        int startX = (width - buttonWidth) / 2;
        int startY = 50;
        
        for (int i = 0; i < VersionSpooferClient.SUPPORTED_VERSIONS.length; i++) {
            String version = VersionSpooferClient.SUPPORTED_VERSIONS[i];
            int y = startY + (i * 25) - scrollOffset;
            
            if (y >= 40 && y <= height - 60) {
                boolean isCurrent = version.equals(VersionSpooferClient.spoofedVersion);
                Text buttonText = Text.literal(version + (isCurrent ? " ✓" : ""));
                
                ButtonWidget button = ButtonWidget.builder(buttonText, btn -> {
                    VersionSpooferClient.spoofedVersion = version;
                    MinecraftClient.getInstance().setScreen(parent);
                })
                .dimensions(startX, y, buttonWidth, 20)
                .build();
                
                addDrawableChild(button);
                
                if (isCurrent) {
                    selectedButton = button;
                }
            }
        }
        
        // Кнопка "Отмена"
        ButtonWidget cancelButton = ButtonWidget.builder(
            Text.literal("Отмена"),
            btn -> MinecraftClient.getInstance().setScreen(parent)
        )
        .dimensions((width - 100) / 2, height - 50, 100, 20)
        .build();
        addDrawableChild(cancelButton);
        
        // Кнопка "Сбросить"
        ButtonWidget resetButton = ButtonWidget.builder(
            Text.literal("Сбросить"),
            btn -> {
                VersionSpooferClient.spoofedVersion = null;
                MinecraftClient.getInstance().setScreen(parent);
            }
        )
        .dimensions((width - 100) / 2 + 110, height - 50, 100, 20)
        .build();
        addDrawableChild(resetButton);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Заголовок
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
        
        // Инструкция
        context.drawCenteredTextWithShadow(textRenderer, 
            "Текущая версия: " + (VersionSpooferClient.spoofedVersion != null ? VersionSpooferClient.spoofedVersion : "Оригинальная (1.19.2)"),
            width / 2, 35, 0xAAAAAA);
        
        // Полосы прокрутки (если нужно)
        if (maxScroll > 0) {
            int scrollbarHeight = Math.max(20, (height - 100) * (height - 100) / (VersionSpooferClient.SUPPORTED_VERSIONS.length * 25));
            int scrollbarY = 50 + (scrollOffset * (height - 100 - scrollbarHeight) / maxScroll);
            context.fill(width - 10, scrollbarY, width - 5, scrollbarY + scrollbarHeight, 0xFFFFFFFF);
        }
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0) {
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int)(verticalAmount * 10)));
            init(); // Пересоздать кнопки с новым offset
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
