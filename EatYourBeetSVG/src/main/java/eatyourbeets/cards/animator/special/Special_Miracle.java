package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Special_Miracle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Special_Miracle.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS);

    public Special_Miracle()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Light(1);

        SetPurge(true);
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlessing(1, true);
        GameActions.Bottom.GainEnergy(magicNumber);
    }
}