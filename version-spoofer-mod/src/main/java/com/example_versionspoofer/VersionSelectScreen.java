package com.example_versionspoofer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

public class VersionSelectScreen extends Screen {
    private final MultiplayerServerListScreen parent;
    private VersionEntryList entryList;
    
    public VersionSelectScreen(MultiplayerServerListScreen parent) {
        super(Text.of("Select Version"));
        this.parent = parent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        entryList = new VersionEntryList(this.client, this.width, this.height, 30, this.height - 50, 25);
        this.addSelectableChild(entryList);
        
        // Кнопка "Назад"
        ButtonWidget backButton = new ButtonWidget(
            this.width / 2 - 100,
            this.height - 40,
            200,
            20,
            Text.of("Back"),
            btn -> client.setScreen(parent)
        );
        
        this.addDrawableChild(backButton);
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground(delta);
        
        // Заголовок
        drawCenteredTextWithShadow(this.textRenderer, "Select Version to Spoof", this.width / 2, 15, 0xFFFFFF);
        
        entryList.render(mouseX, mouseY, delta);
        super.render(mouseX, mouseY, delta);
    }
    
    // Внутренний класс для списка версий
    private class VersionEntryList extends ElementListWidget<VersionEntryList.VersionEntry> {
        public VersionEntryList(net.minecraft.client.MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
            super(client, width, height, top, bottom, itemHeight);
            
            // Добавляем все версии из списка
            for (String version : VersionSpooferClient.SPOOF_VERSIONS) {
                addEntry(new VersionEntry(version));
            }
        }
        
        @Override
        public int getRowWidth() {
            return 200;
        }
        
        @Override
        public int getScrollbarPosition() {
            return this.width / 2 + this.getRowWidth() / 2 + 10;
        }
        
        public class VersionEntry extends ElementListWidget.Entry<VersionEntry> {
            private final String version;
            private ButtonWidget selectButton;
            
            public VersionEntry(String version) {
                this.version = version;
            }
            
            @Override
            public void render(net.minecraft.client.gui.DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                if (selectButton == null) {
                    selectButton = new ButtonWidget(
                        x + 50,
                        y,
                        100,
                        20,
                        Text.of(version),
                        btn -> {
                            VersionSpooferClient.currentSpoofVersion = version;
                            client.setScreen(parent);
                        }
                    );
                    
                    VersionSelectScreen.this.addDrawableChild(selectButton);
                }
                
                selectButton.setPosition(x + 50, y);
                selectButton.render(context, mouseX, mouseY, tickDelta);
                
                // Индикатор текущей выбранной версии
                if (VersionSpooferClient.currentSpoofVersion != null && VersionSpooferClient.currentSpoofVersion.equals(version)) {
                    context.drawText(textRenderer, "✓", x + 10, y + 6, 0x00FF00, false);
                }
            }
            
            @Override
            public java.util.List<? extends net.minecraft.client.gui.Element> children() {
                return selectButton != null ? java.util.Collections.singletonList(selectButton) : java.util.Collections.emptyList();
            }
            
            @Override
            public java.util.List<? extends net.minecraft.client.gui.narration.NarratableEntry> narratables() {
                return selectButton != null ? java.util.Collections.singletonList(selectButton) : java.util.Collections.emptyList();
            }
        }
    }
}
