package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class TsurugaMeisai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TsurugaMeisai.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
        SetAffinity(0, 1, 0, 1, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards != null && cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                switch (card.type)
                {
                    case ATTACK:
                        GameActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(false, true);
                        GameActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(false, true);
                        break;

                    case SKILL:
                        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
                        break;

                    case POWER:
                        GameActions.Bottom.Motivate(2);
                        break;

                    case STATUS:
                    case CURSE:
                        break;
                }
            }
        });
    }
}