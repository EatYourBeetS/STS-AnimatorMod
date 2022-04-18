package eatyourbeets.cards_beta.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class IsuzuTonan extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IsuzuTonan.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();

    public IsuzuTonan()
    {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new IsuzuTonanPower(p, magicNumber));
    }

    public static class IsuzuTonanPower extends AnimatorPower
    {
        public IsuzuTonanPower(AbstractPlayer owner, int amount)
        {
            super(owner, IsuzuTonan.DATA);

            this.amount = amount;
            this.isTurnBased = true;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.ReducePower(this, 1);
        }
    }
}