package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class NoriSakurada extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final EYBCardData DATA =
            Register(NoriSakurada.class)
                    .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public NoriSakurada() {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.Draw(magicNumber);

        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        GameActions.Bottom.MoveCard(card, player.hand, player.drawPile)
                                .SetDestination(CardSelection.Top);
                    }
                });

        CombatStats.onEndOfTurn.SubscribeOnce(this);
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        GameActions.Bottom.Reload(NoriSakurada.DATA.Strings.NAME, cards -> {
            if (cards.size() > 0) {
                GameActions.Bottom.ReducePower(player, player, DelayedDamagePower.POWER_ID, cards.size());
            }
        });
    }
}
