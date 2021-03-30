package eatyourbeets.cards.animator.series.LogHorizon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Marielle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Marielle.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Marielle()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetEthereal(true);
        SetExhaust(true);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractCard card : GameUtilities.GetOtherCardsInHand(this))
        {
            if (card.costForTurn > 0)
            {
                final String key = cardID + uuid;

                GameUtilities.Flash(card, Color.GOLD, true);
                CostModifiers.For(card).Add(key, -1);

                if (card.baseBlock >= 0)
                {
                    BlockModifiers.For(card).Add(key, -magicNumber);
                }
                if (card.baseDamage >= 0)
                {
                    DamageModifiers.For(card).Add(key, -magicNumber);
                }

                GameUtilities.TriggerWhenPlayed(card, key, (k, c) ->
                {
                    CostModifiers.For(c).Remove(k, false);
                    BlockModifiers.For(c).Remove(k);
                    DamageModifiers.For(c).Remove(k);
                });
            }
        }

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Aether());
        }
    }
}