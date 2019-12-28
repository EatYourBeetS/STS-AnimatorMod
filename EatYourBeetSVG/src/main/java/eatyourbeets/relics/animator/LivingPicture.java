package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class LivingPicture extends AnimatorRelic
{
    public static final String ID = CreateFullID(LivingPicture.class.getSimpleName());

    private Boolean hasShownTip1 = null;
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
            Readme.SpawnAll();
            hasShownTip1 = true;
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

        AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
        if (active && card != null && card.HasSynergy())
        {
            GameActions.Bottom.Draw(1);
            active = false;
            this.flash();
        }
    }
}