package eatyourbeets.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;

public class LivingPicture extends AnimatorRelic
{
    public static final String ID = CreateFullID(LivingPicture.class.getSimpleName());

    private Boolean active = true;

    public LivingPicture()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
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