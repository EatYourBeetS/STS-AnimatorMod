package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnPhaseChangedSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Guren extends AnimatorCard implements OnPhaseChangedSubscriber
{
    public static final String ID = Register(Guren.class.getSimpleName(), EYBCardBadge.Special);

    public Guren()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(0, 0,3);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void didDiscard()
    {
        super.didDiscard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActionsHelper.AddToTop(new GurenAction(m, this.magicNumber));
        }

        if (EffectHistory.TryActivateSemiLimited(this.cardID))
        {
            PlayerStatistics.onPhaseChanged.Subscribe(this);
            //GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, amount), amount);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }

    @Override
    public void OnPhaseChanged(GameActionManager.Phase phase)
    {
        if (phase == GameActionManager.Phase.WAITING_ON_USER)
        {
            AbstractPlayer p = AbstractDungeon.player;
            int amount = p.exhaustPile.size();
            if (amount > 0)
            {
                GameActionsHelper.ApplyPower(p, p, new SupportDamagePower(p, amount), amount);
            }

            PlayerStatistics.onPhaseChanged.Unsubscribe(this);
        }
    }
}