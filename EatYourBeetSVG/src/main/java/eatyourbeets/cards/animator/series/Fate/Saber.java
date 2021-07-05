package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Excalibur;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Saber extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saber.class).SetAttack(1, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Excalibur(), false);
    }

    public Saber()
    {
        super(DATA);

        Initialize(9, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 1, 1);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
        SetSynergy(Synergies.Fate);
        SetAffinity(2, 2, 0, 2, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        int progress = 1;
        if (isSynergizing)
        {
            progress += 2;
        }

        cooldown.ProgressCooldownAndTrigger(progress, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Excalibur());
    }
}