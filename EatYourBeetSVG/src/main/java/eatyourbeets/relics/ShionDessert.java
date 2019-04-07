package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.powers.PlayerStatistics;

public class ShionDessert extends AnimatorRelic
{
    public static final String ID = CreateFullID(ShionDessert.class.getSimpleName());

    private static final int ATTACKS_COUNTER = 3;
    private static final int POISON_AMOUNT = 3;

    public ShionDessert()
    {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + ATTACKS_COUNTER + DESCRIPTIONS[1] + POISON_AMOUNT + DESCRIPTIONS[2];
    }

    public void atTurnStart()
    {
        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if (card.type == AbstractCard.CardType.ATTACK)
        {
            this.counter += 1;
            if (this.counter % ATTACKS_COUNTER == 0)
            {
                this.counter = 0;
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));

                AbstractPlayer p = AbstractDungeon.player;
                AbstractMonster enemy = Utilities.GetRandomElement(PlayerStatistics.GetCurrentEnemies(true));
                if (enemy != null)
                {
                    GameActionsHelper.ApplyPower(p, enemy, new PoisonPower(enemy, p, POISON_AMOUNT), POISON_AMOUNT);
                }
            }
        }
    }

    public void onVictory()
    {
        this.counter = -1;
    }
}