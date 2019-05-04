package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Truth extends AnimatorCard_UltraRare
{
    private static final String[] TEXT = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Actions).TEXT;

    public static final String ID = CreateFullID(Truth.class.getSimpleName());

    public Truth()
    {
        super(ID, 1, CardType.SKILL, CardTarget.SELF);

        Initialize(0,0, 4);

        this.baseSecondaryValue = this.secondaryValue = 2;

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, this.secondaryValue), this.secondaryValue);
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber);

        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (c.type != CardType.CURSE && c.type != CardType.STATUS && !c.cardID.equals(this.cardID))
            {
                temp.group.add(c);
            }
        }

        if (temp.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, temp, this::OnCardSelected, this, TEXT[5] + Wound.NAME));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeMagicNumber(2);
        }
    }

    private void OnCardSelected(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() == 1)
        {
            AbstractCard card = cards.get(0);
            AbstractPlayer p = AbstractDungeon.player;
            p.masterDeck.removeCard(card);
            p.masterDeck.addToTop(new Wound());
        }
    }
}