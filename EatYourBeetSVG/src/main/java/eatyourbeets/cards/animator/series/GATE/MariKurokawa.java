package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MariKurokawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MariKurokawa.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    public static final int DISCARD_AMOUNT = 2;

    public MariKurokawa()
    {
        super(DATA);

        Initialize(0, 0, 6, 9);
        SetUpgrade(0, 0, 2, 0);

        SetAffinity_Green(1);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(DISCARD_AMOUNT, Colors.Cream(1));
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCardHeal(this);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.RecoverHP(magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainBlock(secondaryValue);
            GameActions.Bottom.DiscardFromHand(name, DISCARD_AMOUNT, true)
            .ShowEffect(true, true)
            .SetOptions(true, false, true)
            .SetFilter(c -> c.type == CardType.ATTACK);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
        {
            if (c.type == CardType.ATTACK)
            {
                return false;
            }
        }

        return true;
    }
}