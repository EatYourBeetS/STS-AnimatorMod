package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Megumin_Explosion;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class)
            .SetSkill(X_COST, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Megumin_Explosion(), true);
                data.AddPreview(AffinityToken.GetCard(Affinity.Blue), false);
            });

    public Megumin()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Red(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final int charge = GameUtilities.UseXCostEnergy(this);
        final Megumin_Explosion card = new Megumin_Explosion();
        for (int i = 0; i < charge; i++)
        {
            card.upgrade();
        }

        GameActions.Bottom.SFX(SFX.ANIMATOR_MEGUMIN_CHARGE, 0.95f, 1.05f);
        GameActions.Bottom.MakeCardInDrawPile(card)
        .SetDestination(CardSelection.Bottom(upgraded ? (p.drawPile.size() / 2) : 0));

        final int amount = charge - magicNumber;
        if (amount > 0)
        {
            for (Affinity a : Affinity.Basic())
            {
                GameActions.Bottom.StackAffinityPower(a, amount);
            }
        }
    }
}