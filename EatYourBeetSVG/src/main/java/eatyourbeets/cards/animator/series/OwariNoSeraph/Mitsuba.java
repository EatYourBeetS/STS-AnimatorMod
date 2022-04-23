package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Mitsuba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mitsuba.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Mitsuba()
    {
        super(DATA);

        Initialize(6, 2, 2);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnAffinitySeal(boolean manual)
    {
        super.triggerOnAffinitySeal(manual);
        if (CombatStats.TryActivateLimited(cardID)) {
            GameEffects.Queue.ShowCardBriefly(makeStatEquivalentCopy());
            this.affinities.sealed = false;
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (GameUtilities.InEliteRoom())
        {
            DamageModifiers.For(this).Add(magicNumber);
            BlockModifiers.For(this).Add(magicNumber);
        }
    }
}