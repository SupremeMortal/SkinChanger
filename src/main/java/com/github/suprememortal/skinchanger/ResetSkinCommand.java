package com.github.suprememortal.skinchanger;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class ResetSkinCommand extends Command {
    private final SkinChanger skinChanger;

    public ResetSkinCommand(SkinChanger skinChanger) {
        super("resetskin", "Reset player's skin", "/reset", new String[]{"rs"});
        this.skinChanger = skinChanger;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player");
            return true;
        }
        if (skinChanger.resetSkin((Player) sender)) {
            sender.sendMessage("Skin reset");
        } else {
            sender.sendMessage("Skin not reset");
        }
        return true;
    }
}
