package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Guren extends AnimatorCard implements OnPhaseChangedSubscriber
{
    public static final EYBCardData DATA = Register(Guren.class)
            .SetSkill(3, CardRarity.RARE)
            .SetMaxCopies(2)
            .SetSeries(CardSeries.OwariNoSeraph);

    private boolean alreadyPlayed = false;

    public Guren()
    {
        super(DATA);

        Initialize(0, 1,3, 4);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Light, 5);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.Add(new GurenAction(m));
        }

        alreadyPlayed = true;
        CombatStats.onPhaseChanged.Subscribe(this);
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        if (phase == GameActionManager.Phase.WAITING_ON_USER)
        {
            if (TrySpendAffinity(Affinity.Light))
            {
                int amount = player.exhaustPile.size() / 3;
                if (amount > 0)
                {
                    GameActions.Bottom.StackPower(new SupportDamagePower(player, amount));
                }
            }

            alreadyPlayed = false;
            CombatStats.onPhaseChanged.Unsubscribe(this);
        }
    }

    public boolean CanAutoPlay(GurenAction gurenAction)
    {
        return !alreadyPlayed;
    }
}