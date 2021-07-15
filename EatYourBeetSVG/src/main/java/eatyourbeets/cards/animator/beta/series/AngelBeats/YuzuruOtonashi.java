package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuzuruOtonashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuzuruOtonashi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public YuzuruOtonashi()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0, 0, 1, 0);

        SetHaste(true);
        SetAffinity_Green(1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false)
        .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                if (card.type == CardType.ATTACK)
                {
                    GameActions.Bottom.GainForce(1, true);
                }
                else if (card.type == CardType.SKILL)
                {
                    GameActions.Bottom.GainTemporaryHP(secondaryValue);
                }
                else if (GameUtilities.IsCurseOrStatus(card))
                {
                    GameActions.Bottom.Draw(1);
                }
            }
        });
    }
}