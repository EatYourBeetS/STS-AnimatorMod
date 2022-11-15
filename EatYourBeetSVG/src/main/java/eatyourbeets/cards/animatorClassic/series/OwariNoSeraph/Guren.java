package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animatorClassic.GurenAction;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;

public class Guren extends AnimatorClassicCard implements OnPhaseChangedSubscriber
{
    public static final EYBCardData DATA = Register(Guren.class).SetSeriesFromClassPackage().SetSkill(3, CardRarity.RARE).SetMaxCopies(2);

    private boolean alreadyPlayed = false;

    public Guren()
    {
        super(DATA);

        Initialize(0, 0,3);

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
            if (CombatStats.TryActivateSemiLimited(this.cardID))
            {
                int amount = player.exhaustPile.size();
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