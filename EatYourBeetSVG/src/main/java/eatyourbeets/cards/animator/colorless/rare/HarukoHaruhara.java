package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class HarukoHaruhara extends AnimatorCard
{
    public static final String ID = Register(HarukoHaruhara.class);

    public HarukoHaruhara()
    {
        super(ID, 1, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(0, 0);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.FLCL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.DiscardFromHand(name, 1, true)
        .AddCallback(m, this::PlayRandomCard);
    }

    private void PlayRandomCard(Object target, ArrayList<AbstractCard> __)
    {
        AbstractPlayer p = AbstractDungeon.player;

        RandomizedList<AbstractCard> playable = new RandomizedList<>();
        RandomizedList<AbstractCard> unplayable = new RandomizedList<>();
        for (AbstractCard card : p.hand.group)
        {
            if (card != this)
            {
                if (GameUtilities.IsCurseOrStatus(card))
                {
                    unplayable.Add(card);
                }
                else
                {
                    playable.Add(card);
                }
            }
        }

        AbstractCard card = null;
        if (playable.Count() > 0)
        {
            card = playable.Retrieve(AbstractDungeon.cardRandomRng);
        }
        else if (unplayable.Count() > 0)
        {
            card = unplayable.Retrieve(AbstractDungeon.cardRandomRng);
        }

        if (card != null)
        {
            GameActions.Bottom.PlayCard(card, p.hand, (AbstractMonster) target);
        }
    }
}