package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class VividPicture extends AnimatorRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(VividPicture.class);

    public VividPicture()
    {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PlayerStatistics.onSynergy.SubscribeOnce(this);
        SetEnabled(true);
    }

    @Override
    public void OnSynergy(AnimatorCard card)
    {
        GameActions.Bottom.Draw(1);
        GameActions.Bottom.GainEnergy(1);
        SetEnabled(false);
        this.flash();
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
    public boolean canSpawn()
    {
        return AbstractDungeon.player.hasRelic(LivingPicture.ID);
    }
}