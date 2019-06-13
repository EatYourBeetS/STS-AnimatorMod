package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.animator.RinTohsaka;

public class RinTohsakaAction extends AbstractGameAction
{
    private final int block;
    private final RinTohsaka rin;

    public RinTohsakaAction( int block, RinTohsaka rin)
    {
        this.rin = rin;
        this.duration = Settings.ACTION_DUR_FAST;
        this.block = block;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            int totalBlock = 0;
            AbstractPlayer player = AbstractDungeon.player;
            for (int i = 0; i < player.orbs.size(); ++i)
            {
                if (!(player.orbs.get(i) instanceof EmptyOrbSlot))
                {
                    totalBlock += this.block;
                }
            }
            if (totalBlock > 0)
            {
                rin.baseBlock = totalBlock;
                rin.applyPowers();
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(player, player, rin.block));
            }
        }


        this.tickDuration();
    }
}
