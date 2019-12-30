package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class HolyGrailRelic extends AnimatorRelic
{
    public static final String ID = CreateFullID(HolyGrailRelic.class.getSimpleName());
    public static final int GOLD_AMOUNT = 100;

    public HolyGrailRelic()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(GOLD_AMOUNT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        GameEffects.List.Add(new RainingGoldEffect(GOLD_AMOUNT));
        AbstractDungeon.player.gainGold(GOLD_AMOUNT);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        GameActions.Bottom.MakeCardInHand(new HolyGrail());
        this.flash();
    }
}