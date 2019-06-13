package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = CreateFullID(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 2);

        this.exhaust = true;

        AddExtendedDescription();
        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        CardGroup hand = AbstractDungeon.player.hand;
        int toDiscard = 0;
        if (hand.contains(this))
        {
            toDiscard = -1;
        }
        toDiscard += hand.size();

        return toDiscard >= 2 && super.cardPlayable(m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.Discard(this.magicNumber, false);

        GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new BlackLotus(), 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }
}