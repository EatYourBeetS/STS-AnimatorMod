package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class SongOfBrokenPines extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SongOfBrokenPines.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public SongOfBrokenPines()
    {
        super(DATA);

        Initialize(0, 0, 8);
        SetUpgrade(0,0,2);

        SetExhaust(true);
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.PlayCard(this, player.hand, null)
                .SpendEnergy(true)
                .AddCondition(AbstractCard::hasEnoughEnergy);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.WHITE));
        GameActions.Bottom.StackPower(new SongOfBrokenPinesPower(p, magicNumber));
    }

    public static class SongOfBrokenPinesPower extends AnimatorPower
    {
        public static final String POWER_ID = CreateFullID(SongOfBrokenPinesPower.class);

        private int strikeCount;

        public SongOfBrokenPinesPower(AbstractCreature owner, int amount)
        {
            super(owner, POWER_ID);

            strikeCount = 0;
            this.amount = amount;
            updateDescription();
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(strikeCount, Color.WHITE, c.a);
        }

        @Override
        public void updateDescription()
        {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + strikeCount + powerStrings.DESCRIPTIONS[2];
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            // Deal damage dependent on the number of strikes you played after playing this card
            if (strikeCount > 0) {
                int damagePerCreature = strikeCount * this.amount;
                int[] damage = DamageInfo.createDamageMatrix(damagePerCreature, true);
                GameActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            }

            RemovePower();

            super.atEndOfTurn(isPlayer);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            if (usedCard.tags.contains(AbstractCard.CardTags.STRIKE))
            {
                strikeCount += 1;
                updateDescription();
            }
        }
    }
}