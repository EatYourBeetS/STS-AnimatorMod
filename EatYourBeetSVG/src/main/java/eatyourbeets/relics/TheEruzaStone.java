package eatyourbeets.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class TheEruzaStone extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEruzaStone.class.getSimpleName());

    public TheEruzaStone()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (c.type == AbstractCard.CardType.POWER)
        {
            GameActionsHelper.DrawCard(AbstractDungeon.player, 1);
            this.flash();
        }
    }

    @Override
    public void OnManualEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 2;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        AbstractDungeon.player.energy.energyMaster -= 2;
    }
}