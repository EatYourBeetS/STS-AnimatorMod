package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class AliceMargatroid extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AliceMargatroid.class).SetPower(2, CardRarity.UNCOMMON);

    public AliceMargatroid()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetCostUpgrade(-1);
        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new AlicePower(p, magicNumber));
        if (HasSynergy()) {
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    public static class AlicePower extends AnimatorPower
    {

        public AlicePower(AbstractCreature owner, int amount)
        {
            super(owner, AliceMargatroid.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurn();
            GameActions.Bottom.Draw(amount);
            GameActions.Bottom.SelectFromHand(name, amount, false)
            .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, AbstractDungeon.player.drawPile).SetDestination(CardSelection.Top);
                }

                GameActions.Bottom.Add(new RefreshHandLayout());
            });
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

