package dev.axolotlmaid.optionsprofiles.gui;

import dev.axolotlmaid.optionsprofiles.Profiles;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ProfilesScreen extends Screen {
    private final Screen parent;
    private ProfilesListWidget profilesList;

    public ProfilesScreen(Screen parent) {
        super(Text.translatable("gui.options-profiles.profiles-menu-text"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.profilesList = new ProfilesListWidget(this, this.client);
        this.addSelectableChild(this.profilesList);

        // Bottom Buttons
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155, this.height - 29, 150, 20, Text.translatable("gui.options-profiles.save-current-options"), (button -> {
            new Profiles().createProfile();
            this.profilesList.refreshEntries();
        })));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 155 + 160, this.height - 29, 150, 20, ScreenTexts.DONE, (button) -> {
            this.client.setScreen(this.parent);
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.profilesList.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}