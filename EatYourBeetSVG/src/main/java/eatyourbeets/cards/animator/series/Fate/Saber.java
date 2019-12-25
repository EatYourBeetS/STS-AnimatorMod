package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Excalibur;
import eatyourbeets.cards.base.AnimatorCard_Cooldown;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Saber extends AnimatorCard_Cooldown
{
    public static final String ID = Register(Saber.class.getSimpleName(), EYBCardBadge.Synergy);

    public Saber()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(9, 0, 0);
        SetUpgrade(2, 0, 0);

        SetLoyal(true);
        SetSynergy(Synergies.Fate);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Excalibur(), false);
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        int progress = 1;
        if (HasActiveSynergy())
        {
            progress += 2;
        }

        if (ProgressCooldown(progress))
        {
            OnCooldownCompleted(p, m);
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
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Excalibur());
    }
}