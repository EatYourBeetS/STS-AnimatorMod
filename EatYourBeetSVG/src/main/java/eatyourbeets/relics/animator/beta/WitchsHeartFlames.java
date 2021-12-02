package eatyourbeets.relics.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonTriggerablePower;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class WitchsHeartFlames extends AnimatorRelic implements OnCardCreatedSubscriber
{
    public static final String ID = CreateFullID(WitchsHeartFlames.class);
    public static final int BURNING_ATTACK_BONUS = 15;

    public WitchsHeartFlames()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        CombatStats.onCardCreated.Subscribe(this);
        GameActions.Bottom.Callback(() -> CommonTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, BURNING_ATTACK_BONUS));
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        for (AbstractCard c : player.masterDeck.group) {
            if (Burn.ID.equals(c.cardID) || Status_Burn.DATA.ID.equals(c.cardID)) {
                player.masterDeck.group.remove(c);
                GameEffects.TopLevelQueue.ShowAndObtain(new SearingBurn(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f, false);
            }
        }
    }

    @Override
    public void OnCardCreated(AbstractCard card, boolean startOfBattle)
    {
        if (Burn.ID.equals(card.cardID) || Status_Burn.DATA.ID.equals(card.cardID))
        {
            GameActions.Last.ReplaceCard(card.uuid, new SearingBurn());
            flash();
        }
    }
}