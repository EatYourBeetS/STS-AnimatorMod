package eatyourbeets.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.animator.*;
import eatyourbeets.misc.RandomizedList;

import java.util.ArrayList;

public abstract class OrbCore_AbstractPower extends AnimatorPower
{
    protected int value;
    protected int uses;
    protected boolean firstSynergy;

    private static final Color disabledColor = new Color(0.5f, 0.5f, 0.5f, 1);

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

        AnimatorCard card = Utilities.SafeCast(usedCard, AnimatorCard.class);
        if (card != null && card.HasActiveSynergy())
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

    private static final ArrayList<AbstractCard> cores = new ArrayList<>();

    public static CardGroup CreateCoresGroup(int choices)
    {
        if (cores.size() == 0)
        {
            cores.add(new OrbCore_Lightning());
            cores.add(new OrbCore_Frost());
            cores.add(new OrbCore_Plasma());
            cores.add(new OrbCore_Dark());
            cores.add(new OrbCore_Chaos());
        }

        if (choices > cores.size())
        {
            choices = cores.size();
        }

        RandomizedList<AbstractCard> temp = new RandomizedList<>(cores);
        Random rng = AbstractDungeon.miscRng;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        for (int i = 0; i < choices; i++)
        {
            group.group.add(temp.Retrieve(rng, true));
        }

        return group;
    }
}