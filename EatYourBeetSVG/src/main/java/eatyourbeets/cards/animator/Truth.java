package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.defect.RemoveAllOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.resources.Resources_Animator_Strings;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions._legacy.common.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Truth extends AnimatorCard_UltraRare
{
    private static final String[] TEXT = Resources_Animator_Strings.Actions.TEXT;

    public static final String ID = Register(Truth.class.getSimpleName(), EYBCardBadge.Special);

    public Truth()
    {
        super(ID, 1, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int amount = 1;

        GameActions.Bottom.GainFocus(amount);
        GameActions.Bottom.GainEnergy(amount + 1);
        GameActions.Bottom.Draw(amount + 2);
        GameActions.Bottom.GainStrength(amount + 3);

        int count = 0;
        ArrayList<String> orbs = new ArrayList<>();
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
        {
            if (!(orb instanceof EmptyOrbSlot) && !orbs.contains(orb.ID))
            {
                orbs.add(orb.ID);
                count += 1;
            }
        }

        if (count >= 3)
        {
            GameActions.Bottom.Add(new RemoveAllOrbsAction());
        }
        else
        {
            AddWound(p);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }

    private void AddWound(AbstractPlayer p)
    {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!c.cardID.equals(Wound.ID) && !c.cardID.equals(this.cardID)
                && !c.cardID.equals(Necronomicurse.ID)
                && !c.cardID.equals(AscendersBane.ID)
                && !SoulboundField.soulbound.get(c))
            {
                temp.group.add(c);
            }
        }

        if (temp.size() > 0)
        {
            GameActions.Bottom.SelectFromPile(name, 1, temp)
            .SetOptions(false, false)
            .SetMessage(TEXT[5] + Wound.NAME)
            .AddCallback(cards ->
            {
                AbstractCard card = cards.get(0);
                AbstractDungeon.player.masterDeck.removeCard(card);
                AbstractDungeon.player.masterDeck.addToTop(new Wound());
            });
        }
    }
}