package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.DelayedHealPower;

import java.util.ArrayList;

public class LimeBell extends AnimatorCard
{
    public static final String ID = CreateFullID(LimeBell.class.getSimpleName());

    public LimeBell()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 7, 2);

        this.tags.add(CardTags.HEALING);

        this.exhaust = true;

        AddExtendedDescription();
        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, BaseMod.MAX_HAND_SIZE, this, this::OnDiscard));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        if (state == this && discarded != null && discarded.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.ApplyPower(p, p, new DelayedHealPower(p, discarded.size() * this.magicNumber));
        }
    }
}