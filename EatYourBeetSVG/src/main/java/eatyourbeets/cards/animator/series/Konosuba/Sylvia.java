package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Sylvia_Chimera;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Sylvia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sylvia.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.Konosuba_Sylvia(4, Hans.DATA, Verdia.DATA, Sylvia_Chimera.DATA));
                data.AddPreview(new Sylvia_Chimera(), true);
            });

    public Sylvia()
    {
        super(DATA);

        Initialize(0, 5, 2, 3);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Dark(1);
        SetAffinity_Star(0, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        final Affinity a = GameUtilities.GetRandomElement(Affinity.Basic());
        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(a, false));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SealAffinities(p.drawPile, magicNumber, false)
        .SetSelection(CardSelection.Random)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameEffects.List.ShowCopies(cards);
            }
        });
        GameActions.Bottom.Callback(() -> CombatStats.Affinities.UseAffinity(Affinity.Star, 1));
    }
}