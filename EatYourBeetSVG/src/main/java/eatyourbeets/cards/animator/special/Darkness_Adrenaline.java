package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import eatyourbeets.cards.animator.series.Konosuba.Darkness;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Darkness_Adrenaline extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Darkness_Adrenaline.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(Darkness.DATA.Series);

    public Darkness_Adrenaline()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Air(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new AdrenalineEffect(), 0.15f);
        GameActions.Bottom.GainEnergy(secondaryValue);
        GameActions.Bottom.Draw(magicNumber);
    }
}