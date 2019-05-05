package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Chomusuke extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Chomusuke.class.getSimpleName());

    public Chomusuke()
    {
        super(ID, 0, CardType.SKILL, CardTarget.NONE);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        this.baseSecondaryValue = this.secondaryValue = PlayerStatistics.getCardsExhaustedThisTurn();
        this.isSecondaryValueModified = this.secondaryValue != 0;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (c.cardID.equals(cardID))
            {
                return false;
            }
        }

        return super.cardPlayable(m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        int exhausted = PlayerStatistics.getCardsExhaustedThisTurn();
        for (int i = 0; i < exhausted; i++)
        {
            GameActionsHelper.GainEnergy(1);
            GameActionsHelper.DrawCard(p, this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}