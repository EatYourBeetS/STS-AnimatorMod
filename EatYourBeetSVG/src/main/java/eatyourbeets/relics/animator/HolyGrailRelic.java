package eatyourbeets.relics.animator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.cards.animator.ultrarare.HolyGrail;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class HolyGrailRelic extends AnimatorRelic
{
    private static HolyGrail grail;

    public static final String ID = CreateFullID(HolyGrailRelic.class);
    public static final int MAX_HP_ON_PICKUP = 8;

    public HolyGrailRelic()
    {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, MAX_HP_ON_PICKUP);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.increaseMaxHp(MAX_HP_ON_PICKUP, true);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        super.atBattleStartPreDraw();

        GameActions.Bottom.MakeCardInHand(new HolyGrail());
        AnimatorCard_UltraRare.MarkAsSeen(HolyGrail.DATA.ID);
        flash();
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        super.renderTip(sb);

        if (grail == null)
        {
            grail = new HolyGrail();
        }

        grail.drawScale = grail.targetDrawScale = 0.8f;
        grail.current_x = grail.target_x = InputHelper.mX + (((InputHelper.mX > (Settings.WIDTH * 0.5f)) ? -1.505f : 1.505f) * EYBCardTooltip.BOX_W);
        grail.current_y = grail.target_y = InputHelper.mY - (AbstractCard.IMG_HEIGHT * 0.5f);
        GR.UI.AddPostRender(grail::render);
    }
}