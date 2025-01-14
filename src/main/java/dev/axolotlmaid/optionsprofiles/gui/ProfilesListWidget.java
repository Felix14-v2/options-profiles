package dev.axolotlmaid.optionsprofiles.gui;

import com.google.common.collect.ImmutableList;
import dev.axolotlmaid.optionsprofiles.Profiles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;

@Environment(EnvType.CLIENT)
public class ProfilesListWidget extends ElementListWidget<ProfilesListWidget.Entry> {
    final ProfilesScreen parent;

    public void refreshEntries() {
        File profilesDirectory = new Profiles().getDirectory();

        this.clearEntries();

        for (File profile : Objects.requireNonNull(profilesDirectory.listFiles())) {
            String profileName = FilenameUtils.removeExtension(profile.getName());
            this.addEntry(new ProfilesListWidget.ProfileEntry(Text.of(profileName)));

            // Sodium
            // This code doesn't work yet
//            if (!(profileName.contains("-sodium-options"))) {
//                this.addEntry(new ProfilesListWidget.ProfileEntry(Text.of(profileName)));
//            }
        }
    }

    public ProfilesListWidget(ProfilesScreen parent, MinecraftClient client) {
        super(client, parent.width + 45, parent.height, 20, parent.height - 32, 20);
        this.parent = parent;

        refreshEntries();
    }

    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 15;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }

    @Environment(EnvType.CLIENT)
    public class ProfileEntry extends ProfilesListWidget.Entry {
        private final ButtonWidget editButton;
        private final ButtonWidget loadButton;
        private final Text profileName;

        ProfileEntry(final Text profileName) {
            this.profileName = profileName;
            this.editButton = new ButtonWidget.Builder(Text.translatable("gui.options-profiles.edit-profile-text"), (button) -> {
                ProfilesListWidget.this.client.setScreen(new EditProfileScreen(parent, profileName));
            }).position(0, 0).size(75, 20).build();

            this.loadButton = new ButtonWidget.Builder(Text.translatable("gui.options-profiles.load-profile-text"), (button) -> {
                new Profiles().overwriteOptionsFile(profileName.getString());

                client.options.load();
                client.worldRenderer.reload();
                client.getSoundManager().reloadSounds();
            }).position(0, 0).size(75, 20).build();
        }

        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            TextRenderer var10000 = ProfilesListWidget.this.client.textRenderer;

            int var10004 = y + entryHeight / 2;

            Objects.requireNonNull(ProfilesListWidget.this.client.textRenderer);
            var10000.draw(matrices, profileName, x, (float)(var10004 - 9 / 2), 16777215);

            this.editButton.setX(x + 115);
            this.editButton.setY(y);
            this.editButton.render(matrices, mouseX, mouseY, tickDelta);

            this.loadButton.setX(x + 190);
            this.loadButton.setY(y);
            this.loadButton.render(matrices, mouseX, mouseY, tickDelta);
        }

        public List<? extends Element> children() {
            return ImmutableList.of(this.editButton, this.loadButton);
        }

        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.editButton, this.loadButton);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.editButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            } else {
                return this.loadButton.mouseClicked(mouseX, mouseY, button);
            }
        }

        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.editButton.mouseReleased(mouseX, mouseY, button) || this.loadButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
        public Entry() {
        }
    }
}
