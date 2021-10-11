package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.Ginko_Dazed;
import eatyourbeets.cards.animator.beta.status.Ginko_Wound;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.animator.beta.status.Status_Frostbite;
import eatyourbeets.cards.animator.status.*;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Ginko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ginko.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Mushishi);

    public Ginko()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Water(1);
        SetAffinity_Earth(2);
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

    //TODO Altered void status, use multiform for statuses

    private void TransformCard(AbstractCard c) {
        if (c instanceof Wound || c instanceof Status_Wound) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Wound());
        }
        else if (c instanceof Dazed) {
            GameActions.Last.ReplaceCard(c.uuid, new Ginko_Dazed());
        }
        else if (c instanceof Burn || c instanceof Status_Burn) {
            GameActions.Last.ReplaceCard(c.uuid, new SearingBurn());
        }
        else if (c instanceof Status_Frostbite || c instanceof Status_Slimed) {
            ((EYBCard) c).SetForm(1,c.timesUpgraded);
        }
        else if (c instanceof SearingBurn) {
            GameActions.Last.ReplaceCard(c.uuid, new Overheat());
        }
        else {
            GameActions.Last.ReplaceCard(c.uuid, new Crystallize());
        }
    }
}