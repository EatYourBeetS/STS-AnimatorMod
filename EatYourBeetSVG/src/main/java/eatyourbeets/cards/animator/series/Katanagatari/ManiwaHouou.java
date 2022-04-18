package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class ManiwaHouou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ManiwaHouou.class)
            .SetPower(1, CardRarity.RARE)
            .SetSeries(CardSeries.Katanagatari);

    public ManiwaHouou()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetCostUpgrade(-1);

        SetAffinity_Green(1);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new ManiwaHououPower(p, magicNumber));
    }

    public static class ManiwaHououPower extends AnimatorPower
    {
        public ManiwaHououPower(AbstractCreature owner, int amount)
        {
            super(owner, ManiwaHouou.DATA);

            Initialize(amount);
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            GameActions.Bottom.GainStrength(difference).IgnoreArtifact(true);
            GameActions.Bottom.GainDexterity(difference).IgnoreArtifact(true);

            super.onAmountChanged(previousAmount, difference);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            if (info.owner != null && info.owner != this.owner && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0)
            {
                GameActions.Bottom.Callback(() ->
                {
                    if (amount > 0)
                    {
                        GameActions.Bottom.ApplyPoison(owner, owner, amount);
                        reducePower(1);

                        if (amount <= 0)
                        {
                            RemovePower();
                        }
                    }
                });
                flash();
            }
        }
    }
}