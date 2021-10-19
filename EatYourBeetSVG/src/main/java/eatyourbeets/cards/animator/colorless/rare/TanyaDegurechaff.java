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
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.YoujoSenki)
            .PostInitialize(data -> data.AddPreview(new TanyaDegurechaff_Type95(), false));

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(7, 2, 2);
        SetUpgrade(2, 2, 0);

        SetAffinity_Silver(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(Affinity.General, 11);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.75f, 0.8f));
        GameActions.Bottom.DiscardFromPile(name, magicNumber, p.drawPile)
        .ShowEffect(true, true)
        .SetFilter(GameUtilities::IsHindrance)
        .SetOptions(CardSelection.Top, true);

        if (CheckAffinity(Affinity.General) && info.TryActivateLimited())
        {
            GameActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
                GameActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());
            });
        }
    }
}