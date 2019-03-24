package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Kuroyukihime extends AnimatorCard
{
    public static final String ID = CreateFullID(Kuroyukihime.class.getSimpleName());

    public Kuroyukihime()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0);

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
        GameActionsHelper.ChooseAndDiscard(2, false);

        BlackLotus blackLotus = new BlackLotus();
        if (upgraded)
        {
            blackLotus.upgrade();
        }

        GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(blackLotus, 1, true, true));
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}