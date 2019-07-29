package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import eatyourbeets.actions.common.AddToCardGroupAction;
import eatyourbeets.actions.common.PurgeAnywhereAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class Cthulhu extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(Cthulhu.class.getSimpleName());

    public Cthulhu()
    {
        super(ID, 1, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 0, 66);

        this.purgeOnUse = true;

        SetSynergy(Synergies.CallOfCthulhu);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        PurgeAllCards(p.drawPile);
        PurgeAllCards(p.discardPile);
        PurgeAllCards(p.hand);
        PurgeAllCards(p.exhaustPile);

        GenerateRandomCards();

        GameActionsHelper.PurgeCard(this);
        GameActionsHelper.DrawCard(p, 10);
    }

    private void PurgeAllCards(CardGroup group)
    {
        for (AbstractCard card : group.group)
        {
            GameActionsHelper.AddToBottom(new PurgeAnywhereAction(card, 1));
            GameActionsHelper.SFX("CARD_EXHAUST");
        }
    }

    private void GenerateRandomCards()
    {
        ArrayList<String> keys = new ArrayList<>(CardLibrary.cards.keySet());

        for (int i = 0; i < magicNumber; i++)
        {
            String key = Utilities.GetRandomElement(keys);
            GameActionsHelper.AddToBottom(new AddToCardGroupAction(CardLibrary.cards.get(key).makeCopy(), AbstractDungeon.player.drawPile));
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
}