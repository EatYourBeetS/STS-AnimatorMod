package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import eatyourbeets.actions.common.DrawSpecificCardAction;
import eatyourbeets.actions.common.ModifyCostForTurnAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.TemporaryEnvenomPower;
import eatyourbeets.utilities.GameActionsHelper;

public class TakanashiRikka extends AnimatorCard implements Hidden
{
    public static final String ID = CreateFullID(TakanashiRikka.class.getSimpleName());

    public TakanashiRikka()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 1);

        this.exhaust = true;

        SetSynergy(Synergies.Chuunibyou);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            this.retain = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        boolean drawn = false;

        for (AbstractCard c : p.hand.getAttacks().group)
        {
            c.setCostForTurn(0);
        }

        for (AbstractCard c : p.drawPile.getAttacks().group)
        {
            c.setCostForTurn(0);

            if (upgraded && !drawn)
            {
                GameActionsHelper.AddToBottom(new DrawSpecificCardAction(c));
                drawn = true;
            }
        }

        for (AbstractCard c : p.discardPile.getAttacks().group)
        {
            c.setCostForTurn(0);
        }

        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (!m1.hasPower(IntangiblePower.POWER_ID) && !m1.hasPower(IntangiblePlayerPower.POWER_ID))
            {
                GameActionsHelper.ApplyPower(p, m1, new IntangiblePlayerPower(m1, 1), 1);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.retain = true;
        }
    }
}