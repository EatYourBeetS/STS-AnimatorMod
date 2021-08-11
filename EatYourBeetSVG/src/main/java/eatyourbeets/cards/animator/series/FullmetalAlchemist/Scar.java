package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
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
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(__ ->
        {
            SFX.Play(SFX.ORB_DARK_EVOKE, 0.5f, 0.7f);
            return 0f;
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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

        if (isSynergizing)
        {
            GameActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
            .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0], secondaryValue)
            .SetOptions(false, true)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.DealDamage(null, player, secondaryValue, DamageInfo.DamageType.THORNS, AttackEffects.SMASH);
                    GameActions.Top.MoveCard(c, player.exhaustPile, player.discardPile)
                    .ShowEffect(true, true);
                }
            });
        }
    }
}