package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mayuri extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mayuri.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal).SetSeriesFromClassPackage();

    public Mayuri()
    {
        super(DATA);

        Initialize(8, 0, 2);
        SetUpgrade(0, 0, 1);
        SetAffinity_Light(1, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.LIGHTNING);

        if (GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID) <= magicNumber)
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }

        if (isSynergizing)
        {
            GameActions.Bottom.Callback(cards -> {
                    for (AbstractCard card : GameUtilities.GetOtherCardsInHand(this))
                    {
                        if (card.tags.contains(CardTags.STARTER_DEFEND))
                        {
                            GameUtilities.Retain(card);
                        }
                    }
            });
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
    }
}