package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;

import java.util.ArrayList;

public class VividPicture extends AnimatorRelic
{
    public static final String ID = CreateFullID(VividPicture.class.getSimpleName());

    private Boolean active = true;

    public VividPicture()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        active = true;
    }

    @Override
    public void obtain()
    {
        ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            if (relics.get(i).relicId.equals(LivingPicture.ID))
            {
                instantObtain(AbstractDungeon.player, i, true);
                return;
            }
        }

        super.obtain();
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
        if (active && card != null && card.HasSynergy())
        {
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.GainEnergy(1);
            active = false;
            this.flash();
        }
    }

    public boolean canSpawn()
    {
        return AbstractDungeon.player.hasRelic(LivingPicture.ID);
    }
}