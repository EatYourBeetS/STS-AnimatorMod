package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseFromPileAction;
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

        Initialize(0, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int bonus = upgraded ? 1 : 0;
        GameActionsHelper.GainEnergy(1 + bonus);
        GameActionsHelper.DrawCard(p, 2 + bonus);
        GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 3 + bonus), 3 + bonus);
        GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 4 + bonus), 4 + bonus);

        PayThePrice(p);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    private void PayThePrice(AbstractPlayer p)
    {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!c.cardID.equals(Wound.ID) && !c.cardID.equals(Necronomicurse.ID) && !c.cardID.equals(AscendersBane.ID)
                    && !SoulboundField.soulbound.get(c) && !c.cardID.equals(this.cardID))
            {
                temp.group.add(c);
            }
        }

        if (temp.size() > 0)
        {
            GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, temp, this::OnCardSelected, this, TEXT[5] + Wound.NAME));
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