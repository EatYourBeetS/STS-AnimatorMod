package eatyourbeets.cards.animator.series.OwariNoSeraph;

import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Yuuichirou_Asuramaru;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Yuuichirou_Asuramaru(), true));

    public Yuuichirou()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(0, 0, 2);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Yuuichirou_Asuramaru()).SetUpgrade(upgraded, false);
            GameActions.Bottom.SFX(SFX.ATTACK_MAGIC_FAST_2, 0.6f, 0.65f, 0.95f);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.Draw(1);
    }
}