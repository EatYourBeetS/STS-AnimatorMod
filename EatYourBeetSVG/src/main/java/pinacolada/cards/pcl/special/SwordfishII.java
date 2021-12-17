package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class SwordfishII extends PCLCard
{
    public static final PCLCardData DATA = Register(SwordfishII.class).SetSkill(0, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.CowboyBebop);

    public SwordfishII()
    {
        super(DATA);

        Initialize(0, 0, 10, 1);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Silver(1);

        SetAutoplay(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        PCLActions.Bottom.Motivate(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.WHITE));
        PCLActions.Bottom.StackPower(new SwordfishIIPower(p, magicNumber));
        PCLActions.Bottom.Motivate(secondaryValue);
    }

    public static class SwordfishIIPower extends PCLPower
    {
        public static final String POWER_ID = CreateFullID(SwordfishIIPower.class);

        private int strikeCount;

        public SwordfishIIPower(AbstractCreature owner, int amount)
        {
            super(owner, POWER_ID);

            strikeCount = 0;
            ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
            for (AbstractCard abstractCard : cardsPlayed)
            {
                if (abstractCard.tags.contains(CardTags.STRIKE) || abstractCard.tags.contains(CardTags.STARTER_STRIKE))
                {
                    strikeCount += 1;
                }
            }

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
            this.description = FormatDescription(0, amount, strikeCount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            if (strikeCount > 0)
            {
                int damagePerCreature = strikeCount * this.amount;
                int[] damage = DamageInfo.createDamageMatrix(damagePerCreature, true);
                PCLActions.Bottom.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.GUNSHOT);
                PCLActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
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