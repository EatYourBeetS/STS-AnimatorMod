package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.UUID;

public class Iris extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Iris.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None, true)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();
    private static HashMap<UUID, Integer> buffs;

    public Iris()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0,0,1, 1);

        SetAffinity_Light(2, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        buffs = CombatStats.GetCombatData(cardID, null);
        if (buffs == null)
        {
            buffs = new HashMap<>();
            CombatStats.SetCombatData(cardID, buffs);
        }

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetFilter(c -> c.type == CardType.ATTACK)
        .SetOptions(false, false, false)
        .AddCallback((cards) ->
        { //
            for (AbstractCard c : cards) {
                if (c instanceof EYBCard && ((EYBCard) c).attackType.equals(EYBAttackType.Normal)) {
                    GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
                    GameActions.Bottom.MakeCardInHand(GameUtilities.Imitate(c));
                }
                else {
                    GameActions.Bottom.SelectFromHand(name, 1, false)
                            .SetOptions(false, false, false)
                            .SetMessage(GR.Common.Strings.HandSelection.GenericBuff)
                            .SetFilter(c2 -> c2 instanceof EYBCard && !GameUtilities.IsHindrance(c2) && buffs.getOrDefault(c2.uuid, 0) < secondaryValue && (c2.baseDamage >= 0 || c2.baseBlock >= 0))
                            .AddCallback(cards2 ->
                            {
                                for (AbstractCard c2 : cards2)
                                {
                                    int amount = Math.min(secondaryValue,c.costForTurn + 1);
                                    GameActions.Bottom.IncreaseScaling(c2, Affinity.Light, amount);
                                    JUtils.IncrementMapElement(buffs, c2.uuid, amount);
                                    c2.flash();
                                }
                            });
                }
            }
        });
    }
}