package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Excalibur;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Saber extends AnimatorCard
{
    public static final String ID = Register_Old(Saber.class);
    static
    {
        GetStaticData(ID).InitializePreview(new Excalibur(), false);
    }

    public Saber()
    {
        super(ID, 1, CardRarity.RARE, EYBAttackType.Normal);

        Initialize(9, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 1, 1);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
        SetSynergy(Synergies.Fate);
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
        if (HasSynergy())
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