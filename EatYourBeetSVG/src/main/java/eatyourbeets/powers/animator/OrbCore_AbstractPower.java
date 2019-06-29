package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.animator.*;
import eatyourbeets.utilities.RandomizedList;

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
    private static final RandomizedList<AbstractCard> cores0 = new RandomizedList<>();
    private static final RandomizedList<AbstractCard> cores1 = new RandomizedList<>();
    private static final RandomizedList<AbstractCard> cores2 = new RandomizedList<>();

    public static CardGroup CreateCoresGroup(boolean anyCost)
    {
        if (cores.size() == 0)
        {
            cores0.Add(new OrbCore_Fire());
            cores0.Add(new OrbCore_Lightning());
            cores1.Add(new OrbCore_Dark());
            cores1.Add(new OrbCore_Frost());
            cores2.Add(new OrbCore_Plasma());
            cores2.Add(new OrbCore_Chaos());

            cores.addAll(cores0.GetInnerList());
            cores.addAll(cores1.GetInnerList());
            cores.addAll(cores2.GetInnerList());
        }

        Random rng = AbstractDungeon.miscRng;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.CARD_POOL);

        if (anyCost)
        {
            RandomizedList<AbstractCard> temp = new RandomizedList<>(cores);
            group.group.add(temp.Retrieve(rng, true));
            group.group.add(temp.Retrieve(rng, true));
            group.group.add(temp.Retrieve(rng, true));
        }
        else
        {
            group.group.add(cores0.Retrieve(rng, false));
            group.group.add(cores1.Retrieve(rng, false));
            group.group.add(cores2.Retrieve(rng, false));
        }

        return group;
    }

    public static ArrayList<AbstractCard> GetAllCores()
    {
        ArrayList<AbstractCard> cores = new ArrayList<>();

        cores.add(new OrbCore_Fire());
        cores.add(new OrbCore_Lightning());
        cores.add(new OrbCore_Dark());
        cores.add(new OrbCore_Frost());
        cores.add(new OrbCore_Plasma());
        cores.add(new OrbCore_Chaos());

        return cores;
    }
}