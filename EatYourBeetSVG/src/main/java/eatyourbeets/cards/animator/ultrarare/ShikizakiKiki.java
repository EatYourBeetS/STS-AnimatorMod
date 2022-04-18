package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ShikizakiKiki extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(ShikizakiKiki.class)
            .SetPower(2, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Katanagatari);
    public static final int POWER_ENERGY_COST = 2;
    public static final int BLOCK_DAMAGE_BONUS = 2;

    public ShikizakiKiki()
    {
        super(DATA);

        Initialize(0, 0, BLOCK_DAMAGE_BONUS, POWER_ENERGY_COST);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new ShikizakiKikiPower(p, 1));
    }

    public static class ShikizakiKikiPower extends AnimatorClickablePower
    {
        private int attacks;

        public ShikizakiKikiPower(AbstractCreature owner, int amount)
        {
            super(owner, ShikizakiKiki.DATA, PowerTriggerConditionType.Energy, POWER_ENERGY_COST);

            triggerCondition.SetUses(1, true, false);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, triggerCondition.requiredAmount, 1, amount);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard card)
        {
            super.onAfterCardPlayed(card);

            if (card.type == CardType.ATTACK)
            {
                attacks += 1;

                if (attacks >= 3)
                {
                    attacks = 0;
                    GameActions.Bottom.GainInspiration(amount);
                    this.flash();
                }
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            for (AbstractCard c : player.drawPile.group)
            {
                if (c.baseBlock > 0)
                {
                    GameUtilities.IncreaseBlock(c, BLOCK_DAMAGE_BONUS, false);
                }
                if (c.baseDamage > 0)
                {
                    GameUtilities.IncreaseDamage(c, BLOCK_DAMAGE_BONUS, false);
                }
            }

            GameActions.Bottom.SFX(SFX.CARD_UPGRADE, 0.5f, 0.6f).SetDuration(0.25f, true);
            GameActions.Bottom.SFX(SFX.ATTACK_FIRE, 0.5f, 0.6f).SetDuration(0.25f, true);
            GameActions.Bottom.SFX(SFX.ATTACK_AXE, 0.5f, 0.6f).SetDuration(0.25f, true);
            GameActions.Bottom.SFX(SFX.CARD_UPGRADE, 0.5f, 0.6f).SetDuration(0.25f, true);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(attacks, Color.WHITE, c.a);
        }
    }
}