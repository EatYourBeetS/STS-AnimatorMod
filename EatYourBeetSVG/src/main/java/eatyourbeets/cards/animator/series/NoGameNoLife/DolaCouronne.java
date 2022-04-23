package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class DolaCouronne extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DolaCouronne.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public DolaCouronne()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 3);

        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Motivate();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new DolaCouronnePower(p, magicNumber));
    }

    public static class DolaCouronnePower extends AnimatorPower
    {
        public DolaCouronnePower(AbstractCreature owner, int amount)
        {
            super(owner, DolaCouronne.DATA);

            Initialize(amount);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (enabled && usedCard.type == CardType.ATTACK)
            {
                GameActions.Bottom.GainBlock(amount);
                SetEnabled(false);
                RemovePower();
                flash();
            }
        }
    }
}