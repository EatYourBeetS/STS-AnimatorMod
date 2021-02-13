package eatyourbeets.cards.animator.beta.LogHorizon;

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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            if (c.costForTurn > 0)
            {
                c.superFlash(Color.GOLD);
                CostModifiers.For(c).Add(cardID + uuid, -1);

                if (c.baseBlock >= 0)
                {
                    BlockModifiers.For(c).Add(cardID + uuid, -magicNumber);
                }
                if (c.baseDamage >= 0)
                {
                    DamageModifiers.For(c).Add(cardID + uuid, -magicNumber);
                }

                GameUtilities.TriggerWhenPlayed(c, card ->
                {
                    CostModifiers.For(card).Remove(cardID + uuid);
                    BlockModifiers.For(card).Remove(cardID + uuid);
                    DamageModifiers.For(card).Remove(cardID + uuid);
                });
            }
        }

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Aether(), true);
        }
    }
}