package eatyourbeets.cards.animator.beta.LogHorizon;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;

public class Serara extends AnimatorCard {
    public static final EYBCardData DATA = Register(Serara.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Serara() {
        super(DATA);

        Initialize(0, 0, 6, 12);
        SetUpgrade(0, 0, 0,6);

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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (TempHPField.tempHp.get(p) <= secondaryValue)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }

        GameActions.Bottom.ModifyAllCopies(Nyanta.DATA.ID)
            .AddCallback(c ->
            {
                c.modifyCostForCombat(-1);
                c.flash();
            });
    }
}