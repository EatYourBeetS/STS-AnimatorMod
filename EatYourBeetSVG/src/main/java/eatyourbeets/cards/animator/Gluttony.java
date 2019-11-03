package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.common.ExhaustFromPileAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.VariableExhaustAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Gluttony extends AnimatorCard
{
    public static final String ID = Register(Gluttony.class.getSimpleName(), EYBCardBadge.Special);

    public Gluttony()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 4, 4);

        SetHealing(true);
        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);

        AbstractPlayer p = AbstractDungeon.player;
        if (playable)
        {
            int total = p.drawPile.size() + p.discardPile.size() + p.hand.size();
            if (total < 20)
            {
                cantUseMessage = cardData.strings.EXTENDED_DESCRIPTION[0];

                return false;
            }
        }

        return playable && (p.drawPile.size() >= magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (p.drawPile.size() >= magicNumber)
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActionsHelper.AddToBottom(new ExhaustSpecificCardAction(p.drawPile.getNCardFromTop(i), p.drawPile, true));
            }

            GameActionsHelper.AddToBottom(new HealAction(p, p, secondaryValue));
            GameActionsHelper.GainForce(secondaryValue);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }

    private void OnExhaust(Object state, ArrayList<AbstractCard> cards)
    {
        if (state != this || cards == null || cards.size() == 0)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard c : cards)
        {
            switch (c.type)
            {
                case ATTACK:
                    GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
                    break;

                case SKILL:
                    GameActionsHelper.AddToBottom(new AddTemporaryHPAction(p, p, 6));
                    break;

                case POWER:
                    GameActionsHelper.AddToBottom(new HealAction(p, p, 7));
                    break;

                case CURSE:
                    GameActionsHelper.GainBlock(p, 6);
                    break;

                case STATUS:
                    GameActionsHelper.GainEnergy(1);
                    break;
            }
        }
    }
}