package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Haniwa;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class MayumiJoutouguu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MayumiJoutouguu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Haniwa(), false));

    public MayumiJoutouguu()
    {
        super(DATA);

        Initialize(0, 7, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(Affinity.Orange, 3);

        SetCooldown(1, 0, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        cooldown.ProgressCooldownAndTrigger(m);

        if (info.CanActivateSemiLimited && TrySpendAffinity(Affinity.Orange) && info.TryActivateSemiLimited())
        {
            GameActions.Bottom.GainPlatedArmor(magicNumber);
        }
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        for (int i=0; i<2; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Haniwa());
        }
    }
}