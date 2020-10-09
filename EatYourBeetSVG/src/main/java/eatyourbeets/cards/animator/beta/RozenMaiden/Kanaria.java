package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kanaria extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kanaria.class)
    		.SetPower(2, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Kanaria_Pizzicato(), false);
    }
    public Kanaria()
    {
        super(DATA);

        Initialize(0, 0);
        SetUpgrade(0, 0);
        
        SetSpellcaster();
        SetCostUpgrade(-1);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new KanariaPower(p, 1));

        if (HasSynergy() && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new Kanaria_Pizzicato())
                    .AddCallback(GameUtilities::Retain);
        }
    }

    public static class KanariaPower extends AnimatorPower
    {
        public static final int BASE_BLOCK = 2;

        public KanariaPower(AbstractCreature owner, int amount)
        {
            super(owner, Kanaria.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount * BASE_BLOCK);
        }

        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            if (power.type == PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) &&
                    source == this.owner && !target.hasPower(ArtifactPower.POWER_ID))
            {
                this.flash();
                GameActions.Bottom.GainBlock(amount * BASE_BLOCK);
            }
        }
    }
}



//    DATA.AddPreview(new Kanaria(), false);

//    public static final EYBCardTooltip Gethit = new EYBCardTooltip(
//            DATA.Strings.EXTENDED_DESCRIPTION[1],DATA.Strings.EXTENDED_DESCRIPTION[2]);
/*
    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(Gethit);
        }
    }

 */

/*
        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            super.wasHPLost(info, damageAmount);

            if (info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > 0)
            {
                flash();
                GameActions.Top.ReducePower(this, 1);

                GameActions.Bottom.MakeCardInDiscardPile(new Kanaria())
                        .AddCallback(card -> card.modifyCostForCombat(-1));
            }
        }
 */

// alternative plan: make it return to your discard pile if you get hit.

// , taking unblocked attack damage will return this.",
//				"Why not get hit",
//				"If you take unblocked attack damage, this will be returned to your discard pile
