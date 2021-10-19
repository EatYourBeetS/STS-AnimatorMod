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
import eatyourbeets.utilities.GameUtilities;

public class Saber extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Saber.class)
            .SetAttack(2, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Saber_Excalibur(), false));

    public Saber()
    {
        super(DATA);

        Initialize(10, 10, 5);
        SetUpgrade(0, 0);

        SetAffinity_Light(2);
        SetAffinity_Steel();

        SetCooldown(7, -2, this::OnCooldownCompleted);
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
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);

        int amountProgress = 1;

        amountProgress += (GameUtilities.GetAffinityAmount(Affinity.Light))/magicNumber;
        amountProgress += (GameUtilities.GetAffinityAmount(Affinity.Steel))/magicNumber;

        cooldown.ProgressCooldownAndTrigger(amountProgress, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Saber_Excalibur());
    }
}