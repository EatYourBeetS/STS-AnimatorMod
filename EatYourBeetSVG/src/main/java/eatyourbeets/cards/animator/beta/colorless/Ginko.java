package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

        Initialize(0, 3, 3);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1);
        SetAffinity_Orange(1, 0, 1);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
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
        if (c instanceof Burn || c instanceof Status_Burn) {
            GameActions.Last.ReplaceCard(c.uuid, new SearingBurn());
        }
        else if (c instanceof Status_Frostbite || c instanceof Status_Slimed || c instanceof Status_Wound || c instanceof Status_Void || c instanceof Status_Dazed || c instanceof SearingBurn) {
            ((EYBCard) c).SetForm(1,c.timesUpgraded);
        }
        else {
            GameActions.Last.ReplaceCard(c.uuid, new Crystallize());
        }
    }
}