package pinacolada.cards.pcl.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.DelayedDamagePower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class NoriSakurada extends PCLCard implements OnEndOfTurnSubscriber
{
    public static final PCLCardData DATA =
            Register(NoriSakurada.class)
                    .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetSeriesFromClassPackage();

    public NoriSakurada() {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Light, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.FetchFromPile(name, magicNumber, player.discardPile).SetOptions(true,true).AddCallback(cards -> {
            if (CheckAffinity(PCLAffinity.Light)) {
                for (AbstractCard c : cards) {
                    PCLGameUtilities.Retain(c);
                }
            }
        });
        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .SetOptions(false, false, false);

        PCLCombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        PCLActions.Top.Reload(NoriSakurada.DATA.Strings.NAME, cards -> {
            if (cards.size() > 0) {
                PCLActions.Top.ReducePower(player, player, DelayedDamagePower.POWER_ID, cards.size() * secondaryValue);
            }
        });
    }
}
