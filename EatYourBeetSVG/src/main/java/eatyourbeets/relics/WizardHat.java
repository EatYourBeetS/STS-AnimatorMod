package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class WizardHat extends AnimatorRelic
{
    public static final String ID = CreateFullID(WizardHat.class.getSimpleName());

    public WizardHat()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    private static final int TURNS = 7;
    private static final int DAMAGE_AMOUNT = 14;

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + TURNS + DESCRIPTIONS[1] + DAMAGE_AMOUNT + DESCRIPTIONS[2];
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        counter = 0;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = -1;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
        {
            counter += 1;
            if (counter >= TURNS)
            {
                this.flash();
                GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, DamageInfo.createDamageMatrix(DAMAGE_AMOUNT, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);
                counter = 0;
            }
        }
    }
}