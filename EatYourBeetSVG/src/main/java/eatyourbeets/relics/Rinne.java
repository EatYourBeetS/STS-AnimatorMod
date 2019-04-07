package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.animator.HigakiRinne;

public class Rinne extends AnimatorRelic
{
    public static final String ID = CreateFullID(Rinne.class.getSimpleName());

    public Rinne()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        counter = 1;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        counter = -1;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        counter += 1 + ((c.damage + c.block) % 4);
        if (counter == 74)
        {
            for (int i = 0; i < 10; i++)
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new HigakiRinne()));
            }
        }
    }

    @Override
    public int onPlayerGainBlock(int blockAmount)
    {
        counter += 1 + (blockAmount % 4);
        if (counter == 74)
        {
            for (int i = 0; i < 10; i++)
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new HigakiRinne()));
            }
        }

        return super.onPlayerGainBlock(blockAmount);
    }
}