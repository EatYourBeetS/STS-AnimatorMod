package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.TanyaDegurechaff_Type95;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TanyaDegurechaff extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff.class)
            .SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.YoujoSenki)
            .PostInitialize(data -> data.AddPreview(new TanyaDegurechaff_Type95(), false));

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Green(2, 0, 1);
        SetAffinity_Blue(2, 0, 1);

        SetAffinityRequirement(Affinity.Star, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.75f, 0.8f);
        GameActions.Bottom.DiscardFromPile(name, magicNumber, p.drawPile)
        .ShowEffect(true, true)
        .SetFilter(GameUtilities::IsHindrance)
        .SetOptions(CardSelection.Top, true);

        if (info.CanActivateLimited && TryUseAffinity(Affinity.Star) && info.TryActivateLimited())
        {
            GameActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());
        }
    }
}