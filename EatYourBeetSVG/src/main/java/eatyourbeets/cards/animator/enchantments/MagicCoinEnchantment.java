package eatyourbeets.cards.animator.enchantments;

import eatyourbeets.cards.base.EYBCardData;

public class MagicCoinEnchantment extends Enchantment {
    public static final EYBCardData DATA = RegisterInternal(MagicCoinEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 3;

    public MagicCoinEnchantment() {
        super(DATA, LEVEL, INDEX);

        Initialize(0, 0);
    }

    @Override
    public void OnObtain() {
        player.gainGold(250);
    }

    @Override
    public void OnStartOfBattle() {
    }
}