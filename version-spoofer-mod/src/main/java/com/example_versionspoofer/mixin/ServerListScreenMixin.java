package com.example_versionspoofer.mixin;

import com.example_versionspoofer.VersionSpooferClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.mouse.Mouse;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerServerListScreen.class)
public class ServerListScreenMixin {
    
    @Unique
    private ButtonWidget spoofButton;
    
    @Unique
    private boolean isDragging = false;
    
    @Unique
    private int dragStartX = 0;
    
    @Unique
    private int dragStartY = 0;
    
    @Unique
    private int buttonX = 10;
    
    @Unique
    private int buttonY = 10;
    
    @Unique
    private static final int BUTTON_WIDTH = 120;
    
    @Unique
    private static final int BUTTON_HEIGHT = 20;
    
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        MultiplayerServerListScreen screen = (MultiplayerServerListScreen) (Object) this;
        
        String buttonText = VersionSpooferClient.currentSpoofVersion != null 
            ? "v" + VersionSpooferClient.currentSpoofVersion 
            : "Select Version";
        
        spoofButton = new ButtonWidget(
            buttonX, 
            buttonY, 
            BUTTON_WIDTH, 
            BUTTON_HEIGHT, 
            Text.of(buttonText),
            button -> {
                // Открываем экран выбора версии
                MinecraftClient.getInstance().setScreen(new VersionSelectScreen(screen));
            }
        );
        
        screen.addDrawableChild(spoofButton);
    }
    
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfo ci) {
        if (spoofButton != null && 
            mouseX >= spoofButton.getX() && 
            mouseX <= spoofButton.getX() + BUTTON_WIDTH &&
            mouseY >= spoofButton.getY() && 
            mouseY <= spoofButton.getY() + BUTTON_HEIGHT &&
            button == 0) { // Левая кнопка мыши
            
            isDragging = true;
            dragStartX = (int) mouseX - spoofButton.getX();
            dragStartY = (int) mouseY - spoofButton.getY();
        }
    }
    
    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void onMouseReleased(double mouseX, double mouseY, int button, CallbackInfo ci) {
        if (button == 0) {
            isDragging = false;
        }
    }
    
    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfo ci) {
        if (isDragging && spoofButton != null) {
            int newX = (int) mouseX - dragStartX;
            int newY = (int) mouseY - dragStartY;
            
            // Ограничиваем перемещение пределами экрана
            MultiplayerServerListScreen screen = (MultiplayerServerListScreen) (Object) this;
            newX = Math.max(0, Math.min(newX, screen.width - BUTTON_WIDTH));
            newY = Math.max(0, Math.min(newY, screen.height - BUTTON_HEIGHT));
            
            spoofButton.setPosition(newX, newY);
            buttonX = newX;
            buttonY = newY;
        }
    }
}
