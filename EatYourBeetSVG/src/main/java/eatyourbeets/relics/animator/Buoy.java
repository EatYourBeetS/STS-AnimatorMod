package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnAfterCardDrawnSubscriber;

public class Buoy extends AnimatorRelic implements OnAfterCardDrawnSubscriber
{
    public static final String ID = CreateFullID(Buoy.class.getSimpleName());

    private static final int BLOCK_AMOUNT = 3;
    private static final int DEXTERITY_AMOUNT = 1;

    private int startingCardCount = 0;
    private boolean enabled = false;

    public Buoy()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (PlayerStatistics.InBattle())
        {
            atBattleStart();
            atTurnStartPostDraw();
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        PlayerStatistics.onAfterCardDrawn.Subscribe(this);

        this.counter = 0;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = 0;
    }

    @Override
    public void OnAfterCardDrawn(AbstractCard card)
    {
        this.counter = (PlayerStatistics.getCardsDrawnThisTurn() - startingCardCount);

        if (enabled && counter >= 3)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.GainBlock(p, BLOCK_AMOUNT);
            GameActionsHelper.ApplyPowerSilently(p, p, new LoseDexterityPower(p, DEXTERITY_AMOUNT), DEXTERITY_AMOUNT);
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, DEXTERITY_AMOUNT), DEXTERITY_AMOUNT);

            enabled = false;
            this.flash();
        }
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        counter = 0;
        startingCardCount = AbstractDungeon.player.masterHandSize;
        enabled = true;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + BLOCK_AMOUNT + DESCRIPTIONS[1] + DEXTERITY_AMOUNT + DESCRIPTIONS[2];
    }
}