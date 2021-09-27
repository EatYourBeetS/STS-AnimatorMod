package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.*;
import eatyourbeets.cards.animator.status.*;
import eatyourbeets.cards.base.*;
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
        SetAffinity_Orange(2);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
        if (c instanceof Slimed || c instanceof Status_Slimed || c instanceof Hans_Slimed) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Slimed());
        }
        else if (c instanceof Wound || c instanceof Status_Wound) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Wound());
        }
        else if (c instanceof Dazed) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Dazed());
        }
        else if (c instanceof Burn || c instanceof Status_Burn) {
            GameActions.Last.ReplaceCard(c.uuid, new SearingBurn());
        }
        else if (c instanceof VoidCard) {
            AbyssalVoid v = new AbyssalVoid();
            GameUtilities.ModifyCostForCombat(v, 0, false);
            GameActions.Last.ReplaceCard(c.uuid, v);
        }
        else if (c instanceof Frostbite) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Frostbite());
        }
        else if (c instanceof SearingBurn) {
            GameActions.Last.ReplaceCard(c.uuid, new Overheat());
        }
        else {
            GameActions.Last.ReplaceCard(c.uuid, new Crystallize());
        }
    }
}