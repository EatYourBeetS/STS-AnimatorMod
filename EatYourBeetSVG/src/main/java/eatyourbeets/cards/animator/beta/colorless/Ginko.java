package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.*;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.status.Hans_Slimed;
import eatyourbeets.cards.animator.status.Konosuba_Slimed;
import eatyourbeets.cards.animator.status.Overheat;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Ginko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ginko.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Mushishi);

    public Ginko()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, player.hand)
                .SetOptions(false, true)
                .SetFilter(c -> c.type == CardType.STATUS)
                .AddCallback(cards -> {
                    if (cards.size() > 0) {
                        for (AbstractCard c : cards) {
                            TransformCard(c);
                        }
                    }
                });
    }

    private void TransformCard(AbstractCard c) {
        if (c instanceof Slimed || c instanceof Konosuba_Slimed || c instanceof Hans_Slimed) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Slimed());
        }
        else if (c instanceof Wound) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Wound());
        }
        else if (c instanceof Dazed) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Dazed());
        }
        else if (c instanceof Burn) {
            GameActions.Last.ReplaceCard(c.uuid, new SearingBurn());
        }
        else if (c instanceof VoidCard) {
            AbyssalVoid v = new AbyssalVoid();
            GameUtilities.ModifyCostForCombat(v, 0, false);
            GameActions.Last.ReplaceCard(c.uuid, v);
        }
        else if (c instanceof SearingBurn) {
            GameActions.Last.ReplaceCard(c.uuid, new Overheat());
        }
        else {
            GameActions.Last.ReplaceCard(c.uuid, new Crystallize());
        }
    }
}