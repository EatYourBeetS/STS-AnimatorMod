package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Gluttony extends AnimatorCard
{
    public static final String ID = Register(Gluttony.class.getSimpleName(), EYBCardBadge.Special);

    public Gluttony()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 4, 16);

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
            if (total < secondaryValue)
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
                AbstractCard card = p.drawPile.getNCardFromTop(i);
                card.target_x = Settings.WIDTH * (0.3f + (i * 0.02f));
                card.target_y = Settings.HEIGHT * (0.4f + (i * 0.02f));
                GameActions.Top.Add(new WaitAction(0.15f));
                GameActions.Top.Add(new MoveCard(card, p.exhaustPile, p.drawPile, true));
            }

            GameActions.Bottom.Heal(magicNumber);
            GameActions.Bottom.GainForce(magicNumber);
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
}