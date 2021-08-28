package eatyourbeets.cards.animator.series.Fate;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Saber_Excalibur;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Saber extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saber.class)
            .SetAttack(1, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Saber_Excalibur(), false));

    public Saber()
    {
        super(DATA);

        Initialize(9, 0, 0);
        SetUpgrade(2, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(2);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        cooldown.ProgressCooldownAndTrigger(isSynergizing ? 3 : 1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Saber_Excalibur());
    }
}