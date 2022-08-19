package eatyourbeets.cards.animatorClassic.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cocytus extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Cocytus.class).SetAttack(1, CardRarity.COMMON);

    public Cocytus()
    {
        super(DATA);

        Initialize(6, 0, 2, 1);
        SetUpgrade(1, 0, 1, 0);
        SetScaling(0, 0, 2);

        SetSeries(CardSeries.Overlord);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.GainForce(1, true);
        }

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainPlatedArmor(secondaryValue);
            GameActions.Bottom.GainThorns(secondaryValue);
        }
    }
}