package eatyourbeets.relics.animator;

import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class LivingPicture extends AnimatorRelic implements OnSynergySubscriber
{
    public static final String ID = CreateFullID(LivingPicture.class.getSimpleName());

    private Boolean hasShownTip1 = null;

    public LivingPicture()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

//        if (hasShownTip1 == null && AbstractDungeon.actNum == 1 && AbstractDungeon.getCurrMapNode().y == 0)
//        {
//            Readme.SpawnAll();
//            hasShownTip1 = true;
//        }
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        PlayerStatistics.onSynergy.SubscribeOnce(this);
    }

    @Override
    public void OnSynergy(AnimatorCard card)
    {
        GameActions.Bottom.Draw(1);
        this.flash();
    }
}