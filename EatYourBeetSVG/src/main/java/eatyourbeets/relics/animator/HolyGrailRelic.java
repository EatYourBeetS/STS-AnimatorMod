package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class HolyGrailRelic extends AnimatorRelic
{
    public static final String ID = CreateFullID(HolyGrailRelic.class);
    public static final int MAX_HP_ON_PICKUP = 8;

    public HolyGrailRelic()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(MAX_HP_ON_PICKUP);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        AbstractDungeon.player.increaseMaxHp(MAX_HP_ON_PICKUP, true);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        GameActions.Bottom.MakeCardInHand(new HolyGrail());
        AnimatorCard_UltraRare.MarkAsSeen(HolyGrail.DATA.ID);

        this.flash();
    }
}