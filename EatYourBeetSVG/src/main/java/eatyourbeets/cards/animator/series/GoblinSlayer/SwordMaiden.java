package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.ListSelection;

public class SwordMaiden extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SwordMaiden.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None, true)
            .SetSeriesFromClassPackage();

    public SwordMaiden()
    {
        super(DATA);

        Initialize(0, 0, 7, 5);

        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetPrimaryInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public AbstractAttribute GetSecondaryInfo()
    {
        return heal > 0 ? HPAttribute.Instance.SetCard(this, false).SetText(heal, Colors.Cream(1)) : null;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.heal = GameUtilities.GetHealthRecoverAmount(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.RecoverHP(secondaryValue);
        GameActions.Bottom.RemoveDebuffs(player, ListSelection.Last(0), 1).AddCallback(debuffs -> {
            if (debuffs.size() == 0) {
                GameActions.Bottom.GainInvocation(secondaryValue);
            }
        });
    }
}