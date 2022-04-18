package eatyourbeets.cards.unnamed.basic;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;

public class Defend extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Defend.class)
            .SetSkill(1, CardRarity.BASIC, EYBCardTarget.None);

    public Defend()
    {
        super(DATA);

        Initialize(0, 3, 2);
        SetUpgrade(0, 2, 1);

        this.cropPortrait = false;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.RecoverHP(magicNumber);
    }
}