package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Gluttony extends AnimatorCard implements OnCallbackSubscriber
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
        GameActionsHelper.DelayedAction(this);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }

    @Override
    public void OnCallback(Object state, AbstractGameAction action)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.drawPile.size() >= magicNumber)
        {
            for (int i = 0; i < magicNumber; i++)
            {
                AbstractCard card = p.drawPile.getNCardFromTop(i);
                card.target_x = Settings.WIDTH * (0.3f + (i * 0.02f));
                card.target_y = Settings.HEIGHT * (0.4f + (i * 0.02f));
                GameActionsHelper.AddToBottom(new MoveSpecificCardAction(card, p.exhaustPile, p.drawPile, true));
                GameActionsHelper.Wait(0.2f);
                //GameActionsHelper.AddToBottom(new ExhaustSpecificCardAction(p.drawPile.getNCardFromTop(i), p.drawPile, true));
            }

            GameActionsHelper.AddToBottom(new HealAction(p, p, secondaryValue));
            GameActionsHelper.GainForce(secondaryValue);
        }
    }
}