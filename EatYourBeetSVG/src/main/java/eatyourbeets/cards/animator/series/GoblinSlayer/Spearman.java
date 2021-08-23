package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Spearman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Spearman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new FakeAbstractCard(new Wound()), false);
    }

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Spearman()
    {
        super(DATA);

        Initialize(10, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.GainForce(1, true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (!CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.MakeCardInDrawPile(new Wound()).SetDestination(CardSelection.Top);
        }
    }
}