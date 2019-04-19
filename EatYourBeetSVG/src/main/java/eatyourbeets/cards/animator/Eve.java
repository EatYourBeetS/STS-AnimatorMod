package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.EvePower;

import java.util.ArrayList;

public class Eve extends AnimatorCard
{
    public static final String ID = CreateFullID(Eve.class.getSimpleName());

    public Eve()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new EvePower(p, this.magicNumber, 1), 1);

        CardGroup cores = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        cores.addToTop(new OrbCore_Dark());
        cores.addToTop(new OrbCore_Frost());
        cores.addToTop(new OrbCore_Lightning());
        cores.addToTop(new OrbCore_Plasma());

        GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, cores, this::OrbChosen, this, ""));
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        if (state == this && chosen != null && chosen.size() == 1)
        {
            AbstractCard card = chosen.get(0);
            AbstractDungeon.player.drawPile.addToTop(card);
            card.applyPowers();
            GameActionsHelper.AddToBottom(new PlayTopCardAction(null, false));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(2);
        }
    }
}