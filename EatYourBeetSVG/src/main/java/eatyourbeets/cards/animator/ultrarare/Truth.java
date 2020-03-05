package eatyourbeets.cards.animator.ultrarare;

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
import eatyourbeets.cards.base.*;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Truth extends AnimatorCard_UltraRare
{
    private static final Wound wound = new Wound();

    public static final EYBCardData DATA = Register(Truth.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new FakeAbstractCard(wound), false);
    }

    public Truth()
    {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int amount = 1;

        GameActions.Bottom.GainForce(magicNumber);
        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.GainIntellect(magicNumber);
        GameActions.Bottom.GainStrength(magicNumber);
        GameActions.Bottom.GainEnergy(magicNumber);

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

    private void AddWound(AbstractPlayer p)
    {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : p.masterDeck.group)
        {
            if (!c.cardID.equals(wound.cardID) && !c.cardID.equals(this.cardID)
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
            .SetMessage(GR.Common.Strings.GridSelection.TransformInto(wound.name))
            .AddCallback(cards ->
            {
                AbstractCard card = cards.get(0);
                AbstractDungeon.player.masterDeck.removeCard(card);
                AbstractDungeon.player.masterDeck.addToTop(wound.makeCopy());
            });
        }
    }
}