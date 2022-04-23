package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Guren extends AnimatorCard implements OnPhaseChangedSubscriber
{
    public static final EYBCardData DATA = Register(Guren.class)
            .SetSkill(3, CardRarity.RARE)
            .SetSeries(CardSeries.OwariNoSeraph);

    public Guren()
    {
        super(DATA);

        Initialize(0, 0,2);
        SetUpgrade(0, 0,1);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.Add(new GurenAction(m));
        }

        CombatStats.onPhaseChanged.Subscribe(this);
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        if (phase == GameActionManager.Phase.WAITING_ON_USER)
        {
            if (CombatStats.TryActivateSemiLimited(this.cardID))
            {
                int amount = player.exhaustPile.size();
                if (amount > 0)
                {
                    GameActions.Bottom.StackPower(new SupportDamagePower(player, amount));
                }
            }

            CombatStats.onPhaseChanged.Unsubscribe(this);
        }
    }
}