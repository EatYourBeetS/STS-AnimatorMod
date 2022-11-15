package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Yuuichirou_Asuramaru;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Yuuichirou.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.UNCOMMON);
    static
    {
        DATA.AddPreview(new Yuuichirou_Asuramaru(), true);
    }

    public Yuuichirou()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);
        SetScaling(0, 1, 1);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
    }
}