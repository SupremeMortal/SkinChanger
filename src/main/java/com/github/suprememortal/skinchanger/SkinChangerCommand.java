package com.github.suprememortal.skinchanger;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import com.google.common.base.Strings;

public class SkinChangerCommand extends Command {
    private final SkinChanger skinChanger;

    public SkinChangerCommand(SkinChanger skinChanger) {
        super("changeskin", "Change skin", "/changeskin <skin id>", new String[]{"cs", "skinchange"});
        this.skinChanger = skinChanger;

        this.commandParameters.clear();

        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("skin", false, skinChanger.getAvailableSkins())
        });
        this.setPermission("skinchanger.use");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }
        if (args.length != 1 || Strings.isNullOrEmpty(args[0].trim())) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Not a player");
            return true;
        }

        try {
            skinChanger.changeSkin((Player) sender, args[0]);
        } catch (SkinChangeException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }
}
