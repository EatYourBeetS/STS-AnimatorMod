package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.FtueTip;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

public class LivingPicture extends AnimatorRelic
{
    public static final String ID = CreateFullID(LivingPicture.class.getSimpleName());

    private static Boolean hasShownTip1 = null;
    private static Boolean hasShownTip2 = null;

    private Boolean active = true;

    public LivingPicture()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        if (hasShownTip1 == null && AbstractDungeon.actNum == 1 && AbstractDungeon.getCurrMapNode().y == 0)
        {
            hasShownTip1 = AnimatorMetrics.GetConfig().getBool("LivingPictureTipShown");

            if (!hasShownTip1)
            {
                AbstractDungeon.ftue = new FtueTip("The Animator",
                        Resources_Animator_Strings.Tips.TEXT[0],
                        Settings.WIDTH / 2f, Settings.HEIGHT / 2f, FtueTip.TipType.CARD_REWARD);

                AnimatorMetrics.GetConfig().setBool("LivingPictureTipShown", true);
                AnimatorMetrics.SaveConfig();
                hasShownTip1 = true;
            }
        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        active = true;
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
        if (active && card != null && card.HasActiveSynergy())
        {
            GameActionsHelper.DrawCard(AbstractDungeon.player, 1);
            active = false;
            this.flash();
        }
    }
}