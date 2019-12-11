package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.animator.series.Katanagatari.HigakiRinne;

public class Rinne extends AnimatorRelic// implements OnEquipUnnamedReignRelicSubscriber
{
    public static final int RINNE_DOES = 3 + 1 + 1;
    public static final int RINNE_SAYS = 33 + 27 + 9 + RINNE_DOES;

    public static final String ID = CreateFullID(Rinne.class.getSimpleName());

    public Rinne()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL);
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

        this.counter = 0;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = 0;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = 0;

        if (AbstractDungeon.player.currentHealth == 1)
        {
            this.flash();
            AbstractDungeon.player.heal(AbstractDungeon.cardRandomRng.random(5, 20));
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

    private static final HigakiRinne RINNE_ITSELF = new HigakiRinne();

    private void DoSomething(int value)
    {
        counter += 1 + (value % 7);

        if (counter % 21 == RINNE_DOES)
        {
            GameActions.Bottom.Add(new HigakiRinneAction(RINNE_ITSELF));
        }
    }
}