package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ChihayaOhtori extends AnimatorCard {
    public static final EYBCardData DATA = Register(ChihayaOhtori.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal);

    public ChihayaOhtori() {
        super(DATA);

        Initialize(8, 0, 1,3);
        SetUpgrade(3, 0, 0);
        SetScaling(1,0,1);

        SetHaste(true);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.haste)
        {
            GameActions.Bottom.GainTemporaryArtifact(secondaryValue);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Top.FetchFromPile(name, 1, player.discardPile)
        .SetOptions(false, false)
        .SetFilter(this::CardIsMartialArtist)
        .AddCallback(cards -> {
            if (cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                card.setCostForTurn(card.costForTurn - 1);
                GameActions.Bottom.Add(new RefreshHandLayout());
            }
        });
    }

    private boolean CardIsMartialArtist(AbstractCard card)
    {
        return card.hasTag(MARTIAL_ARTIST);
    }
}