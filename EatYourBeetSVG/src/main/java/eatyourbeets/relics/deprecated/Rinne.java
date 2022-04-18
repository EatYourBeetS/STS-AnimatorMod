package eatyourbeets.relics.deprecated;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class Rinne extends AnimatorRelic
{
    public static final String ID = CreateFullID(Rinne.class);

    protected transient static final HigakiRinne RINNE_ITSELF = new HigakiRinne();
    protected transient static final int RINNE_DOES = 3 + 1 + 1;
    protected transient static final int RINNE_SAYS = 33 + 27 + 9 + RINNE_DOES;

    public Rinne()
    {
        super(ID, RelicTier.DEPRECATED, LandingSound.MAGICAL);
    }

    @Override
    public int getPrice()
    {
        return 500;
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(0);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetCounter(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetCounter(0);

        if (player.currentHealth == 1)
        {
            player.heal(rng.random(5, 20));
            flash();
        }
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
        if (AddCounter(1 + (value % 7)) % 21 == RINNE_DOES)
        {
            GameActions.Bottom.Add(new HigakiRinneAction(RINNE_ITSELF, 1));
        }
    }
}