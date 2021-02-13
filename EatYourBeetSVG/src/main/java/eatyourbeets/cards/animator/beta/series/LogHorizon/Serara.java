package eatyourbeets.cards.animator.beta.series.LogHorizon;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Serara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Serara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Serara()
    {
        super(DATA);

        Initialize(0, 0, 6, 17);
        SetUpgrade(0, 0, 0, 6);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (!GameUtilities.InBattle() || TempHPField.tempHp.get(player) <= secondaryValue)
        {
            return TempHPAttribute.Instance.SetCard(this, true);
        }

        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (TempHPField.tempHp.get(p) <= secondaryValue)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }

        for (AbstractCard c : player.drawPile.group)
        {
            if (c.cardID.equals(Nyanta.DATA.ID))
            {
                GameActions.Bottom.Motivate(c, 1);
            }
        }

        for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
        {
            if (c.cardID.equals(Nyanta.DATA.ID))
            {
                GameActions.Bottom.Motivate(c, 1);
            }
        }
    }
}