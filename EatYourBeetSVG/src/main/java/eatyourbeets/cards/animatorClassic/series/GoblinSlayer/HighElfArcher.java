package eatyourbeets.cards.animatorClassic.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.affinity.animatorClassic.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HighElfArcher extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(HighElfArcher.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.UNCOMMON, EYBAttackType.Ranged);

    public HighElfArcher()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(1, 0, 1);
        SetScaling(0, 1, 0);


    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainAgility(1);
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ModifyAllInstances(uuid)
            .AddCallback(c ->
            {
                if (!c.hasTag(HASTE))
                {
                    c.tags.add(HASTE);
                }
            });
        }
    }
}