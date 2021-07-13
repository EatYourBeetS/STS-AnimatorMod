package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Tyuule extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Tyuule.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.ALL);

//    Not used anymore. Commented as an example on how to add custom tooltips
//
//    public static final EYBCardTooltip CommonDebuffs = new EYBCardTooltip(DATA.Strings.EXTENDED_DESCRIPTION[0], DATA.Strings.EXTENDED_DESCRIPTION[1]);
//
//    @Override
//    public void initializeDescription()
//    {
//        super.initializeDescription();
//
//        if (cardText != null)
//        {
//            tooltips.add(CommonDebuffs);
//        }
//    }

    public Tyuule()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 2);

        SetSeries(CardSeries.Gate);
        SetAffinity(0, 0, 1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ApplyPoison(TargetHelper.Enemies(), magicNumber).AddCallback(power ->
        {
            final AbstractCreature target = power.owner;
            for (AbstractPower debuff : target.powers)
            {
                if (WeakPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyWeak(player, target, 1);
                }
                else if (VulnerablePower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyVulnerable(player, target, 1);
                }
                else if (PoisonPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyPoison(player, target, 1);
                }
                else if (BurningPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ApplyBurning(player, target, 1);
                }
                else if (GainStrengthPower.POWER_ID.equals(debuff.ID))
                {
                    GameActions.Bottom.ReduceStrength(target, 1, true);
                }
            }
        });
    }
}