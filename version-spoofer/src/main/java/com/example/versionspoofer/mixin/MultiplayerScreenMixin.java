package com.example.versionspoofer.mixin;

import com.example.versionspoofer.VersionSpooferClient;
import com.example.versionspoofer.gui.VersionSelectScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Миксин для добавления кнопки выбора версии в экран мультиплеера
 */
@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    
    @Unique
    private ButtonWidget versionSpooferButton;
    
    @Unique
    private int buttonX, buttonY;
    
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }
    
    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        MultiplayerScreen screen = (MultiplayerScreen)(Object)this;
        
        // Начальная позиция кнопки (справа сверху)
        buttonX = width - 130;
        buttonY = 10;
        
        // Создаем перетаскиваемую кнопку
        versionSpooferButton = ButtonWidget.builder(
            Text.literal("Version"),
            btn -> {
                // Открываем экран выбора версии
                screen.setScreen(new VersionSelectScreen(screen));
            }
        )
        .dimensions(buttonX, buttonY, 120, 20)
        .build();
        
        // Добавляем кнопку как элемент для рендеринга
        addDrawableChild(versionSpooferButton);
    }
    
    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfo ci) {
        if (versionSpooferButton != null && isMouseOverButton(mouseX, mouseY)) {
            // Перетаскивание кнопки
            buttonX = (int)(buttonX + deltaX);
            buttonY = (int)(buttonY + deltaY);
            
            // Ограничиваем позицию в пределах экрана
            buttonX = Math.max(0, Math.min(width - versionSpooferButton.getWidth(), buttonX));
            buttonY = Math.max(0, Math.min(height - versionSpooferButton.getHeight(), buttonY));
            
            versionSpooferButton.setPosition(buttonX, buttonY);
            ci.cancel();
        }
    }
    
    @Unique
    private boolean isMouseOverButton(double mouseX, double mouseY) {
        return versionSpooferButton != null && 
               mouseX >= buttonX && 
               mouseX <= buttonX + versionSpooferButton.getWidth() &&
               mouseY >= buttonY && 
               mouseY <= buttonY + versionSpooferButton.getHeight();
    }
}
