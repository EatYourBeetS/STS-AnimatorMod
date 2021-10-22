package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.QuestionCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameEffects;

public class QuestionMarkEnchantment extends Enchantment
{
    public static final EYBCardData DATA = RegisterInternal(QuestionMarkEnchantment.class);
    public static final int LEVEL = 1;
    public static final int INDEX = 6;

    public QuestionMarkEnchantment()
    {
        super(DATA, LEVEL, INDEX, new QuestionCard());

        Initialize(0, 0);
    }

    @Override
    public void OnObtain()
    {
        GameEffects.TopLevelList.ObtainRelic(new QuestionCard());
    }

    @Override
    public void OnStartOfBattle()
    {

    }
}