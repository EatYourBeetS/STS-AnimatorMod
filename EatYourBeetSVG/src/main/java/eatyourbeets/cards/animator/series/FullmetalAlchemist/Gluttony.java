package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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
            for (int i = magicNumber-1; i >= 0; i--)
            {
                AbstractCard card = p.drawPile.getNCardFromTop(i);
                float target_x = Settings.WIDTH * (0.3f + (i * 0.02f));
                float target_y = Settings.HEIGHT * (0.4f + (i * 0.02f));

                GameActions.Top.Exhaust(card, p.drawPile)
                .SetCardPosition(target_x, target_y);
                GameActions.Top.WaitRealtime(0.2f);
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