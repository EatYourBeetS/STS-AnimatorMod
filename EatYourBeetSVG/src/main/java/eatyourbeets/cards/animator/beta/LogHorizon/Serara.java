package eatyourbeets.cards.animator.beta.LogHorizon;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.FullmetalAlchemist.RoyMustang;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Serara extends AnimatorCard {
    public static final EYBCardData DATA = Register(Serara.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Serara() {
        super(DATA);

        Initialize(0, 0, 6, 10);
        SetUpgrade(0, 0, 0,4);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (TempHPField.tempHp.get(player) <= secondaryValue) {
            return TempHPAttribute.Instance.SetCard(this, true);
        }

        return null;
    }

    @Override
    public void triggerOnManualDiscard()
    {
        if (!DrawNyanta(player.drawPile))
        {
            if (!DrawNyanta(player.discardPile))
            {
                DrawNyanta(player.exhaustPile);
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (TempHPField.tempHp.get(p) <= secondaryValue)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }

    private boolean DrawNyanta(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (Nyanta.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());
                    GameActions.Top.MoveCard(c, group, player.hand)
                    .ShowEffect(true, true);

                    return true;
                }
            }
        }

        return false;
    }
}