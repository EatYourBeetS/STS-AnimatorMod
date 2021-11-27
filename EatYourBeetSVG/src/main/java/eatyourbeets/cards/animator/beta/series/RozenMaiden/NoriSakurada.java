package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class NoriSakurada extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final EYBCardData DATA =
            Register(NoriSakurada.class)
                    .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public NoriSakurada() {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(Affinity.Light, 8);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.FetchFromPile(name, magicNumber, player.discardPile).SetOptions(true,true).AddCallback(cards -> {
            if (CheckAffinity(Affinity.Light)) {
                for (AbstractCard c : cards) {
                    GameUtilities.Retain(c);
                }
            }
        });
        GameActions.Bottom.DiscardFromHand(name, magicNumber, false)
                .SetOptions(false, false, false);

        CombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        GameActions.Top.Reload(NoriSakurada.DATA.Strings.NAME, cards -> {
            if (cards.size() > 0) {
                GameActions.Top.ReducePower(player, player, DelayedDamagePower.POWER_ID, cards.size());
            }
        });
    }
}
