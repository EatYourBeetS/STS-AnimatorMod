package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.colorless.uncommon.Magilou;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Magilou_Bienfu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Magilou_Bienfu.class)
            .SetSkill(-2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(Magilou.DATA.Series);

    public Magilou_Bienfu()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Blue(1);

        SetLoyal(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        GameActions.Bottom.GainWisdom(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();
        GameActions.Bottom.TriggerOrbPassive(1, true, false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return false;
    }
}