package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard_Cooldown;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Saber extends AnimatorCard_Cooldown
{
    public static final String ID = Register(Saber.class.getSimpleName(), EYBCardBadge.Synergy);

    public Saber()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(8,0,0);

        SetLoyal(true);
        SetSynergy(Synergies.Fate);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Excalibur(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (ProgressCooldown() || (HasActiveSynergy() && ProgressCooldown()))
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
            SetInnate(true);
        }
    }

    @Override
    protected int GetBaseCooldown()
    {
        return 8;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m)
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.MakeCardInHand(new Excalibur(), 1, false);
        }
    }
}