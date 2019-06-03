package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseAnyNumberFromPileAction;
import eatyourbeets.actions.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Geryuganshoop extends AnimatorCard
{
    public static final String ID = CreateFullID(Geryuganshoop.class.getSimpleName());

    public Geryuganshoop()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0,2, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.CycleCardAction(this.secondaryValue);
        String message = cardStrings.EXTENDED_DESCRIPTION[0].replace("@", String.valueOf(magicNumber));
        GameActionsHelper.AddToBottom(new ChooseAnyNumberFromPileAction(magicNumber, p.exhaustPile, this::OnCardChosen, this, message, true));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
            upgradeMagicNumber(1);
        }
    }

    private void OnCardChosen(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards != null && cards.size() > 0)
        {
            for (AbstractCard card : cards)
            {
                AbstractDungeon.player.exhaustPile.removeCard(card);
                CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
            }

            GameActionsHelper.GainEnergy(cards.size());
        }
    }
}