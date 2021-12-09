package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class HousakiTohya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiTohya.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new HousakiMinori(), false));

    public HousakiTohya()
    {
        super(DATA);

        Initialize(4, 0, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);
        if (IsStarter()) {
            GameActions.Bottom.ApplyWeak(player, m, magicNumber);
            GameActions.Bottom.ApplyVulnerable(player, m, magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(1)
                .SetFilter(c -> HousakiMinori.DATA.ID.equals(c.cardID), false)
                .AddCallback((cards) ->
                {
                    if (cards.size() > 0) {
                        GameActions.Bottom.GainEnergyNextTurn(1);
                    }
                    else {
                        GameActions.Bottom.GainEnergy(1);
                    }
                });
    }
}