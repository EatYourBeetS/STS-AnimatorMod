package eatyourbeets.cards.animator.beta.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class YuzuruOtonashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuzuruOtonashi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self);

    public YuzuruOtonashi()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 1, 0);
        SetSynergy(Synergies.AngelBeats);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        if (card.type == CardType.ATTACK) {
                            GameActions.Bottom.GainForce(1, true);
                        } else if (card.type == CardType.SKILL) {
                            GameActions.Bottom.GainTemporaryHP(secondaryValue);
                        } else if (card.type == CardType.POWER) {
                            GameActions.Bottom.GainEnergy(1);
                        } else if (card.type == CardType.CURSE || card.type == CardType.STATUS) {
                            GameActions.Bottom.Draw(1);
                        }
                    }
                });
    }
}