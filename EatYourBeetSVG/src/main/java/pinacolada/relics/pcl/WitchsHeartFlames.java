package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.interfaces.subscribers.OnCardCreatedSubscriber;
import pinacolada.cards.pcl.curse.Curse_SearingBurn;
import pinacolada.cards.pcl.status.Status_Burn;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLTriggerablePower;
import pinacolada.powers.common.BurningPower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class WitchsHeartFlames extends PCLRelic implements OnCardCreatedSubscriber
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

        PCLCombatStats.onCardCreated.Subscribe(this);
        PCLActions.Bottom.Callback(() -> PCLTriggerablePower.AddEffectBonus(BurningPower.POWER_ID, BURNING_ATTACK_BONUS));
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        for (AbstractCard c : player.masterDeck.group) {
            if (Burn.ID.equals(c.cardID) || Status_Burn.DATA.ID.equals(c.cardID)) {
                player.masterDeck.group.remove(c);
                PCLGameEffects.TopLevelQueue.ShowAndObtain(new Curse_SearingBurn(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f, false);
            }
        }
    }

    @Override
    public void OnCardCreated(AbstractCard card, boolean startOfBattle)
    {
        if (Burn.ID.equals(card.cardID) || Status_Burn.DATA.ID.equals(card.cardID))
        {
            PCLActions.Last.ReplaceCard(card.uuid, new Curse_SearingBurn());
            flash();
        }
    }
}