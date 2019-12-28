package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;

public abstract class OrbCore_AbstractPower extends AnimatorPower
{
    private static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);

    protected int value;
    protected int uses;
    protected boolean firstSynergy;

    public OrbCore_AbstractPower(String id, AbstractCreature owner, int amount)
    {
        super(owner, id);

        this.firstSynergy = PlayerStatistics.getSynergiesThisTurn() == 0;
        this.uses = amount;
        this.amount = amount;
    }

    @Override
    public void updateDescription()
    {
        if (firstSynergy)
        {
            this.description = "Needs 1 more Synergy";
        }
        else
        {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1] + this.value + powerStrings.DESCRIPTIONS[2];
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.uses += stackAmount;
        this.updateDescription();
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (firstSynergy)
        {
            super.renderIcons(sb, x, y, disabledColor);
        }
        else
        {
            super.renderIcons(sb, x, y, c);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        this.firstSynergy = true;
        this.amount = uses;
        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        AnimatorCard card = JavaUtilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasSynergy())
        {
            if (firstSynergy)
            {
                firstSynergy = false;
            }
            else if (amount > 0)
            {
                amount -= 1;

                OnSynergy(AbstractDungeon.player, usedCard);

                this.flash();
            }

            updateDescription();
        }
    }

    protected abstract void OnSynergy(AbstractPlayer p, AbstractCard usedCard);
}