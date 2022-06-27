package eatyourbeets.cards.animator.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ReimuHakurei extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ReimuHakurei.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Light), false));

    public ReimuHakurei()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Light(2, 0, 1);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Last.SelectFromPile(name, 1, p.hand)
                .SetOptions(false, true)
                .SetFilter(GameUtilities::CanSeal)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        GameActions.Bottom.SealAffinities(c, false);
                    }
                });
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);
        GameActions.Bottom.ObtainAffinityToken(Affinity.Light, false);
    }
}

