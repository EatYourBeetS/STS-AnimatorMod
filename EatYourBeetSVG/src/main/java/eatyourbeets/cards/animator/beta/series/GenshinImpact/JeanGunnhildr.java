package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SwirledPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class JeanGunnhildr extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JeanGunnhildr.class).SetAttack(1, CardRarity.RARE).SetSeriesFromClassPackage();

    public JeanGunnhildr()
    {
        super(DATA);

        Initialize(8, 1, 2, 1);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(2, 0, 1);

        SetLoyal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            for (AbstractMonster m : GameUtilities.GetEnemies(true)) {
                GameActions.Bottom.StackPower(player, new SwirledPower(m, 1));
                break;
            }
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .AddCallback(cards ->
                {
                    int discarded = cards.size();
                    if (discarded > 0)
                    {
                        for (AbstractPower power : player.powers)
                        {
                            if (GameUtilities.IsCommonDebuff(power))
                            {
                                int decrease = Math.min(discarded, power.amount);
                                GameActions.Top.ReducePower(power, decrease);
                                discarded -= decrease;
                                if (discarded <= 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                });
    }
}