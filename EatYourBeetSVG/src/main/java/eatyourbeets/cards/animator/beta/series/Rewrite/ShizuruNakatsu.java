package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.Arrays;

public class ShizuruNakatsu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.None).SetSeriesFromClassPackage();

    private boolean canAttack;

    public ShizuruNakatsu()
    {
        super(DATA);

        Initialize(6, 3, 2);
        SetUpgrade(0, 3, 0);
        SetAffinity_Green(2, 0, 1);
        SetAffinity_Orange(0, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (CheckAttackCondition())
        {
            return super.GetDamageInfo();
        }

        return null;
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (!player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.DiscardFromHand(name, magicNumber, true)
                    .ShowEffect(true, true)
                    .SetFilter(c -> c.type == CardType.SKILL)
                    .SetOptions(false, false, false)
                    .AddCallback(() -> GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID));
        }

        if (CheckAttackCondition())
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.GUNSHOT);
        }
    }

    private int GetNumberOfSkills(CardGroup group)
    {
        int count = 0;

        for (AbstractCard card : group.group)
        {
            if (card.type == CardType.SKILL)
            {
                count++;
            }
        }

        return count;
    }

    private boolean CheckAttackCondition() {
        Affinity highestAffinity = JUtils.FindMax(Arrays.asList(Affinity.Basic()), this::GetHandAffinity);
        return (highestAffinity.equals(Affinity.Green));
    }
}