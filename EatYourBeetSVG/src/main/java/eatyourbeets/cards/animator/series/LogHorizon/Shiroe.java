package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Shiroe extends AnimatorCard
{
    public static final int MINIMUM_TEAMWORK = 3;
    public static final EYBCardData DATA = Register(Shiroe.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Shiroe()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Cycle(name, magicNumber);
        GameActions.Bottom.StackPower(new ShiroePower(p, secondaryValue));
    }

    public static class ShiroePower extends AnimatorPower
    {
        public ShiroePower(AbstractPlayer owner, int amount)
        {
            super(owner, Shiroe.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card instanceof EYBCard && ((EYBCard) card).GetTeamwork(null) >= MINIMUM_TEAMWORK)
            {
                GameActions.Bottom.ApplyConstricted(TargetHelper.Enemies(), amount);
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            GameActions.Bottom.RemovePower(owner, owner, this);
        }
    }
}