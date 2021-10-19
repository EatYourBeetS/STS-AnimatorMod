package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Spearman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Spearman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Status_Wound(), false));

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Spearman()
    {
        super(DATA);

        Initialize(12, 0, 2);
        SetUpgrade(4, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).forEach(d -> d.SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f));
        GameActions.Bottom.GainAgility(magicNumber, true);
        GameActions.Bottom.GainForce(magicNumber, true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(Affinity.Red, Affinity.Green))
        {
            GameActions.Bottom.MakeCardInHand(new Status_Wound());
        }
        else {
            GameActions.Bottom.MakeCardInDrawPile(new Status_Wound())
                    .SetDestination(CardSelection.Top);
        }
    }
}