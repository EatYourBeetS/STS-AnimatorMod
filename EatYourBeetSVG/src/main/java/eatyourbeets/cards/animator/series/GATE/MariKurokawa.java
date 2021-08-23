package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;

public class MariKurokawa extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MariKurokawa.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public MariKurokawa()
    {
        super(DATA);

        Initialize(0, 0, 7, 9);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 1, 0);
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return heal <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(heal, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        CalculateHeal();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DiscardFromHand(name, 2, false)
        .SetOptions(false, true, false)
        .SetFilter(c -> c.type == CardType.ATTACK)
        .AddCallback(cards ->
        {
            if (cards.size() >= 2)
            {
                GameActions.Bottom.GainBlock(secondaryValue);
            }
        });

        if (CalculateHeal() > 0)
        {
            GameActions.Bottom.Heal(heal);
        }
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(magicNumber, GameActionManager.playerHpLastTurn - player.currentHealth);
    }
}