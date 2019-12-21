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
import eatyourbeets.utilities.GameEffects;

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
                for (int i = secondaryValue-1; i >= 0; i--)
                {
                    AbstractCard card = player.drawPile.getNCardFromTop(i);
                    float target_x = Settings.WIDTH * (0.35f + (i * 0.05f));
                    float target_y = Settings.HEIGHT * (0.45f + (i * 0.05f));

                    //GameEffects.Queue.ShowCardBriefly(card.makeStatEquivalentCopy(), target_x, target_y);

                    GameActions.Top.Discard(card, player.drawPile)
                    .SetCardPosition(target_x, target_y);
                    GameActions.Top.WaitRealtime(0.15f);
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