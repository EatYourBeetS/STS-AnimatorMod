package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

import java.util.HashSet;

public class ShinjiMatou extends AnimatorCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final EYBCardData DATA = Register(ShinjiMatou.class).SetSkill(1, CardRarity.COMMON);
    static
    {
        DATA.AddPreview(new ShinjiMatou_CommandSpell(), false);
    }

    public ShinjiMatou()
    {
        super(DATA);

        Initialize(0, 1, 1, 1);
        SetUpgrade(0, 0, 2, 0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        cardTypes.clear();
        for (AbstractCard card : player.hand.group)
        {
            cardTypes.add(card.type);
        }

        magicNumber = (baseMagicNumber + (secondaryValue * cardTypes.size()));
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (HasSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new ShinjiMatou_CommandSpell())
            .AddCallback(c -> c.retain = true);
        }
    }
}
