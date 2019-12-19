package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Guy extends AnimatorCard
{
    public static final String ID = Register(Guy.class.getSimpleName(), EYBCardBadge.Synergy);

    public Guy()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,0, 1, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.Draw(this.magicNumber);
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, false, true);

        if (HasActiveSynergy())
        {
            GameActions.Bottom.Callback(__ ->
            {
                AbstractPlayer player = AbstractDungeon.player;
                int max = Math.min(player.drawPile.size(), secondaryValue);
                for (int i = 0; i < max; i++)
                {
                    AbstractCard card = player.drawPile.getNCardFromTop(i);
                    GameActions.Top.WaitRealtime(0.25f);
                    GameActions.Top.MoveCard(card,  player.discardPile, player.drawPile,true);
                }
            });
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