package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Saber_Excalibur;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
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

        SetAffinity_Fire(1, 0, 1);
        SetAffinity_Air(1, 0, 1);
        SetAffinity_Light(2, 0, 1);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainRandomAffinityPower(0,true, Affinity.Light,Affinity.Fire);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        cooldown.ProgressCooldownAndTrigger(info.IsSynergizing ? 3 : 1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Saber_Excalibur());
    }
}