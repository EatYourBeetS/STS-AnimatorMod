package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Garou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Garou.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Garou()
    {
        super(DATA);

        Initialize(0, 0, 6);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(2);
        SetAffinity_Green(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetUnplayable(player.drawPile.size() < 3);
        GameUtilities.ModifySecondaryValue(this, JUtils.Count(CombatStats.CardsExhaustedThisTurn(), GameUtilities::HasLightAffinity), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.drawPile.size() >= 3)
        {
            GameActions.Bottom.StackPower(new GarouPower(p, magicNumber));
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile, 3)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top);
            GameActions.Last.Callback(() ->
            {
                final int light = JUtils.Count(CombatStats.CardsExhaustedThisTurn(), GameUtilities::HasLightAffinity);
                if (light > 0)
                {
                    GameActions.Bottom.GainForce(light, upgraded);
                }
            });
        }
    }

    public static class GarouPower extends AnimatorPower
    {
        public GarouPower(AbstractCreature owner, int amount)
        {
            super(owner, Garou.DATA);

            Initialize(amount);
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            GameActions.Bottom.GainStrength(difference).IgnoreArtifact(true).ShowEffect(difference > 0, true);
            GameActions.Bottom.GainDexterity(difference).IgnoreArtifact(true).ShowEffect(difference > 0, true);

            super.onAmountChanged(previousAmount, difference);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            RemovePower(GameActions.Delayed);
        }
    }
}