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

        Initialize(12, 0);
        SetUpgrade(4, 0);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Fire, 2);
        SetAffinityRequirement(Affinity.Air, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f);
        GameActions.Bottom.RaiseAirLevel(1, true);
        GameActions.Bottom.RaiseFireLevel(1, true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckAffinity(Affinity.Fire) && CheckAffinity(Affinity.Air))
        {
            GameActions.Bottom.MakeCardInHand(new Status_Wound());
        }
        else {
            GameActions.Bottom.MakeCardInDrawPile(new Status_Wound())
                    .SetDestination(CardSelection.Top);
        }
    }
}