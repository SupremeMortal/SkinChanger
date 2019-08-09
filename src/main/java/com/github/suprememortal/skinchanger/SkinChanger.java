package com.github.suprememortal.skinchanger;

import cn.nukkit.Player;

public interface SkinChanger {

    void changeSkin(Player player, String skinName) throws SkinChangeException;

    boolean resetSkin(Player player);

    String[] getAvailableSkins();
}
