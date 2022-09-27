package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Scar.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Scar()
    {
        super(DATA);

        Initialize(14, 0, 0, 4);
        SetUpgrade(4, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);

        SetAffinityRequirement(Affinity.Blue, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(__ ->
        {
            SFX.Play(SFX.ORB_DARK_EVOKE, 0.5f, 0.7f);
            return 0f;
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(true, true, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.ChannelOrb(new Earth());
            }
        });

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
            .SetOptions(false, true)
            .AddCallback(cards ->
            {
                if (cards.size() > 0 && TryUseAffinity(Affinity.Blue))
                {
                    GameActions.Bottom.TakeDamageAtEndOfTurn(secondaryValue, AttackEffects.SMASH);
                    GameActions.Top.MoveCard(cards.get(0), player.exhaustPile, player.discardPile)
                    .ShowEffect(true, true);
                }
            });
        }
    }
}