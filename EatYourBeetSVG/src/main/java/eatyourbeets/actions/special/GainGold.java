package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameEffects;

public class GainGold extends EYBAction
{
    protected boolean showCoins;

    public GainGold(int amount, boolean showCoins)
    {
        super(ActionType.SPECIAL);

        this.showCoins = showCoins;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (amount > 0)
        {
            CardCrawlGame.sound.play("GOLD_JINGLE");

            if (showCoins)
            {
                for (int i = 0; i < amount; ++i)
                {
                    GameEffects.Queue.Add(new GainPennyEffect(player.hb.cX, player.hb.cY + (player.hb.height / 2)));
                }
            }

            player.gainGold(amount);
        }

        Complete();
    }
}
