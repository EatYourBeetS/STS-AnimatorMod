package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class CrumblingOrb extends AnimatorRelic
{
    public static final String ID = CreateFullID(CrumblingOrb.class);
    public static final int CHOOSE_AMOUNT = 2;
    public static final int CHOOSE_SIZE = 4;

    private Random battleRNG;

    public CrumblingOrb()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, CHOOSE_AMOUNT, CHOOSE_SIZE);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        player.energy.energyMaster -= 1;
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        battleRNG = new Random(rng.randomLong(), 0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        battleRNG = null;
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (battleRNG == null)
        {
            JUtils.LogError(this, "atTurnStart called before atBattleStartPreDraw");
            battleRNG = new Random(rng.randomLong(), 0);
        }

        final AbstractCard card = GameUtilities.GetCardsInCombatWeighted(null).Retrieve(battleRNG).makeCopy();
        if (card.canUpgrade() && battleRNG.randomBoolean(0.3f))
        {
            card.upgrade();
        }

        player.drawPile.addToTop(card);
        CombatStats.OnCardCreated(card, false);
        flash();
    }
}