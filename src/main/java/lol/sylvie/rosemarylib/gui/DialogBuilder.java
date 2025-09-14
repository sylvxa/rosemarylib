package lol.sylvie.rosemarylib.gui;

import lol.sylvie.rosemarylib.Rosemary;
import net.minecraft.dialog.*;
import net.minecraft.dialog.action.DialogAction;
import net.minecraft.dialog.action.DynamicCustomDialogAction;
import net.minecraft.dialog.body.DialogBody;
import net.minecraft.dialog.body.ItemDialogBody;
import net.minecraft.dialog.body.PlainMessageDialogBody;
import net.minecraft.dialog.input.*;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.dialog.type.DialogInput;
import net.minecraft.dialog.type.MultiActionDialog;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class DialogBuilder {
    private final ServerPlayerEntity player;
    private final Text title;
    private boolean closeOnEsc = true;
    private int columns = 1;

    private final ArrayList<DialogBody> body = new ArrayList<>();
    private final ArrayList<DialogInput> inputs = new ArrayList<>();
    private final ArrayList<DialogActionButtonData> buttons = new ArrayList<>();

    private final HashMap<Identifier, Consumer<NbtCompound>> actions = new HashMap<>();

    public DialogBuilder(ServerPlayerEntity player, Text title) {
        this.player = player;
        this.title = title;
    }

    public DialogBuilder closeOnEsc(boolean value) {
        this.closeOnEsc = value;
        return this;
    }

    public DialogBuilder columns(int value) {
        this.columns = value;
        return this;
    }

    public DialogBuilder bodyText(Text text) {
        this.body.add(new PlainMessageDialogBody(text, PlainMessageDialogBody.DEFAULT_WIDTH));
        return this;
    }

    public DialogBuilder bodyItem(ItemStack stack, PlainMessageDialogBody description, boolean showDecorations, boolean showTooltip, @Nullable Integer width, @Nullable Integer height) {
        this.body.add(new ItemDialogBody(stack, Optional.ofNullable(description), showDecorations, showTooltip, width == null ? PlainMessageDialogBody.DEFAULT_WIDTH : width, height == null ? 64 : height));
        return this;
    }

    public DialogBuilder bodyItem(ItemStack stack, Text text, int width, int height) {
        return this.bodyItem(stack, new PlainMessageDialogBody(text, PlainMessageDialogBody.DEFAULT_WIDTH), true, true, width, height);
    }

    public DialogBuilder bodyItem(ItemStack stack, boolean showDecorations, boolean showTooltip, int width, int height) {
        return this.bodyItem(stack, null, showDecorations, showTooltip, width, height);
    }

    public DialogBuilder bodyItem(ItemStack stack, int width, int height) {
        return this.bodyItem(stack, false, false, width, height);
    }


    public DialogBuilder bodyItem(ItemStack stack, boolean showDecorations, boolean showTooltip) {
        return this.bodyItem(stack, null, showDecorations, showTooltip, 16, 16);
    }


    public DialogBuilder input(String key, InputControl control) {
        this.inputs.add(new DialogInput(key, control));
        return this;
    }

    public DialogBuilder textInput(String key, int width, Text label, String value, int maxLength, @Nullable TextInputControl.Multiline multiline) {
        return this.input(key, new TextInputControl(width, label, !label.getString().isEmpty(), value, maxLength, Optional.ofNullable(multiline)));
    }

    public DialogBuilder textInput(String key, int width, Text label, String value, int maxLength) {
        return this.textInput(key, width, label, value, maxLength, null);
    }

    public DialogBuilder checkboxInput(String key, Text label, boolean value) {
        return this.input(key, new BooleanInputControl(label, value, null, null));
    }

    public DialogBuilder numberInput(String key, int width, Text label, String format, NumberRangeInputControl.RangeInfo range) {
        return this.input(key, new NumberRangeInputControl(width, label, format, range));
    }

    public DialogBuilder numberInput(String key, int width, Text label, NumberRangeInputControl.RangeInfo range) {
        return this.numberInput(key, width, label, "%s: %s", range);
    }

    public DialogBuilder singleOptionInput(String key, int width, Text label, List<SingleOptionInputControl.Entry> options) {
        return this.input(key, new SingleOptionInputControl(width, options, label, !label.getString().isEmpty()));
    }

    public DialogBuilder actionButton(Identifier buttonId, Text text, Consumer<NbtCompound> callback, @Nullable NbtCompound extraData, @Nullable Integer buttonWidth) {
        DialogAction action = new DynamicCustomDialogAction(buttonId, Optional.ofNullable(extraData));
        DialogActionButtonData button = new DialogActionButtonData(new DialogButtonData(text, buttonWidth == null ? Dialogs.BUTTON_WIDTH : buttonWidth), Optional.of(action));
        buttons.add(button);
        if (!actions.containsKey(buttonId))
            actions.put(buttonId, callback);
        else Rosemary.LOGGER.warn("Action {} was added twice :(", buttonId);
        return this;
    }

    public DialogBuilder actionButton(Identifier buttonId, Text text, Consumer<NbtCompound> callback) {
        return this.actionButton(buttonId, text, callback, null, null);
    }

    public Dialog build() {
        for (Map.Entry<Identifier, Consumer<NbtCompound>> entry : actions.entrySet()) {
            DialogManager.register(player, entry.getKey(), entry.getValue());
        }
        DialogCommonData data = new DialogCommonData(this.title, Optional.empty(), closeOnEsc, false, AfterAction.CLOSE, body, inputs);
        return new MultiActionDialog(data, buttons, Optional.empty(), columns);
    }

    public void buildAndOpen(ServerPlayerEntity player) {
        player.openDialog(new RegistryEntry.Direct<>(this.build()));
    }
}