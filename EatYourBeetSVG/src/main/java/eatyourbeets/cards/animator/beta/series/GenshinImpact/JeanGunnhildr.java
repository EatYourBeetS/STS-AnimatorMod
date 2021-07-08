package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class JeanGunnhildr extends AnimatorCard
{
    public static final EYBCardData DATA = Register(JeanGunnhildr.class).SetAttack(1, CardRarity.RARE);

    public JeanGunnhildr()
    {
        super(DATA);

        Initialize(8, 1, 2);
        SetUpgrade(3, 1, 0);
        SetScaling(0, 1, 1);

        SetLoyal(true);
        SetSynergy(Synergies.GenshinImpact);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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
                            if (WeakPower.POWER_ID.equals(power.ID) || VulnerablePower.POWER_ID.equals(power.ID) || FrailPower.POWER_ID.equals(power.ID))
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