package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.animator.HigakiRinne;

public class Rinne extends AnimatorRelic
{
    private static final int RINNE_DOES = 3 + 1 + 1;
    private static final int RINNE_SAYS = 33 + 27 + 9 + RINNE_DOES;

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

        DoSomething(c.damage + c.block);
    }

    @Override
    public int onPlayerGainBlock(int blockAmount)
    {
        DoSomething(blockAmount);

        return super.onPlayerGainBlock(blockAmount);
    }

    private void DoSomething(int value)
    {
        counter += 1 + (value % 4);
        if (counter == RINNE_SAYS)
        {
            for (int i = 0; i < RINNE_DOES; i++)
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new HigakiRinne()));
            }
        }
    }
}