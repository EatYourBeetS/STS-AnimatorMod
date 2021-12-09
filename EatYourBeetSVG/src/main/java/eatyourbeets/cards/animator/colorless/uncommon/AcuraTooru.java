package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class AcuraTooru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraTooru.class)
            .SetAttack(2, CardRarity.UNCOMMON)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                data.AddPreview(new AcuraAkari(), false);
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public AcuraTooru()
    {
        super(DATA);

        Initialize(4, 0, 2, 2);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1, 0, 2);
        SetAffinity_Red(1);

        SetProtagonist(true);

        SetAffinityRequirement(Affinity.Green, 4);
        SetHitCount(2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
        GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (TrySpendAffinity(Affinity.Green)) {
            GameActions.Bottom.MakeCardInDrawPile(GameUtilities.Imitate(new AcuraAkari()));
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), 1);
        }
    }
}