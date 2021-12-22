package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import pinacolada.actions.special.GurenAction;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.common.SupportDamagePower;
import pinacolada.utilities.PCLActions;

public class Guren extends PCLCard implements OnPhaseChangedSubscriber
{
    public static final PCLCardData DATA = Register(Guren.class)
            .SetSkill(3, CardRarity.RARE)
            .SetMaxCopies(1)
            .SetSeries(CardSeries.OwariNoSeraph);

    private boolean alreadyPlayed = false;

    public Guren()
    {
        super(DATA);

        Initialize(0, 1,3, 4);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 5);

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
        PCLActions.Bottom.GainBlock(block);
        for (int i = 0; i < magicNumber; i++)
        {
            PCLActions.Bottom.Add(new GurenAction(m));
        }

        alreadyPlayed = true;
        PCLCombatStats.onPhaseChanged.Subscribe(this);
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        if (phase == GameActionManager.Phase.WAITING_ON_USER)
        {
            if (TrySpendAffinity(PCLAffinity.Light))
            {
                int amount = player.exhaustPile.size() / 3;
                if (amount > 0)
                {
                    PCLActions.Bottom.StackPower(new SupportDamagePower(player, amount));
                }
            }

            alreadyPlayed = false;
            PCLCombatStats.onPhaseChanged.Unsubscribe(this);
        }
    }

    public boolean CanAutoPlay(GurenAction gurenAction)
    {
        return !alreadyPlayed;
    }
}