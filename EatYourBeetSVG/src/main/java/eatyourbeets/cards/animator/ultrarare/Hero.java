package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hero extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Hero.class)
            .SetAttack(3, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GoblinSlayer);

    public Hero()
    {
        super(DATA);

        Initialize(30, 0, 10);
        SetUpgrade(10, 0, 0);

        SetAffinity_Star(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Star(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY)
        .AddCallback(c ->
        {
            if (GameUtilities.IsFatal(c, true) && HasEnoughHindrancesInExhaustPile() && info.TryActivateLimited())
            {
                final AbstractCard deckInstance = GameUtilities.GetMasterDeckInstance(uuid);
                if (deckInstance == null)
                {
                    return;
                }

                final AbstractRelic.RelicTier tier;
                final int roll = rng.random(0, 99);
                if (roll < 63)
                {
                    tier = AbstractRelic.RelicTier.COMMON;
                }
                else if (roll < 91)
                {
                    tier = AbstractRelic.RelicTier.UNCOMMON;
                }
                else
                {
                    tier = AbstractRelic.RelicTier.RARE;
                }
            }
        });
    }

    private boolean HasEnoughHindrancesInExhaustPile()
    {
        int hindranceThreshold = 0;

        for (AbstractCard card : player.exhaustPile.group)
        {
            if (GameUtilities.IsHindrance(card))
            {
                hindranceThreshold++;
            }
        }

        return hindranceThreshold >= magicNumber;
    }
}